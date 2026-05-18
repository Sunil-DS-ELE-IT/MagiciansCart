package com.elementzit.mc.ui.screens.vendor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.elementzit.mc.ui.components.FormLabel
import com.elementzit.mc.ui.components.RoundedTextField

@Composable
fun AddProductDetailsScreen(
    navController: NavController,
/*    productName: String,
    productDescription: String,
    brandName: String,
    sku: String,
    productTags: List<String>,*/
    selectedCategory: String?
) {

    var currentProductName by remember { mutableStateOf("") }
    var currentDescription by remember { mutableStateOf("") }
    var currentBrandName by remember { mutableStateOf("") }
    var currentSku by remember { mutableStateOf("") }
    var currentProductTags by remember { mutableStateOf(mutableListOf<String>()) }

    var newTagInput by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val orangeColor = Color(0xFFFF9800)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White // Main background surface
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFF9F9F9)).padding(bottom = 50.dp)
        ) { // Removed bottom padding from here
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .padding(top = 50.dp)
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFFF9800),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) { Text("✨", fontSize = 20.sp) }
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                "Magician's Cart",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text("Vendor Panel", fontSize = 12.sp, color = Color.Gray, maxLines = 1)
                        }
                    }
                    Icon(Icons.Default.Notifications, null, tint = Color.Gray)
                }
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = "", onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search products, orders...") },
                    leadingIcon = { Icon(Icons.Rounded.Search, null) },
                    shape = RoundedCornerShape(24.dp)
                )
                Spacer(Modifier.height(16.dp))
                Text("Welcome Back, Alwyn 👋", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                Text(
                    "Here's what's happening with your store today",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }

            // Scrollable Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState),
            ) {
                // Product Name
                FormLabel("Product Name *")
                Spacer(modifier = Modifier.height(12.dp))
                RoundedTextField(
                    value = currentProductName,
                    onValueChange = { currentProductName = it },
                    placeholder = "e.g., Handcrafted Wooden Bowl"
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Description
                FormLabel("Product Description *")
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = currentDescription, onValueChange = {currentDescription = it},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(75.dp),
                    placeholder = {
                        Text(
                            text = "Describe your product in detail...",
                            color = Color.Gray
                        )
                    },
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF5F5F5),
                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )

                // Brand + SKU
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        FormLabel("Brand Name")
                        Spacer(modifier = Modifier.height(12.dp))
                        RoundedTextField(
                            value = currentBrandName,
                            onValueChange = {
                                currentBrandName = it
                            },
                            placeholder = "Brand"
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        FormLabel("SKU")
                        Spacer(modifier = Modifier.height(12.dp))
                        RoundedTextField(
                            value = currentSku,
                            onValueChange = {
                                currentSku = it
                            },
                            placeholder = "SKU123"
                        )
                    }
                }

                // Tags
                FormLabel("Product Tags")
                Spacer(modifier = Modifier.height(16.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(currentProductTags) { tag ->
                        TagChip(tag) { tagToRemove ->
                            currentProductTags = currentProductTags.filter { it != tagToRemove }.toMutableList()
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RoundedTextField(
                        value = newTagInput,
                        onValueChange = { newTagInput = it },
                        placeholder = "Add a new tag",
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (newTagInput.isNotBlank() && !currentProductTags.contains(newTagInput.trim())) {
                                currentProductTags = (currentProductTags + newTagInput.trim()).toMutableList()
                                newTagInput = ""
                            }
                        },
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = orangeColor),
                        modifier = Modifier.height(50.dp)
                    ) {
                        Text("Add", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
                // Bottom Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 20.dp,
                            vertical = 20.dp
                        ).padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    Button(
                        onClick = {
                            navController.popBackStack()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF1F1F1)
                        )
                    ) {
                        Text(
                            text = "Back",
                            color = Color(0xFF374151),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = {
                            val tagsString = currentProductTags.joinToString(",")
                            navController.navigate("add_product_more_details")
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .shadow(
                                elevation = 12.dp,
                                shape = RoundedCornerShape(24.dp)
                            ),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = orangeColor
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Next",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "›",
                                fontSize = 20.sp
                            )
                        }
                    }
                }//
            }

        }
    }
}

@Composable
fun TagChip(
    text: String,
    onRemove: (String) -> Unit
) {

    Box(
        modifier = Modifier
            .background(
                color = Color(0xFFF3F4F6),
                shape = RoundedCornerShape(50.dp)
            )
            .padding(
                horizontal = 20.dp,
                vertical = 12.dp
            ),
        contentAlignment = Alignment.Center
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                color = Color(0xFF4B5563),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(
                onClick = { onRemove(text) },
                modifier = Modifier.size(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove tag",
                    tint = Color(0xFF4B5563)
                )
            }
        }
    }
}
