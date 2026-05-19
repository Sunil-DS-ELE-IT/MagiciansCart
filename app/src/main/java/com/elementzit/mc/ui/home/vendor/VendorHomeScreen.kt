package com.elementzit.mc.ui.home.vendor

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.elementzit.mc.domain.model.Product
import com.elementzit.mc.ui.viewmodel.VendorProductViewModel

@Composable
fun VendorHomeScreen(
    navController: NavController,
    onLogout: () -> Unit,
    viewModel: VendorProductViewModel = hiltViewModel()
) {
    val products by viewModel.products.collectAsState(initial = emptyList()) // Assuming products come from ViewModel

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF9F9F9))) {
        // Sticky Header Section
        Column(modifier = Modifier.padding(16.dp).padding(top = 50.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFFFF9800), modifier = Modifier.size(40.dp)) {
                        Box(contentAlignment = Alignment.Center) { Text("✨", fontSize = 20.sp) }
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Magician's Cart", fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text("Vendor Panel", fontSize = 12.sp, color = Color.Gray, maxLines = 1)
                    }
                }
                Icon(Icons.Default.Notifications, null, tint = Color.Gray)
            }
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = viewModel.searchQuery.collectAsState().value, onValueChange = { viewModel.onSearchQueryChange(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search products, orders...") },
                leadingIcon = { Icon(Icons.Rounded.Search, null) },
                shape = RoundedCornerShape(24.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text("Welcome Back, Alwyn 👋", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
            Text("Here's what's happening with your store today", color = Color.Gray, fontSize = 14.sp)
        }

        // Scrollable Content
        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Stats Grid
            item {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        StatsCard("TOTAL PRODUCTS", "48", "+12%", Color(0xFF4CAF50), Icons.Rounded.Inventory, Modifier.weight(1f))
                        StatsCard("ACTIVE LISTINGS", "42", "+8%", Color(0xFF4CAF50), Icons.Rounded.CheckBox, Modifier.weight(1f))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        StatsCard("OUT OF STOCK", "6", "-3", Color(0xFFF44336), Icons.Rounded.Warning, Modifier.weight(1f))
                        StatsCard("TOTAL ORDERS", "234", "+18%", Color(0xFFFF9800), Icons.Rounded.ShoppingCart, Modifier.weight(1f))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        StatsCard("REVENUE", "₹45,890", "+24%", Color(0xFF4CAF50), Icons.Rounded.AttachMoney, Modifier.weight(1f))
                        StatsCard("PENDING DELIVERIES", "12", "-5", Color(0xFF607D8B), Icons.Rounded.LocalShipping, Modifier.weight(1f))
                    }
                }
            }

            // Quick Actions
            item {
                Text("Quick Actions", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(vertical = 8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    ActionCard("Add Product", "Create new listing", Color(0xFFFF6B00), Icons.Default.Add, Modifier.weight(1f)) { navController.navigate("nav_category") } // Navigate to AddProduct screen
                    ActionCard("View Orders", "Manage orders", Color(0xFFF79E1B), Icons.Default.ShoppingCart, Modifier.weight(1f)) { /* TODO: Implement View Orders navigation */ }
                }
            }

            // Your Products Header
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Your Products", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("View All", color = Color(0xFFFF6B00), fontWeight = FontWeight.Bold)
                }
            }

            // Product List
            items(products) { product ->
                DetailedProductItem(
                    product = product, 
                    navController = navController,
                    onVisibilityToggle = { viewModel.updateProduct(product.copy(isVisible = !product.isVisible)) },
                    onDelete = { viewModel.deleteProduct(product.id) },
                    onCopy = { viewModel.addProduct(product.copy(id = "", name = "${product.name} (Copy)")) }
                )
            }

            //Restock
            item {
                LowStockAlertCardd(
                      onRestockClick = {
                          // Navigate to inventory screen
                          navController.navigate("restock_now")
                      }
                  )
            }

        }
    }
}

