package com.elementzit.mc.data.local

import androidx.room.TypeConverter
import com.elementzit.mc.domain.model.UserRole

/**
 * Type converters for Room to handle complex data types.
 */
class Converters {
    /**
     * Converts a [UserRole] to its string representation.
     */
    @TypeConverter
    fun fromUserRole(role: UserRole): String = role.name

    /**
     * Converts a string representation back into a [UserRole].
     */
    @TypeConverter
    fun toUserRole(role: String): UserRole = UserRole.valueOf(role)
}
