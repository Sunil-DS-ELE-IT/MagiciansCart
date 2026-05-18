package com.elementzit.mc.data.model

import com.elementzit.mc.domain.model.UserRole

/**
 * Data layer models (DTOs) used for network or internal data representation.
 */
data class UserDto(
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole
)

data class ProductDto(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val vendorId: String,
    val category: String
)


