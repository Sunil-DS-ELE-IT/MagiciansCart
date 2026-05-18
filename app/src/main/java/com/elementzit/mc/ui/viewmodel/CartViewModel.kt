package com.elementzit.mc.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.elementzit.mc.domain.model.CartItem
import com.elementzit.mc.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor() : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    fun addToCart(product: Product) {
        _cartItems.update { currentItems ->
            val existingItem = currentItems.find { it.product.id == product.id }
            if (existingItem != null) {
                currentItems.map {
                    if (it.product.id == product.id) it.copy(quantity = it.quantity + 1) else it
                }
            } else {
                currentItems + CartItem(product = product)
            }
        }
    }

    fun removeFromCart(productId: String) {
        _cartItems.update { currentItems ->
            currentItems.filter { it.product.id != productId }
        }
    }

    fun updateQuantity(productId: String, quantity: Int) {
        if (quantity <= 0) {
            removeFromCart(productId)
            return
        }
        _cartItems.update { currentItems ->
            currentItems.map {
                if (it.product.id == productId) it.copy(quantity = quantity) else it
            }
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }
}
