package com.elementzit.mc.domain.model

data class Order(
    val id: String = "",
    val orderId: String = "",
    val customerId: String = "",
    val vendorId: String = "",
    val customerName: String = "",
    val productName: String = "",
    val productImage: String = "",
    val price: Double = 0.0,
    val quantity: Int = 0,
    val status: String = "Pending", // Pending, Confirmed, In Production, Shipped, Delivered
    val type: String = "Standard", // Custom or Standard
    val timestamp: Long = System.currentTimeMillis()
)
