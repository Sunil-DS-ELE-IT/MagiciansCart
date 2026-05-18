package com.elementzit.mc.data.repository

import android.content.Context
import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.elementzit.mc.domain.model.Product
import com.elementzit.mc.domain.repository.AuthRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.io.IOException
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val authRepository: AuthRepository
) : ProductRepository {

    private val productsCollection = firestore.collection("products")
    private val storageRef = storage.reference

    override fun getProducts(): Flow<List<Product>> = callbackFlow {
        val listener = productsCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            snapshot?.let {
                val products = it.documents.mapNotNull { doc ->
                    doc.toObject(Product::class.java)?.copy(id = doc.id)
                }
                trySend(products)
            }
        }
        awaitClose { listener.remove() }
    }

    override suspend fun addProduct(product: Product, imageUris: List<String>) {
        val currentVendorId = authRepository.getCurrentUserId() ?: throw Exception("Not logged in")
        val productWithVendor = product.copy(vendorId = currentVendorId)
        val uploadedUrls = uploadImages(imageUris, product.id)
        val finalProduct = productWithVendor.copy(imageUrls = uploadedUrls)
        productsCollection.document(product.id).set(finalProduct).await()
    }

    override suspend fun updateProduct(product: Product, imageUris: List<String>) {
        val currentVendorId = authRepository.getCurrentUserId() ?: throw Exception("Not logged in")
        val productWithVendor = product.copy(vendorId = currentVendorId)
        
        val uploadedUrls = if (imageUris.any { it.startsWith("content://") || it.startsWith("file://") }) {
            imageUris.map { uri ->
                if (uri.startsWith("http")) uri
                else uploadImage(uri, product.id)
            }
        } else {
            imageUris
        }
        val updatedProduct = productWithVendor.copy(imageUrls = uploadedUrls)
        productsCollection.document(product.id).set(updatedProduct).await()
    }

    override suspend fun deleteProduct(productId: String) {
        productsCollection.document(productId).delete().await()
    }

    override fun getProductById(productId: String): Flow<Product?> = callbackFlow {
        val listener = productsCollection.document(productId).addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            snapshot?.let {
                val product = it.toObject(Product::class.java)?.copy(id = it.id)
                trySend(product)
            }
        }
        awaitClose { listener.remove() }
    }

    private suspend fun uploadImages(imageUris: List<String>, productId: String): List<String> {
        return imageUris.map { uri ->
            uploadImage(uri, productId)
        }
    }

    private suspend fun uploadImage(imageUriString: String, productId: String): String {
        val uri = Uri.parse(imageUriString)
        val contentResolver = context.contentResolver
        
        return try {
            val inputStream = contentResolver.openInputStream(uri)
                ?: throw IOException("Could not open input stream for $imageUriString")
            
            val fileName = UUID.randomUUID().toString()
            val ref = storageRef.child("product_images/$productId/$fileName")
            
            inputStream.use { stream ->
                ref.putStream(stream).await()
            }
            
            ref.downloadUrl.await().toString()
        } catch (e: Exception) {
            throw e
        }
    }
}
