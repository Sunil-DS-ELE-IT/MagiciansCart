package com.elementzit.mc.domain.model

data class Product(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val brand: String = "",
    val sku: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val vendorId: String = "",
    val category: String = "",
    val quantity: Int = 0,
    val stock: Int = 0,
    val sales: Int = 0, // Added sales field
    val isVisible: Boolean = true, // Added visibility toggle
    val tags: List<String> = emptyList(),
    val rating: Double = 0.0,
    val imageUrls: List<String> = emptyList(),
    val duration: String = "",
    val expectedDeliveryDate: String = "",
    val shippingMethod: String = "",
    val deliveryNotes: String = ""
)
