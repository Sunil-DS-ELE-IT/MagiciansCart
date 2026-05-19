package com.elementzit.mc.domain.model

data class Order(
    val orderId: String = "",
    val customerId: String = "",
    val vendorId: String = "",
    val customerAddress: Address? = null,
    val customerPhoneNumber: String = "",
    val paymentId: String = "",
    val paymentMethod: String = "",
    val totalAmount: Double = 0.0,
    val orderSummary: List<OrderItem> = emptyList(),
    val expectedDeliveryDate: String = "",
    val status: OrderStatus = OrderStatus.PENDING,
    val timestamp: Long = System.currentTimeMillis()
)

data class OrderItem(
    val productId: String = "",
    val productName: String = "",
    val quantity: Int = 0,
    val price: Double = 0.0,
    val imageUrl: String = ""
)

enum class OrderStatus {
    PENDING,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    CANCELLED
}
