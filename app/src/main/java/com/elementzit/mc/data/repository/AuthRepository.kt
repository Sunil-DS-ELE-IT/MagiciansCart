package com.elementzit.mc.data.repository

import com.elementzit.mc.data.local.dao.UserDao
import com.elementzit.mc.data.local.entity.UserEntity

class AuthRepository(private val userDao: UserDao) {
    suspend fun register(user: UserEntity): Boolean {
        return try {
            userDao.insertUser(user)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun login(email: String, password: String): UserEntity? {
        return userDao.authenticate(email, password)
    }

    suspend fun checkEmailExists(email: String): Boolean {
        return userDao.getUserByEmail(email) != null
    }
}
