package com.elementzit.mc.data.repository

import com.elementzit.mc.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(): Flow<List<Product>>
    suspend fun addProduct(product: Product, imageUri: String?)
    suspend fun updateProduct(product: Product, imageUri: String?)
    suspend fun deleteProduct(productId: String)
    fun getProductById(productId: String): Flow<Product?>
}
