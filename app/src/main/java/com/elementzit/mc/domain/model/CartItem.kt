package com.elementzit.mc.domain.model

data class CartItem(
    val product: Product,
    val quantity: Int = 1
)
