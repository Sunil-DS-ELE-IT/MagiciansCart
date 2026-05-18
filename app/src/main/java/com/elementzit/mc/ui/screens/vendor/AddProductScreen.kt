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
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    navController: NavController,
    category: String? = null,
    viewModel: VendorProductViewModel = hiltViewModel()
) {
    val profile by viewModel.vendorProfile.collectAsState()

    var productName by remember { mutableStateOf("") }
    var productDescription by remember { mutableStateOf("") }
    var brandName by remember { mutableStateOf("") }
    var sku by remember { mutableStateOf("") }
    var productTags by remember { mutableStateOf("") } // Comma-separated tags

    var productCategory by remember { mutableStateOf(category ?: "") }

    val categories = listOf("Fashion", "Grocery", "Electronics", "Footwear", "Beauty", "Furniture", "Handmade", "Jewelry")
    var expanded by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Add Product Details") },
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
            OutlinedTextField(value = brandName, onValueChange = { brandName = it }, label = { Text("Brand Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = sku, onValueChange = { sku = it }, label = { Text("SKU") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = productTags, onValueChange = { productTags = it }, label = { Text("Product Tags (comma-separated)") }, modifier = Modifier.fillMaxWidth())

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
            
            Button(
                onClick = {
                    val encodedProductName = URLEncoder.encode(productName, StandardCharsets.UTF_8.toString())
                    val encodedProductDescription = URLEncoder.encode(productDescription, StandardCharsets.UTF_8.toString())
                    val encodedBrandName = URLEncoder.encode(brandName, StandardCharsets.UTF_8.toString())
                    val encodedSku = URLEncoder.encode(sku, StandardCharsets.UTF_8.toString())
                    val encodedProductTags = URLEncoder.encode(productTags, StandardCharsets.UTF_8.toString())
                    val encodedProductCategory = URLEncoder.encode(productCategory, StandardCharsets.UTF_8.toString())


                    navController.navigate("add_product_details/" +
                            "\${encodedProductName}/" +
                            "\${encodedProductDescription}/" +
                            "\${encodedBrandName}/" +
                            "\${encodedSku}/" +
                            "\${encodedProductTags}/" +
                            "\${encodedProductCategory}")
                },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp).height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722))
            ) {
                Text("Next", fontSize = 18.sp)
            }
        }
    }
}
