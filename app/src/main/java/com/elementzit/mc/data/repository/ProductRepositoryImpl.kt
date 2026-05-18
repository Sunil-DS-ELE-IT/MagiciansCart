package com.elementzit.mc.data.repository

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.elementzit.mc.domain.model.Product
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : ProductRepository {

    private val productsCollection = firestore.collection("products")
    private val imagesRef = storage.reference.child("product_images")

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

    override suspend fun addProduct(product: Product, imageUri: String?) {
        val imageUrl = imageUri?.let { uploadImage(it, product.id) }
        val productWithImage = product.copy(imageUrl = imageUrl ?: "")
        productsCollection.document(product.id).set(productWithImage).await()
    }

    override suspend fun updateProduct(product: Product, imageUri: String?) {
        val imageUrl = imageUri?.let { uploadImage(it, product.id) }
        val updatedProduct = if (imageUrl != null) {
            product.copy(imageUrl = imageUrl)
        } else {
            product // Keep existing image if no new URI is provided
        }
        productsCollection.document(product.id).set(updatedProduct).await()
    }

    override suspend fun deleteProduct(productId: String) {
        // Optionally delete image from storage as well
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

    private suspend fun uploadImage(imageUriString: String, productId: String): String {
        val uri = Uri.parse(imageUriString)
        val ref = imagesRef.child("$productId-${uri.lastPathSegment}")
        val uploadTask = ref.putFile(uri).await()
        return ref.downloadUrl.await().toString()
    }
}
