package com.elementzit.mc.di

import android.content.Context
import androidx.room.Room
import com.elementzit.mc.data.local.MarketplaceDatabase
import com.elementzit.mc.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that provides database-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provides a singleton instance of the [MarketplaceDatabase].
     */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MarketplaceDatabase {
        return Room.databaseBuilder(
            context,
            MarketplaceDatabase::class.java,
            "marketplace_db"
        ).fallbackToDestructiveMigration().build()
    }

    /**
     * Provides an instance of [UserDao].
     */
    @Provides
    fun provideUserDao(database: MarketplaceDatabase): UserDao {
        return database.userDao()
    }
}
