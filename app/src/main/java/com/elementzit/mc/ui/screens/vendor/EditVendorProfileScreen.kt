package com.elementzit.mc.ui.screens.vendor

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.elementzit.mc.ui.viewmodel.VendorProductViewModel

@Composable
fun EditVendorProfileScreen(
    navController: NavController,
    viewModel: VendorProductViewModel = hiltViewModel()
) {
    val profile by viewModel.vendorProfile.collectAsState()
    
    var shopName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }

    LaunchedEffect(profile) {
        profile?.let {
            shopName = it["shopName"]?.toString() ?: ""
            address = it["address"]?.toString() ?: ""
            latitude = it["latitude"]?.toString() ?: ""
            longitude = it["longitude"]?.toString() ?: ""
        }
    }

    Column(modifier = Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Edit Profile", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(value = shopName, onValueChange = { shopName = it }, label = { Text("Shop Name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Address") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = latitude, onValueChange = { latitude = it }, label = { Text("Latitude") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = longitude, onValueChange = { longitude = it }, label = { Text("Longitude") }, modifier = Modifier.fillMaxWidth())
        
        Button(
            onClick = {
                viewModel.updateVendorProfile(shopName, address, latitude.toDoubleOrNull() ?: 0.0, longitude.toDoubleOrNull() ?: 0.0)
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Save Profile")
        }
    }
}
