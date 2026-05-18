package com.elementzit.mc.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.elementzit.mc.domain.model.User
import com.elementzit.mc.domain.model.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    private val _pendingVendors = MutableStateFlow<List<User>>(emptyList())
    val pendingVendors: StateFlow<List<User>> = _pendingVendors

    init {
        fetchUsers()
    }

    fun fetchUsers() {
        viewModelScope.launch {
            firestore.collection("users")
                .addSnapshotListener { snapshot, e ->
                    if (e != null) return@addSnapshotListener

                    if (snapshot != null) {
                        val allUsers = snapshot.documents.mapNotNull { doc ->
                            val roleStr = doc.getString("role") ?: "CUSTOMER"
                            User(
                                id = doc.id,
                                name = doc.getString("name") ?: "",
                                email = doc.getString("email") ?: "",
                                role = UserRole.valueOf(roleStr),
                                status = doc.getString("status") ?: "APPROVED"
                            )
                        }
                        _users.value = allUsers
                        _pendingVendors.value = allUsers.filter { it.role == UserRole.VENDOR && it.status == "PENDING" }
                    }
                }
        }
    }

    fun approveVendor(userId: String) {
        viewModelScope.launch {
            firestore.collection("users").document(userId)
                .update("status", "APPROVED")
        }
    }

    fun rejectVendor(userId: String) {
        viewModelScope.launch {
            // Depending on policy, either delete the user or mark as REJECTED
            firestore.collection("users").document(userId)
                .update("status", "REJECTED")
        }
    }
}
