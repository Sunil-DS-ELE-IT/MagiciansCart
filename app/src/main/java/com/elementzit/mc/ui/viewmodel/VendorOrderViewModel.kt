package com.elementzit.mc.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elementzit.mc.domain.model.Order
import com.elementzit.mc.domain.repository.AuthRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VendorOrderViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedFilter = MutableStateFlow("Pending")
    val selectedFilter: StateFlow<String> = _selectedFilter

    val filteredOrders = combine(_orders, _searchQuery, _selectedFilter) { orders, query, filter ->
        orders.filter { order ->
            (query.isEmpty() || order.productName.contains(query, ignoreCase = true) || order.orderId.contains(query, ignoreCase = true)) &&
            (filter == "All" || order.status == filter)
        }
    }

    init {
        fetchOrders()
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onFilterChange(filter: String) {
        _selectedFilter.value = filter
    }

    private fun fetchOrders() {
        val uid = authRepository.getCurrentUserId() ?: return
        Log.d("VendorOrderViewModel", "Fetching orders for vendor: $uid")
        firestore.collection("orders")
            .whereEqualTo("vendorId", uid)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("VendorOrderViewModel", "Error fetching orders", e)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val orderList = snapshot.toObjects(Order::class.java)
                    Log.d("VendorOrderViewModel", "Fetched ${orderList.size} orders")
                    _orders.value = orderList
                }
            }
    }

    fun updateOrderStatus(orderId: String, newStatus: String) {
        viewModelScope.launch {
            firestore.collection("orders").document(orderId)
                .update("status", newStatus)
                .addOnSuccessListener {
                    Log.d("VendorOrderViewModel", "Order $orderId status updated to $newStatus")
                }
                .addOnFailureListener { e ->
                    Log.e("VendorOrderViewModel", "Error updating order status", e)
                }
        }
    }
}
