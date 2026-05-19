package com.elementzit.mc.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.elementzit.mc.domain.model.Product
import com.elementzit.mc.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VendorProductViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _allProducts = MutableStateFlow<List<Product>>(emptyList())
    val allProducts: StateFlow<List<Product>> = _allProducts
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val products = combine(_allProducts, _searchQuery) { products, query ->
        if (query.isEmpty()) products else products.filter { it.name.contains(query, ignoreCase = true) }
    }

    private val _addUpdateStatus = MutableStateFlow<Boolean?>(null)
    val addUpdateStatus: StateFlow<Boolean?> = _addUpdateStatus

    private val _vendorStatus = MutableStateFlow("LOADING")
    val vendorStatus: StateFlow<String> = _vendorStatus

    private val _vendorProfile = MutableStateFlow<Map<String, Any>?>(null)
    val vendorProfile: StateFlow<Map<String, Any>?> = _vendorProfile

    init {
        loadVendorData()
    }

    private fun loadVendorData() {
        val uid = getCurrentUserId()
        val user = FirebaseAuth.getInstance().currentUser

        if (user == null) {
            Log.e("Firestore", "User not logged in")
            return
        }
        Log.d("VendorProductViewModel", "init: Checking user ID: $uid")
        if (uid != null) {
            fetchProducts()
            fetchVendorStatus()
            fetchVendorProfile()
        } else {
            // Retry logic or wait for authentication
            Log.e("VendorProductViewModel", "User not logged in, cannot fetch data. Retrying...")
            // If you are using a navigation pattern where login precedes this, 
            // the ViewModel might be initialized before the user is fully logged in.
            // You may need a listener or a more reactive approach here.
        }
    }

    private fun fetchVendorStatus() {
        val uid = getCurrentUserId() ?: return
        firestore.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                _vendorStatus.value = doc.getString("status") ?: "PENDING"
            }
            .addOnFailureListener { 
                Log.e("VendorProductViewModel", "Failed to fetch status", it)
                _vendorStatus.value = "PENDING" 
            }
    }

    fun fetchVendorProfile() {
        val uid = getCurrentUserId() ?: return
        firestore.collection("vendors").document(uid).addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.e("VendorProductViewModel", "Profile listen failed", e)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                _vendorProfile.value = snapshot.data
            }
        }
    }

    fun onSearchQueryChange(newQuery: String) { _searchQuery.value = newQuery }

    fun getCurrentUserId(): String? = authRepository.getCurrentUserId()

    fun fetchProducts() {
        val uid = getCurrentUserId()
        if (uid == null) {
            Log.e("VendorProductViewModel", "fetchProducts: UID is null")
            return
        }
        Log.d("VendorProductViewModel", "Fetching products for vendor: $uid")
        firestore.collection("products")
            .whereEqualTo("vendorId", uid)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("VendorProductViewModel", "Products listen failed", e)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val productList = snapshot.toObjects(Product::class.java)
                    Log.d("VendorProductViewModel", "Successfully fetched ${productList.size} products")
                    _allProducts.value = productList
                }
            }
    }

    fun updateVendorProfile(shopName: String, address: String, lat: Double, lng: Double) {
        val uid = getCurrentUserId() ?: return
        val vendorData = mapOf(
            "shopName" to shopName,
            "address" to address,
            "latitude" to lat,
            "longitude" to lng
        )
        firestore.collection("vendors").document(uid).set(vendorData)
    }

    fun addProduct(product: Product) {
        val uid = getCurrentUserId() ?: return
        viewModelScope.launch {
            val docRef = firestore.collection("products").document()
            val productWithVendor = product.copy(id = docRef.id, vendorId = uid)
            docRef.set(productWithVendor)
                .addOnSuccessListener { _addUpdateStatus.value = true }
                .addOnFailureListener { _addUpdateStatus.value = false }
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            firestore.collection("products").document(product.id)
                .set(product)
                .addOnSuccessListener { _addUpdateStatus.value = true }
                .addOnFailureListener { _addUpdateStatus.value = false }
        }
    }

    fun resetAddUpdateStatus() { _addUpdateStatus.value = null }

    fun deleteProduct(productId: String) {
        firestore.collection("products").document(productId).delete()
    }
}
