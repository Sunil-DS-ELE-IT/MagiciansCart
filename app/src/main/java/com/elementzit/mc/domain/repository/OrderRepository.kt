package com.elementzit.mc.domain.repository

import com.elementzit.mc.domain.model.Order
import com.elementzit.mc.domain.model.OrderStatus
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    suspend fun placeOrder(order: Order): Result<String>
    fun getOrdersForVendor(vendorId: String): Flow<List<Order>>
    fun getOrdersForCustomer(customerId: String): Flow<List<Order>>
    suspend fun updateOrderStatus(orderId: String, status: OrderStatus, expectedDeliveryDate: String? = null): Result<Unit>
    fun getOrderById(orderId: String): Flow<Order?>
}
