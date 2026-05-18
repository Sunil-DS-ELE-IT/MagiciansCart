package com.elementzit.mc.ui.screens.vendor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.elementzit.mc.domain.model.Product
import com.elementzit.mc.ui.viewmodel.VendorProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    navController: NavController,
    product: Product,
    viewModel: VendorProductViewModel = hiltViewModel()
) {
    val profile by viewModel.vendorProfile.collectAsState()
    
    var productName by remember { mutableStateOf(product.name) }
    var productDescription by remember { mutableStateOf(product.description) }
    var productPrice by remember { mutableStateOf(product.price.toString()) }
    var productImageUrl by remember { mutableStateOf(product.imageUrl) }
    var productCategory by remember { mutableStateOf("") }

    var shopName by remember { mutableStateOf(profile?.get("shopName")?.toString() ?: "") }
    var latitude by remember { mutableStateOf(profile?.get("latitude")?.toString() ?: "") }
    var longitude by remember { mutableStateOf(profile?.get("longitude")?.toString() ?: "") }
    var expanded by remember { mutableStateOf(false) }

    val categories = listOf("Fashion", "Grocery", "Electronics", "Footwear", "Beauty")

    LaunchedEffect(profile) {
        profile?.let {
            if (shopName.isEmpty()) shopName = it["shopName"]?.toString() ?: ""
            if (latitude.isEmpty()) latitude = it["latitude"]?.toString() ?: ""
            if (longitude.isEmpty()) longitude = it["longitude"]?.toString() ?: ""
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val addUpdateStatus by viewModel.addUpdateStatus.collectAsState()

    LaunchedEffect(addUpdateStatus) {
        addUpdateStatus?.let {
            if (it) {
                snackbarHostState.showSnackbar("Product updated successfully!")
                navController.popBackStack()
            } else {
                snackbarHostState.showSnackbar("Failed to update product.")
            }
            viewModel.resetAddUpdateStatus()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Edit Product & Shop Info") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(value = productName, onValueChange = { productName = it }, label = { Text("Product Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = productDescription, onValueChange = { productDescription = it }, label = { Text("Product Description") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = productPrice, onValueChange = { productPrice = it }, label = { Text("Price") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = productImageUrl, onValueChange = { productImageUrl = it }, label = { Text("Product Image URL") }, modifier = Modifier.fillMaxWidth())
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = productCategory,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                productCategory = selectionOption
                                expanded = false
                            }
                        )
                    }
                }
            }
            OutlinedTextField(value = shopName, onValueChange = { shopName = it }, label = { Text("Shop Name") }, modifier = Modifier.fillMaxWidth())
            
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = latitude, onValueChange = { latitude = it }, label = { Text("Latitude") }, modifier = Modifier.weight(1f))
                OutlinedTextField(value = longitude, onValueChange = { longitude = it }, label = { Text("Longitude") }, modifier = Modifier.weight(1f))
            }

            Button(
                onClick = {
                    viewModel.updateVendorProfile(shopName, "", latitude.toDoubleOrNull() ?: 0.0, longitude.toDoubleOrNull() ?: 0.0)
                    val updatedProduct = product.copy(
                        name = productName,
                        description = productDescription,
                        price = productPrice.toDoubleOrNull() ?: 0.0,
                        imageUrl = productImageUrl,
                        category = productCategory
                    )
                    viewModel.updateProduct(updatedProduct)
                },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp).height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722))
            ) {
                Text("Update", fontSize = 18.sp)
            }
        }
    }
}
