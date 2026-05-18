package com.elementzit.mc.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.elementzit.mc.domain.model.Address
import com.elementzit.mc.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _addresses = MutableStateFlow<List<Address>>(emptyList())
    val addresses: StateFlow<List<Address>> = _addresses

    private val _selectedAddress = MutableStateFlow<Address?>(null)
    val selectedAddress: StateFlow<Address?> = _selectedAddress

    init {
        fetchAddresses()
    }

    fun fetchAddresses() {
        val uid = authRepository.getCurrentUserId() ?: return
        viewModelScope.launch {
            firestore.collection("users").document(uid).collection("addresses")
                .addSnapshotListener { snapshot, _ ->
                    if (snapshot != null) {
                        val list = snapshot.toObjects(Address::class.java)
                        _addresses.value = list
                        if (_selectedAddress.value == null && list.isNotEmpty()) {
                            _selectedAddress.value = list.find { it.isDefault } ?: list.first()
                        }
                    }
                }
        }
    }

    fun addAddress(address: Address, onSuccess: () -> Unit) {
        val uid = authRepository.getCurrentUserId() ?: return
        viewModelScope.launch {
            val docRef = firestore.collection("users").document(uid).collection("addresses").document()
            val newAddress = address.copy(id = docRef.id, userId = uid)
            docRef.set(newAddress).addOnSuccessListener {
                onSuccess()
            }
        }
    }

    fun selectAddress(address: Address) {
        _selectedAddress.value = address
    }
}
