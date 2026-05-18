package com.elementzit.mc.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.elementzit.mc.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerProductViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    init {
        fetchAllProducts()
    }

    private fun fetchAllProducts() {
        viewModelScope.launch {
            firestore.collection("products")
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        _products.value = emptyList()
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        val productList = snapshot.toObjects(Product::class.java)
                        _products.value = productList
                    }
                }
        }
    }
}
