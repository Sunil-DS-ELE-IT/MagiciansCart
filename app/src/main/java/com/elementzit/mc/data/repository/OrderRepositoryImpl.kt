package com.elementzit.mc.data.repository

import com.elementzit.mc.domain.model.Order
import com.elementzit.mc.domain.model.OrderStatus
import com.elementzit.mc.domain.repository.OrderRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : OrderRepository {

    private val ordersCollection = firestore.collection("orders")

    override suspend fun placeOrder(order: Order): Result<String> {
        return try {
            val docRef = if (order.orderId.isEmpty()) {
                ordersCollection.document()
            } else {
                ordersCollection.document(order.orderId)
            }
            val orderWithId = order.copy(orderId = docRef.id)
            docRef.set(orderWithId).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getOrdersForVendor(vendorId: String): Flow<List<Order>> = callbackFlow {
        val listener = ordersCollection
            .whereEqualTo("vendorId", vendorId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                snapshot?.let {
                    val orders = it.toObjects(Order::class.java)
                        .sortedByDescending { order -> order.timestamp }
                    trySend(orders)
                }
            }
        awaitClose { listener.remove() }
    }

    override fun getOrdersForCustomer(customerId: String): Flow<List<Order>> = callbackFlow {
        val listener = ordersCollection
            .whereEqualTo("customerId", customerId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                snapshot?.let {
                    val orders = it.toObjects(Order::class.java)
                        .sortedByDescending { order -> order.timestamp }
                    trySend(orders)
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun updateOrderStatus(
        orderId: String,
        status: OrderStatus,
        expectedDeliveryDate: String?
    ): Result<Unit> {
        return try {
            val updates = mutableMapOf<String, Any>("status" to status.name)
            expectedDeliveryDate?.let { updates["expectedDeliveryDate"] = it }
            
            ordersCollection.document(orderId).update(updates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getOrderById(orderId: String): Flow<Order?> = callbackFlow {
        val listener = ordersCollection.document(orderId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                snapshot?.let {
                    trySend(it.toObject(Order::class.java))
                }
            }
        awaitClose { listener.remove() }
    }
}
