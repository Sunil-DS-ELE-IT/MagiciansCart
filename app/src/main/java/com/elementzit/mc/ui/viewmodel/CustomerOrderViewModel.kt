package com.elementzit.mc.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elementzit.mc.domain.model.Order
import com.elementzit.mc.domain.model.OrderStatus
import com.elementzit.mc.domain.repository.AuthRepository
import com.elementzit.mc.domain.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerOrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchOrders()
    }

    private fun fetchOrders() {
        val customerId = authRepository.getCurrentUserId() ?: return
        viewModelScope.launch {
            _isLoading.value = true
            orderRepository.getOrdersForCustomer(customerId).collectLatest { orderList ->
                // Filter for PENDING and CONFIRMED orders as per user request
                val filteredOrders = orderList.filter { 
                    it.status == OrderStatus.PENDING || it.status == OrderStatus.CONFIRMED 
                }
                _orders.value = filteredOrders
                _isLoading.value = false
            }
        }
    }
}
