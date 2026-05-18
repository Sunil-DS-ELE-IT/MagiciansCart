package com.elementzit.mc.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elementzit.mc.domain.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UiState<out T> {
    object Idle : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val uiState: StateFlow<UiState<String>> = _uiState.asStateFlow()

    fun saveProduct(product: Product, isUpdate: Boolean) {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val collection = firestore.collection("products")
                if (isUpdate && product.id.isNotEmpty()) {
                    collection.document(product.id).set(product).addOnSuccessListener {
                        _uiState.value = UiState.Success("Product updated successfully")
                    }.addOnFailureListener {
                        _uiState.value = UiState.Error(it.message ?: "Update failed")
                    }
                } else {
                    val docRef = collection.document()
                    val newProduct = product.copy(id = docRef.id)
                    docRef.set(newProduct).addOnSuccessListener {
                        _uiState.value = UiState.Success("Product added successfully")
                    }.addOnFailureListener {
                        _uiState.value = UiState.Error(it.message ?: "Add failed")
                    }
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
