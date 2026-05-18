package com.elementzit.mc.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.elementzit.mc.data.local.dao.UserDao
import com.elementzit.mc.data.local.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MarketplaceDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        private const val DB_NAME = "marketplace_db"

        @Volatile
        private var INSTANCE: MarketplaceDatabase? = null

        fun getDatabase(context: Context): MarketplaceDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MarketplaceDatabase::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration() // Handles schema changes by wiping the DB
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}