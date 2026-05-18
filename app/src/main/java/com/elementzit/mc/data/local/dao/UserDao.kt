package com.elementzit.mc.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elementzit.mc.data.local.entity.UserEntity

/**
 * Data Access Object for the users table.
 */
@Dao
interface UserDao {
    /**
     * Inserts a new user into the database.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: UserEntity)

    /**
     * Retrieves a user by their email address.
     */
    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserEntity?

    /**
     * Authenticates a user by checking email and password.
     */
    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    suspend fun authenticate(email: String, password: String): UserEntity?
}
