package com.elementzit.mc.domain.model

enum class UserRole {
    CUSTOMER,
    VENDOR,
    ADMIN
}

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val role: UserRole = UserRole.CUSTOMER,
    val status: String = "APPROVED"
)
