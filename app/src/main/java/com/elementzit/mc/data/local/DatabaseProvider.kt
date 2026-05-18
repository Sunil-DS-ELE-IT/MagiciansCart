package com.elementzit.mc.data.local

import android.content.Context
import com.elementzit.mc.data.repository.AuthRepository

object DatabaseProvider {
    private var database: MarketplaceDatabase? = null
    private var authRepository: AuthRepository? = null

    fun getDatabase(context: Context): MarketplaceDatabase {
        return database ?: synchronized(this) {
            val instance = MarketplaceDatabase.Companion.getDatabase(context)
            database = instance
            instance
        }
    }

    fun getAuthRepository(context: Context): AuthRepository {
        return authRepository ?: synchronized(this) {
            val repository = AuthRepository(getDatabase(context).userDao())
            authRepository = repository
            repository
        }
    }
}
