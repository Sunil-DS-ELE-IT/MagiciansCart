package com.elementzit.mc.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elementzit.mc.domain.model.Address
import com.elementzit.mc.domain.model.CartItem
import com.elementzit.mc.domain.model.Order
import com.elementzit.mc.domain.model.OrderItem
import com.elementzit.mc.domain.model.OrderStatus
import com.elementzit.mc.domain.repository.AuthRepository
import com.elementzit.mc.domain.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _checkoutState = MutableStateFlow<CheckoutState>(CheckoutState.Idle)
    val checkoutState: StateFlow<CheckoutState> = _checkoutState.asStateFlow()

    fun placeOrder(
        cartItems: List<CartItem>,
        address: Address,
        paymentMethod: String,
        totalAmount: Double
    ) {
        viewModelScope.launch {
            _checkoutState.value = CheckoutState.Loading
            
            val customerId = authRepository.getCurrentUserId() ?: run {
                _checkoutState.value = CheckoutState.Error("User not logged in")
                return@launch
            }

            // Assuming all items in cart are from the same vendor for simplicity, 
            // or we could split orders by vendor. 
            // Here we just take the vendorId from the first item.
            val vendorId = cartItems.firstOrNull()?.product?.vendorId ?: ""

            val orderItems = cartItems.map { item ->
                OrderItem(
                    productId = item.product.id,
                    productName = item.product.name,
                    quantity = item.quantity,
                    price = item.product.price,
                    imageUrl = item.product.imageUrls.firstOrNull() ?: item.product.imageUrl
                )
            }

            val order = Order(
                customerId = customerId,
                vendorId = vendorId,
                customerAddress = address,
                customerPhoneNumber = address.phone,
                paymentMethod = paymentMethod,
                paymentId = "PAY_${System.currentTimeMillis()}", // Mock payment ID
                totalAmount = totalAmount,
                orderSummary = orderItems,
                status = OrderStatus.PENDING
            )

            orderRepository.placeOrder(order).fold(
                onSuccess = { orderId ->
                    _checkoutState.value = CheckoutState.Success(orderId)
                },
                onFailure = { error ->
                    _checkoutState.value = CheckoutState.Error(error.message ?: "Failed to place order")
                }
            )
        }
    }

    fun resetState() {
        _checkoutState.value = CheckoutState.Idle
    }
}

sealed class CheckoutState {
    object Idle : CheckoutState()
    object Loading : CheckoutState()
    data class Success(val orderId: String) : CheckoutState()
    data class Error(val message: String) : CheckoutState()
}
