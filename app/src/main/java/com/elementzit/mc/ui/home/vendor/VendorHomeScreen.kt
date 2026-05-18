package com.elementzit.mc.ui.home.vendor

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.elementzit.mc.domain.model.Product
import com.elementzit.mc.ui.viewmodel.VendorProductViewModel
import androidx.compose.material3.SuggestionChipDefaults // Import Material3 SuggestionChipDefaults
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

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
                value = "", onValueChange = {},
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
                DetailedProductItem(product = product, navController = navController)
            }

            //Restock
            item {
                //ActionCard("Low Stock Alert", "5 products are running low on stock", Color(0xFFFF6B00), Icons.Default.Add, Modifier.weight(1f)) { navController.navigate("Restock Now") } // Navigate to AddProduct screen
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
        Box(modifier = Modifier.fillMaxSize().clickable(onClick = onClick))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedProductItem(product: Product, navController: NavController) {
    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Box(modifier = Modifier.size(70.dp), contentAlignment = Alignment.BottomEnd) {
                    Surface(modifier = Modifier.fillMaxSize(), shape = RoundedCornerShape(12.dp), color = Color(0xFFF5F5F5)) {
                        // Placeholder for product image based on product category/type
                        // For demonstration, using text as placeholder
                       /* val productIconText = when (product.category) {
                            "HANDMADE" -> "🎨"
                            "FASHION" -> "👜"
                            "ACCESSORIES" -> "☕"
                            else -> "📦"
                        }*/

                        AsyncImage(
                            model = product.imageUrl,
                            contentDescription = product.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        Box(contentAlignment = Alignment.Center) {
                          //  Text(productIconText, fontSize = 30.sp)
                        }
                    }
                    // Status Icon (Green Check or Red Exclamation)
                    val statusIcon = if (product.stock > 0) Icons.Filled.CheckCircle else Icons.Filled.Warning
                    val statusColor = if (product.stock > 0) Color(0xFF4CAF50) else Color(0xFFF44336)
                    Icon(statusIcon, null, tint = statusColor, modifier = Modifier.size(20.dp).offset(x = 4.dp, y = 4.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(product.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(product.category, fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    
                    // Tags (SuggestionChips)
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.padding(top = 4.dp)) {
                        product.tags.forEach { tag ->
                            SuggestionChip(onClick = {}, label = { Text(tag, fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis) }, colors = SuggestionChipDefaults.suggestionChipColors(containerColor = Color(0xFFFFF3E0), labelColor = Color(0xFFFF9800)))
                        }
                    }
                }
                Icon(Icons.Default.MoreVert, contentDescription = null, tint = Color.Gray)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("₹${product.price}", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                val stockColor = if (product.stock > 0) Color(0xFF4CAF50) else Color(0xFFF44336)
                Text("• ${product.stock} left", color = stockColor, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                if (product.rating > 0) {
                    Surface(color = Color(0xFFFFF9C4), shape = RoundedCornerShape(4.dp)) {
                        Text("⭐ ${product.rating}", modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontWeight = FontWeight.Bold, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = { navController.navigate("edit_product/${product.id}") }, modifier = Modifier.weight(1f).height(40.dp), shape = RoundedCornerShape(20.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))) {
                    Icon(Icons.Outlined.Edit, null, modifier = Modifier.size(16.dp), tint = Color.White)
                    Spacer(Modifier.width(4.dp))
                    Text("Edit", color = Color.White)
                }
                IconButton(onClick = {}, modifier = Modifier.size(40.dp).background(Color(0xFFF5F5F5), RoundedCornerShape(50))) { Icon(Icons.Outlined.Visibility, null, tint = Color.Gray) }
                IconButton(onClick = {}, modifier = Modifier.size(40.dp).background(Color(0xFFF5F5F5), RoundedCornerShape(50))) { Icon(Icons.Outlined.ContentCopy, null, tint = Color.Gray) }
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
            .padding(horizontal = 4.dp), // Matched padding with DetailedProductItem
        shape = RoundedCornerShape(20.dp), // Consistent with other cards
        border = BorderStroke(1.dp, Color(0xFFFFD3A6)),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically // Better alignment for tighter layout
        ) {
            // Smaller Icon Box
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
                    fontSize = 16.sp, // Reduced to match headers
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
                Text(
                    text = "5 products running low",
                    fontSize = 12.sp, // Reduced to match subtext
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

@Composable
fun LowStockAlertCard(
    modifier: Modifier = Modifier,
    onRestockClick: () -> Unit = {}
) {

    val orangeColor = Color(0xFFFF6B00)
    val backgroundColor = Color(0xFFFFFCF5)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(28.dp),
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFFFFD3A6)
        ),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 24.dp
                ),
            verticalAlignment = Alignment.Top
        ) {

            // Alert Icon Box
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        color = orangeColor,
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.WarningAmber,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(34.dp)
                )
            }

            Spacer(modifier = Modifier.width(18.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Low Stock Alert",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "5 products are running low on stock",
                    fontSize = 18.sp,
                    color = Color(0xFF4B5563)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Restock Now",
                    color = orangeColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        onRestockClick()
                    }
                )
            }
        }
    }
}
