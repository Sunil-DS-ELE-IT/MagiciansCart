package com.elementzit.mc.ui.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.elementzit.mc.domain.model.Vendor
import com.elementzit.mc.domain.usecase.GetNearbyVendorsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class VendorViewModel @Inject constructor(
    private val getNearbyVendorsUseCase: GetNearbyVendorsUseCase,
    private val fusedLocationClient: FusedLocationProviderClient
) : ViewModel() {

    private val _uiState = MutableStateFlow<VendorUiState>(VendorUiState.Loading)
    val uiState: StateFlow<VendorUiState> = _uiState

    @SuppressLint("MissingPermission")
    fun fetchNearbyVendors() {
        viewModelScope.launch {
            _uiState.value = VendorUiState.Loading
            try {
                val location = fusedLocationClient.lastLocation.await()
                if (location != null) {
                    val vendors = getNearbyVendorsUseCase(location.latitude, location.longitude)
                    _uiState.value = VendorUiState.Success(vendors)
                } else {
                    _uiState.value = VendorUiState.Error("Unable to get current location")
                }
            } catch (e: Exception) {
                _uiState.value = VendorUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class VendorUiState {
    object Loading : VendorUiState()
    data class Success(val vendors: List<Vendor>) : VendorUiState()
    data class Error(val message: String) : VendorUiState()
}
