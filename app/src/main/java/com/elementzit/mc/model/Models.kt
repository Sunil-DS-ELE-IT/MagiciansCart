package com.elementzit.mc.model

enum class UserRole {
    CUSTOMER,
    VENDOR,
    ADMIN
}

data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole
)

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val vendorId: String,
    val category: String
)
