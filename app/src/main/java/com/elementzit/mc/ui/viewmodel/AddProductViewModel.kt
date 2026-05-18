package com.elementzit.mc.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elementzit.mc.data.repository.ProductRepository
import com.elementzit.mc.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class ProductUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val message: String? = null
)

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    private val _selectedImageUris = MutableStateFlow<List<String>>(emptyList())
    val selectedImageUris: StateFlow<List<String>> = _selectedImageUris.asStateFlow()

    fun onImagesSelected(uris: List<String>) {
        _selectedImageUris.value = _selectedImageUris.value + uris
    }

    fun removeImage(uri: String) {
        _selectedImageUris.value = _selectedImageUris.value.filter { it != uri }
    }

    fun saveProduct(product: Product, isUpdate: Boolean) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, isSuccess = false)
            try {
                val productId = if (isUpdate && product.id.isNotEmpty()) product.id else UUID.randomUUID().toString()
                val finalProduct = product.copy(id = productId)
                
                if (isUpdate) {
                    repository.updateProduct(finalProduct, _selectedImageUris.value)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = true,
                        message = "Product updated successfully"
                    )
                } else {
                    repository.addProduct(finalProduct, _selectedImageUris.value)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = true,
                        message = "Product added successfully"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.localizedMessage ?: "An unexpected error occurred"
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = ProductUiState()
    }
}
