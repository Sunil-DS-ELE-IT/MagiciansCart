package com.elementzit.mc.data.mapper

import com.elementzit.mc.data.local.entity.UserEntity
import com.elementzit.mc.domain.model.User

/**
 * Maps UserEntity (Database) to User (Domain Model)
 */
fun UserEntity.toDomain(): User {
    return User(
        id = this.id.toString(),
        name = this.name,
        email = this.email,
        role = this.role
    )
}

/**
 * Maps User (Domain Model) to UserEntity (Database)
 * Note: Password handling should be managed separately during registration
 */
fun User.toEntity(password: String): UserEntity {
    return UserEntity(
        id = this.id.toLongOrNull() ?: 0L,
        name = this.name,
        email = this.email,
        password = password,
        role = this.role
    )
}
