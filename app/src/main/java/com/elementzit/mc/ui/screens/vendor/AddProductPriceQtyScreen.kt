package com.elementzit.mc.ui.screens.vendor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Upload
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
fun AddProductPriceQtyScreen(
    navController: NavController,
){
    var productPrice by remember { mutableStateOf("") }
    var productPriceDiscount by remember { mutableStateOf("") }
    var productQuantityAvailable by remember { mutableStateOf("") }
    var productMinQuantity by remember { mutableStateOf("") }
    var showImageUpload by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val orangeColor = Color(0xFFFF9800)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White // Main background surface
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFF9F9F9)).padding(bottom = 50.dp)
        ) {
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
                            color = orangeColor,
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .weight(1f)
                    .verticalScroll(scrollState),
            ) {
                if (!showImageUpload) {
                    // Price + Discount
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            FormLabel("Price *")
                            Spacer(modifier = Modifier.height(12.dp))
                            RoundedTextField(
                                value = productPrice,
                                onValueChange = {
                                    productPrice = it
                                },
                                placeholder = "₹ 0"
                            )
                        }

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            FormLabel("Discount Price")
                            Spacer(modifier = Modifier.height(12.dp))
                            RoundedTextField(
                                value = productPriceDiscount,
                                onValueChange = {
                                    productPriceDiscount = it
                                },
                                placeholder = "₹ 0"
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    FormLabel("Quantity Available *")
                    Spacer(modifier = Modifier.height(12.dp))
                    RoundedTextField(
                        value = productQuantityAvailable,
                        onValueChange = { productQuantityAvailable = it },
                        placeholder = "0"
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    FormLabel("Minimum Order Quantity")
                    Spacer(modifier = Modifier.height(12.dp))
                    RoundedTextField(
                        value = productMinQuantity,
                        onValueChange = { productMinQuantity = it },
                        placeholder = "1"
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp)
                            .border(
                                width = 2.dp,
                                color = Color.LightGray,
                                shape = RoundedCornerShape(24.dp)
                            )
                            .background(Color.Transparent, RoundedCornerShape(24.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(vertical = 40.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = orangeColor,
                                modifier = Modifier.size(80.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Upload,
                                        contentDescription = "Upload Icon",
                                        tint = Color.White,
                                        modifier = Modifier.size(40.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Upload Product Images",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color(0xFF374151)
                            )
                            Text(
                                text = "Drag & drop or click to browse",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
            
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
                        if (showImageUpload) {
                            showImageUpload = false
                        } else {
                            navController.popBackStack()
                        }
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
                       if (!showImageUpload) {
                           showImageUpload = true
                       } else {
                           // Submit flow
                       }
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
            }
        }
    }
}
