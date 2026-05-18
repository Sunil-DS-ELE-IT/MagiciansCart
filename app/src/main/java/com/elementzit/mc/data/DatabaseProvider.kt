package com.elementzit.mc.data

import android.content.Context
import com.elementzit.mc.data.local.MarketplaceDatabase
import com.elementzit.mc.data.repository.AuthRepository

object DatabaseProvider {
    private var database: MarketplaceDatabase? = null
    private var authRepository: AuthRepository? = null

    private fun getDatabase(context: Context): MarketplaceDatabase {
        return database ?: synchronized(this) {
            val instance = MarketplaceDatabase.getDatabase(context)
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