@Composable
fun StatsCard(title: String, value: String, trend: String, trendColor: Color, icon: ImageVector, modifier: Modifier) {
    Card(modifier = modifier.height(100.dp), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Icon(icon, null, modifier = Modifier.size(20.dp), tint = Color(0xFFFF9800))
            Spacer(Modifier.height(4.dp))
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
            Text(title, fontSize = 9.sp, color = Color.Gray, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
fun ActionCard(title: String, subtitle: String, color: Color, icon: ImageVector, modifier: Modifier, onClick: () -> Unit) {
    Card(modifier = modifier.height(120.dp).clickable(onClick = onClick), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = color)) {
        Column(modifier = Modifier.padding(16.dp))
        {
            Icon(icon, null, tint = Color.White, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, color = Color.White, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(subtitle, color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedProductItem(
    product: Product, 
    navController: NavController,
    onVisibilityToggle: () -> Unit,
    onDelete: () -> Unit,
    onCopy: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp), 
        shape = RoundedCornerShape(28.dp), 
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, Color(0xFFF3F4F6))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                // Product Image with status overlay
                Box(modifier = Modifier.size(85.dp)) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFFFF7ED)
                    ) {
                        AsyncImage(
                            model = product.imageUrls.firstOrNull() ?: product.imageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                    if (product.stock > 0) {
                        Surface(
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.TopEnd)
                                .offset(x = 4.dp, y = (-4).dp),
                            shape = CircleShape,
                            color = Color(0xFF10B981),
                            border = BorderStroke(2.dp, Color.White)
                        ) {
                            Icon(
                                Icons.Default.Check, 
                                null, 
                                tint = Color.White, 
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(), 
                        horizontalArrangement = Arrangement.SpaceBetween, 
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                product.name, 
                                fontWeight = FontWeight.Bold, 
                                fontSize = 18.sp, 
                                color = Color(0xFF1F2937),
                                maxLines = 1, 
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                product.category.uppercase(), 
                                fontSize = 13.sp, 
                                color = Color(0xFF6B7280), 
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        
                        Box {
                            IconButton(onClick = { showMenu = true }, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.MoreVert, contentDescription = null, tint = Color(0xFF9CA3AF))
                            }
                            DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                                DropdownMenuItem(
                                    text = { Text("Delete Product", color = Color.Red) },
                                    onClick = { 
                                        onDelete()
                                        showMenu = false
                                    },
                                    leadingIcon = { Icon(Icons.Outlined.Delete, null, tint = Color.Red) }
                                )
                            }
                        }
                    }
                    
                    // Tags
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp), 
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        product.tags.take(2).forEach { tag ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFFFF7ED),
                                border = BorderStroke(1.dp, Color(0xFFFFE4D1))
                            ) {
                                Text(
                                    text = tag,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    color = Color(0xFFD97706),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "₹${product.price.toInt()}", 
                            fontWeight = FontWeight.ExtraBold, 
                            fontSize = 22.sp,
                            color = Color(0xFF111827)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("•", color = Color(0xFFD1D5DB), fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "${product.stock} left", 
                            color = Color(0xFF059669), 
                            fontWeight = FontWeight.Bold, 
                            fontSize = 16.sp
                        )
                        
                        Spacer(modifier = Modifier.weight(1f))
                        
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFFFEFCE8)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Star, null, tint = Color(0xFFF59E0B), modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    "${product.rating}", 
                                    color = Color(0xFFCA8A04), 
                                    fontSize = 14.sp, 
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp), 
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { navController.navigate("edit_product/${product.id}") }, 
                    modifier = Modifier.weight(1f).height(54.dp), 
                    shape = RoundedCornerShape(27.dp), 
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8C00))
                ) {
                    Icon(Icons.Outlined.Edit, null, modifier = Modifier.size(20.dp), tint = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Text("Edit", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                
                Surface(
                    onClick = onVisibilityToggle,
                    shape = CircleShape,
                    color = Color(0xFFF9FAFB),
                    border = BorderStroke(1.dp, Color(0xFFF3F4F6)),
                    modifier = Modifier.size(54.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            if (product.isVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff, 
                            null, 
                            tint = Color(0xFF4B5563),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                
                Surface(
                    onClick = onCopy,
                    shape = CircleShape,
                    color = Color(0xFFF9FAFB),
                    border = BorderStroke(1.dp, Color(0xFFF3F4F6)),
                    modifier = Modifier.size(54.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Outlined.ContentCopy, null, tint = Color(0xFF4B5563), modifier = Modifier.size(22.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun LowStockAlertCardd(
    modifier: Modifier = Modifier,
    onRestockClick: () -> Unit = {}
) {
    val orangeColor = Color(0xFFFF6B00)
    val backgroundColor = Color(0xFFFFFCF5)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color(0xFFFFD3A6)),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(orangeColor, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.WarningAmber,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Low Stock Alert",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
                Text(
                    text = "5 products running low",
                    fontSize = 12.sp,
                    color = Color(0xFF4B5563)
                )
            }

            TextButton(
                onClick = onRestockClick,
                colors = ButtonDefaults.textButtonColors(contentColor = orangeColor)
            ) {
                Text("Restock", fontWeight = FontWeight.Bold)
            }
        }
    }
}
