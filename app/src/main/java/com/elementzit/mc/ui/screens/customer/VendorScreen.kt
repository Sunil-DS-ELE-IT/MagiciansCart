package com.elementzit.mc.ui.screens.customer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.elementzit.mc.domain.model.Vendor
import com.elementzit.mc.ui.viewmodel.VendorUiState
import com.elementzit.mc.ui.viewmodel.VendorViewModel

@Composable
fun VendorScreen(viewModel: VendorViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold { padding ->
        when (val state = uiState) {
            is VendorUiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            is VendorUiState.Success -> {
                LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
                    items(state.vendors) { vendor ->
                        VendorItem(vendor)
                    }
                }
            }
            is VendorUiState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(state.message) }
        }
    }
}

@Composable
fun VendorItem(vendor: Vendor) {
    Card(modifier = Modifier.padding(8.dp).fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(vendor.shopName, style = MaterialTheme.typography.titleLarge)
            Text(vendor.address)
            Text(vendor.distanceText, color = MaterialTheme.colorScheme.primary)
        }
    }
}
