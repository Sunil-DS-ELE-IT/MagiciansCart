package com.elementzit.mc.domain.repository

import com.elementzit.mc.domain.model.User
import com.elementzit.mc.domain.model.UserRole

/**
 * Interface for authentication operations.
 * Defines the contract for user registration, login, and profile management.
 */
interface AuthRepository {
    /**
     * Registers a new user with the given credentials.
     * @param name The user's name.
     * @param email The user's email address.
     * @param password The user's password.
     * @param role The role assigned to the user (e.g., Customer, Admin).
     * @return A [Result] indicating success or failure.
     */
    suspend fun register(name: String, email: String, password: String, role: UserRole): Result<Unit>

    /**
     * Authenticates a user with email and password.
     * @param email The user's email address.
     * @param password The user's password.
     * @return A [Result] containing the authenticated [User] if successful, or an error.
     */
    suspend fun login(email: String, password: String): Result<User>

    /**
     * Checks if a user already exists with the given email.
     * @param email The email to check.
     * @return True if the email is registered, false otherwise.
     */
    suspend fun checkEmailExists(email: String): Boolean

    /**
     * Gets the ID of the currently logged-in user, if any.
     * @return The user ID as a string, or null if no user is logged in.
     */
    fun getCurrentUserId(): String?
}
