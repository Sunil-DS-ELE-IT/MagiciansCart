package com.elementzit.mc.di

import com.elementzit.mc.data.repository.AuthRepositoryImpl
import com.elementzit.mc.data.repository.OrderRepositoryImpl
import com.elementzit.mc.data.repository.ProductRepository
import com.elementzit.mc.data.repository.ProductRepositoryImpl
import com.elementzit.mc.data.repository.VendorRepositoryImpl
import com.elementzit.mc.domain.repository.AuthRepository
import com.elementzit.mc.domain.repository.OrderRepository
import com.elementzit.mc.domain.repository.VendorRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.elementzit.mc.data.local.dao.UserDao

/**
 * Hilt module that provides binding for repository interfaces.
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideProductRepository(productRepositoryImpl: ProductRepositoryImpl): ProductRepository {
        return productRepositoryImpl
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        userDao: UserDao,
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): AuthRepository {
        return AuthRepositoryImpl(userDao, firebaseAuth, firestore)
    }

    @Provides
    @Singleton
    fun provideVendorRepository(vendorRepositoryImpl: VendorRepositoryImpl): VendorRepository {
        return vendorRepositoryImpl
    }

    @Provides
    @Singleton
    fun provideOrderRepository(orderRepositoryImpl: OrderRepositoryImpl): OrderRepository {
        return orderRepositoryImpl
    }
}
