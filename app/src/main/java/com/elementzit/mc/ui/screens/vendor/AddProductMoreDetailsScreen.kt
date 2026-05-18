package com.elementzit.mc.ui.screens.vendor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun AddProductMoreDetailsScreen(
    navController: NavController,
 /*   productName: String,
    productDescription: String,
    brandName: String,
    sku: String,
    productTags: List<String>,
    selectedCategory: String?,*/
) {

    var productSize by remember { mutableStateOf("") }
    var productColor by remember { mutableStateOf("") }
    var productMaterial by remember { mutableStateOf("") }
    var productWeight by remember { mutableStateOf("") }
    var productDimension by remember { mutableStateOf("") }

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
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF5F5F5),
                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
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
                FormLabel("Size")
                Spacer(modifier = Modifier.height(12.dp))
                RoundedTextField(
                    value = productSize,
                    onValueChange = { productSize = it },
                    placeholder = "Enter Size"
                )

                Spacer(modifier = Modifier.height(8.dp))

                FormLabel("Color")
                Spacer(modifier = Modifier.height(12.dp))
                RoundedTextField(
                    value = productColor,
                    onValueChange = { productColor = it },
                    placeholder = "Enter color"
                )

                Spacer(modifier = Modifier.height(8.dp))

                FormLabel("Material")
                Spacer(modifier = Modifier.height(12.dp))
                RoundedTextField(
                    value = productMaterial,
                    onValueChange = { productMaterial = it },
                    placeholder = "Enter Material"
                )

                Spacer(modifier = Modifier.height(8.dp))

                FormLabel("Weight")
                Spacer(modifier = Modifier.height(12.dp))
                RoundedTextField(
                    value = productWeight,
                    onValueChange = { productWeight = it },
                    placeholder = "Enter Weight"
                )

                Spacer(modifier = Modifier.height(8.dp))

                FormLabel("Dimensions")
                Spacer(modifier = Modifier.height(12.dp))
                RoundedTextField(
                    value = productDimension,
                    onValueChange = { productDimension = it },
                    placeholder = "Enter Dimensions"
                )

                Spacer(modifier = Modifier.height(8.dp))


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
                            //val tagsString = productTags.joinToString(",")
                            navController.navigate("add_product_price_qty")
                            /*navController.navigate(
                                "add_product_pricing_image/" +
                                        "${productName}/" +
                                        "${productDescription}/" +
                                        "${brandName}/" +
                                        "${sku}/" +
                                        "${tagsString}/" +
                                        "${selectedCategory}/" +
                                        "${productSize}/" +
                                        "${productColor}/" +
                                        "${productMaterial}/" +
                                        "${productWeight}/" +
                                        "${productDimension}"
                            )*/
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
}
