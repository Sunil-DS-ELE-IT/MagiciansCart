package com.elementzit.mc.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.elementzit.mc.data.local.dao.UserDao
import com.elementzit.mc.data.local.entity.UserEntity
import com.elementzit.mc.domain.model.User
import com.elementzit.mc.domain.model.UserRole
import com.elementzit.mc.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [AuthRepository] using Firebase Authentication, Firestore, and local Room database.
 */
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    /**
     * Registers a new user via Firebase Auth, saves their profile in Firestore, 
     * and caches details in the local database.
     */
    override suspend fun register(
        name: String,
        email: String,
        password: String,
        role: UserRole
    ): Result<Unit> {
        return try {
            // 1. Register with Firebase Auth
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: throw Exception("User ID not found")

            // Determine initial status
            val status = if (role == UserRole.VENDOR) "PENDING" else "APPROVED"

            // 2. Save user profile in Firestore
            val userMap = mapOf(
                "uid" to uid,
                "name" to name,
                "email" to email,
                "role" to role.name,
                "status" to status
            )
            firestore.collection("users").document(uid).set(userMap).await()

            // 3. Store in local DB
            val entity = UserEntity(
                name = name,
                email = email,
                password = password, // Note: Storing password locally is generally discouraged
                role = role
            )
            userDao.insertUser(entity)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Authenticates a user with Firebase and retrieves their profile.
     */
    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val uid = firebaseAuth.currentUser?.uid ?: throw Exception("Auth failed")

            // Retrieve profile from Firestore
            val doc = firestore.collection("users").document(uid).get().await()
            if (doc.exists()) {
                val user = User(
                    id = uid,
                    name = doc.getString("name") ?: "",
                    email = doc.getString("email") ?: "",
                    role = UserRole.valueOf(doc.getString("role") ?: "CUSTOMER"),
                    status = doc.getString("status") ?: "APPROVED"
                )
                
                // Optional: You could block login here if (user.role == UserRole.VENDOR && user.status == "PENDING")

                Result.success(user)
            } else {
                Result.failure(Exception("User profile not found in database"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Verifies if a user email is already registered in Firebase.
     */
    override suspend fun checkEmailExists(email: String): Boolean {
        return try {
            val methods = firebaseAuth.fetchSignInMethodsForEmail(email).await()
            methods.signInMethods?.isNotEmpty() == true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Returns the UID of the currently signed-in user from Firebase.
     */
    override fun getCurrentUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }
}
