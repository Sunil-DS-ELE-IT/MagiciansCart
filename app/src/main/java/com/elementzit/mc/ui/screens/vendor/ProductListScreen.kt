package com.elementzit.mc.ui.screens.vendor

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.Search
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.elementzit.mc.domain.model.Product
import com.elementzit.mc.ui.viewmodel.VendorProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    navController: NavController,
    viewModel: VendorProductViewModel = hiltViewModel()
) {
    val products by viewModel.products.collectAsState(initial = emptyList())
    val searchQuery by viewModel.searchQuery.collectAsState()
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Active", "Out of Stock", "Low Stock", "Drafts")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header Section
            Column(modifier = Modifier.padding(16.dp).padding(top = 8.dp)) {
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
                            Text("Magician's Cart", fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text("Vendor Panel", fontSize = 12.sp, color = Color.Gray, maxLines = 1)
                        }
                    }
                    Box {
                        Icon(Icons.Default.Notifications, null, tint = Color.Gray)
                        Surface(
                            modifier = Modifier.size(8.dp).align(Alignment.TopEnd).offset(x = 1.dp, y = 1.dp),
                            shape = CircleShape,
                            color = Color(0xFFFF6B00),
                            border = BorderStroke(1.dp, Color.White)
                        ) {}
                    }
                }
                
                Spacer(Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = searchQuery, onValueChange = { viewModel.onSearchQueryChange(it) },
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
                Text("Here's what's happening with your store today", color = Color.Gray, fontSize = 14.sp)
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Status Stats Row
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ProductStatusCard("8", "Total", Color(0xFFFF6B00), Modifier.weight(1f))
                        ProductStatusCard("6", "Active", Color(0xFF10B981), Modifier.weight(1f))
                        ProductStatusCard("3", "Low Stock", Color(0xFFF59E0B), Modifier.weight(1f))
                        ProductStatusCard("2", "Out of Stock", Color(0xFFEF4444), Modifier.weight(1f))
                    }
                }

                // Filter & View Toggle Row
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = "", onValueChange = {},
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("Search products...") },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )
                        )
                        Surface(
                            modifier = Modifier.size(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White,
                            border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.FilterList, null, tint = Color(0xFF4B5563))
                            }
                        }
                        Surface(
                            modifier = Modifier.height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF3F4F6)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 4.dp)) {
                                IconButton(onClick = { }) { Icon(Icons.AutoMirrored.Filled.List, null, tint = Color(0xFF4B5563)) }
                                IconButton(onClick = { }) { Icon(Icons.Default.GridView, null, tint = Color(0xFF9CA3AF)) }
                            }
                        }
                    }
                }

                // Filter Chips
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(filters) { filter ->
                            FilterChip(
                                selected = selectedFilter == filter,
                                onClick = { selectedFilter = filter },
                                label = { Text(filter, fontWeight = FontWeight.SemiBold) },
                                shape = RoundedCornerShape(20.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFFFF6B00),
                                    selectedLabelColor = Color.White,
                                    containerColor = Color.White,
                                    labelColor = Color(0xFF6B7280)
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    borderColor = if (selectedFilter == filter) Color.Transparent else Color(0xFFE5E7EB),
                                    enabled = true,
                                    selected = selectedFilter == filter
                                )
                            )
                        }
                    }
                }

                // Product List
                items(products) { product ->
                    ProductListCard(
                        product = product,
                        onEdit = { navController.navigate("edit_product/${product.id}") },
                        onVisibilityToggle = { viewModel.updateProduct(product.copy(isVisible = !product.isVisible)) },
                        onCopy = { viewModel.addProduct(product.copy(id = "", name = "${product.name} (Copy)")) },
                        onDelete = { viewModel.deleteProduct(product.id) }
                    )
                }
            }
        }

        // Floating Action Button moved outside nested Scaffold to Box to avoid layout issues
        FloatingActionButton(
            onClick = { navController.navigate("nav_category") },
            containerColor = Color(0xFFFF6B00),
            contentColor = Color.White,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp, end = 16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Product", modifier = Modifier.size(24.dp))
        }
    }
}

@Composable
fun ProductStatusCard(count: String, label: String, color: Color, modifier: Modifier) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, Color(0xFFF3F4F6))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(26.dp),
                shape = CircleShape,
                color = color
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(count, color = Color.White, fontWeight = FontWeight.Bold, fontSize =10.sp)
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(label, fontSize = 10.sp, color = Color(0xFF6B7280), fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun ProductListCard(
    product: Product,
    onEdit: () -> Unit,
    onVisibilityToggle: () -> Unit,
    onCopy: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFF3F4F6))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                // Image Box - Increased size from 80.dp to 100.dp
                Box(modifier = Modifier.size(100.dp)) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFFFF7ED)
                    ) {
                        AsyncImage(
                            model = product.imageUrls.firstOrNull() ?: product.imageUrl,
                            contentDescription = product.name,
                            modifier = Modifier.fillMaxSize().padding(8.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                    if (product.stock > 0) {
                        Surface(
                            modifier = Modifier.size(24.dp).align(Alignment.TopEnd).offset(x = 6.dp, y = (-6).dp),
                            shape = CircleShape,
                            color = Color(0xFF10B981),
                            border = BorderStroke(1.5.dp, Color.White)
                        ) {
                            Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.padding(4.dp))
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
                        Column {
                            Text(
                                product.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = Color(0xFF1F2937),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                product.category,
                                fontSize = 12.sp,
                                color = Color(0xFF6B7280),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Icon(Icons.Default.MoreVert, null, tint = Color(0xFF9CA3AF))
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        product.tags.take(2).forEach { tag ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFFFF7ED),
                                border = BorderStroke(0.5.dp, Color(0xFFFFE4D1))
                            ) {
                                Text(
                                    text = tag,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    color = Color(0xFFD97706),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("PRICE", fontSize = 10.sp, color = Color(0xFF9CA3AF), fontWeight = FontWeight.Bold)
                            Text("₹${product.price.toInt()}", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Color(0xFF111827))
                        }
                        Column {
                            Text("STOCK", fontSize = 10.sp, color = Color(0xFF9CA3AF), fontWeight = FontWeight.Bold)
                            Text("${product.stock}", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Color(0xFF10B981))
                        }
                        Column {
                            Text("SALES", fontSize = 10.sp, color = Color(0xFF9CA3AF), fontWeight = FontWeight.Bold)
                            Text("${product.sales}", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Color(0xFF3B82F6))
                        }
                        Column {
                            Text("RATING", fontSize = 10.sp, color = Color(0xFF9CA3AF), fontWeight = FontWeight.Bold)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, null, tint = Color(0xFFF59E0B), modifier = Modifier.size(14.dp))
                                Text("${product.rating}", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Color(0xFFF59E0B))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onEdit,
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
                ) {
                    Icon(Icons.Default.Edit, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Edit", fontWeight = FontWeight.Bold)
                }
                
                ProductActionIcon(if (product.isVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff, onVisibilityToggle)
                ProductActionIcon(Icons.Outlined.ContentCopy, onCopy)
                ProductActionIcon(Icons.Outlined.Delete, onDelete, isDanger = true)
            }
        }
    }
}

@Composable
fun ProductActionIcon(icon: ImageVector, onClick: () -> Unit, isDanger: Boolean = false) {
    Surface(
        onClick = onClick,
        modifier = Modifier.size(48.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF9FAFB),
        border = BorderStroke(1.dp, Color(0xFFF3F4F6))
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                icon, 
                null, 
                tint = if (isDanger) Color(0xFFEF4444).copy(alpha = 0.8f) else Color(0xFF4B5563),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
