package com.elementzit.mc.ui.screens.vendor

import android.text.format.DateUtils
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.elementzit.mc.domain.model.Order
import com.elementzit.mc.ui.viewmodel.VendorOrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    navController: NavController,
    viewModel: VendorOrderViewModel = hiltViewModel()
) {
    val orders by viewModel.orders.collectAsState()
    val filteredOrders by viewModel.filteredOrders.collectAsState(initial = emptyList())
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()

    val pendingCount = orders.count { it.status == "Pending" || it.status == "Confirmed" }
    val inProductionCount = orders.count { it.status == "In Production" }
    val shippedCount = orders.count { it.status == "Shipped" }
    val completedCount = orders.count { it.status == "Delivered" }

    val filters = listOf("Pending", "In Production", "Shipped", "Delivered")

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFF9F9F9)).padding(bottom = 50.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .padding(top = 50.dp)
                    .padding(horizontal = 4.dp)
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box {
                            Icon(Icons.Default.Notifications, null, tint = Color.Gray)
                            Surface(
                                modifier = Modifier.size(8.dp).align(Alignment.TopEnd).offset(x = 1.dp, y = 1.dp),
                                shape = CircleShape,
                                color = Color(0xFFFF6B00),
                                border = BorderStroke(1.dp, Color.White)
                            ) {}
                        }
                        Spacer(Modifier.width(12.dp))
                        Surface(
                            modifier = Modifier.size(36.dp),
                            shape = CircleShape,
                            color = Color(0xFFFF9800)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("A", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }
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
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
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

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Stat Cards
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OrderStatCard("New Orders", pendingCount.toString(), Color(0xFFFF6B00), Modifier.weight(1f))
                        OrderStatCard("In Production", inProductionCount.toString(), Color(0xFF9333EA), Modifier.weight(1f))
                        OrderStatCard("Shipped", shippedCount.toString(), Color(0xFF0284C7), Modifier.weight(1f))
                        OrderStatCard("Completed", completedCount.toString(), Color(0xFF10B981), Modifier.weight(1f))
                    }
                }

                // Filter Chips
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(filters) { filter ->
                            FilterChip(
                                selected = selectedFilter == filter,
                                onClick = { viewModel.onFilterChange(filter) },
                                label = { Text(filter, fontWeight = FontWeight.SemiBold) },
                                shape = RoundedCornerShape(20.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFFF3F4F6), // Slightly gray when selected
                                    selectedLabelColor = Color(0xFF374151),
                                    containerColor = Color.White,
                                    labelColor = Color(0xFF6B7280)
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    borderColor = Color.Transparent,
                                    enabled = true,
                                    selected = selectedFilter == filter
                                )
                            )
                        }
                        item {
                            Surface(
                                modifier = Modifier.size(36.dp),
                                shape = CircleShape,
                                color = Color(0xFFF3F4F6)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.FilterList, null, tint = Color(0xFF4B5563), modifier = Modifier.size(20.dp))
                                }
                            }
                        }
                    }
                }

                // Order List
                items(filteredOrders) { order ->
                    OrderItemCard(
                        order = order,
                        onUpdateStatus = { viewModel.updateOrderStatus(order.id, it) }
                    )
                }
            }
        }
    }
}

@Composable
fun OrderStatCard(label: String, count: String, color: Color, modifier: Modifier) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, Color(0xFFF3F4F6))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(32.dp),
                shape = CircleShape,
                color = color
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(count, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(label, fontSize = 9.sp, color = Color(0xFF6B7280), fontWeight = FontWeight.Medium, maxLines = 1)
        }
    }
}

@Composable
fun OrderItemCard(
    order: Order,
    onUpdateStatus: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFF3F4F6))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
            Surface(
                modifier = Modifier.size(60.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFFFF7ED)
            ) {
                if (order.productImage.isNotBlank()) {
                    AsyncImage(
                        model = order.productImage,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(contentAlignment = Alignment.Center) {
                        Text("🎨", fontSize = 28.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            order.productName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF1F2937),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        if (order.type == "Custom") {
                            Surface(shape = RoundedCornerShape(10.dp), color = Color(0xFFF3E8FF)) {
                                Text("Custom", modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), color = Color(0xFF9333EA), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Text("›", fontSize = 20.sp, color = Color.Gray)
                }
                Text("${order.orderId} • ${order.customerName}", fontSize = 11.sp, color = Color(0xFF6B7280))
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val statusColor = when (order.status) {
                        "Confirmed", "Pending" -> Color(0xFF3B82F6)
                        "In Production" -> Color(0xFF9333EA)
                        "Shipped" -> Color(0xFFFF9800)
                        else -> Color(0xFF10B981)
                    }
                    val statusBgColor = when (order.status) {
                        "Confirmed", "Pending" -> Color(0xFFEFF6FF)
                        "In Production" -> Color(0xFFF3E8FF)
                        "Shipped" -> Color(0xFFFFF7ED)
                        else -> Color(0xFFECFDF5)
                    }
                    Surface(shape = RoundedCornerShape(12.dp), color = statusBgColor) {
                        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                if (order.status == "In Production") Icons.Default.Inventory else Icons.Default.CheckCircle,
                                null,
                                tint = statusColor,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(order.status, color = statusColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(Modifier.width(8.dp))
                    Text("•", color = Color.Gray, fontSize = 10.sp)
                    Spacer(Modifier.width(8.dp))
                    Text(getRelativeTime(order.timestamp), fontSize = 11.sp, color = Color(0xFF6B7280))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("₹${order.price.toInt()}", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Color(0xFF111827))
                        Spacer(Modifier.width(8.dp))
                        Text("Qty: ${order.quantity}", fontSize = 12.sp, color = Color(0xFF6B7280))
                    }
                    
                    val actionText = when (order.status) {
                        "Pending", "Confirmed" -> "Start Production"
                        "In Production" -> "Mark Shipped"
                        "Shipped" -> "Mark Delivered"
                        else -> ""
                    }
                    
                    if (actionText.isNotEmpty()) {
                        val nextStatus = when (order.status) {
                            "Pending", "Confirmed" -> "In Production"
                            "In Production" -> "Shipped"
                            "Shipped" -> "Delivered"
                            else -> ""
                        }
                        Text(
                            text = actionText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (order.status == "In Production") Color(0xFFFF6B00) else Color(0xFF9333EA),
                            modifier = Modifier.clickable { onUpdateStatus(nextStatus) }
                        )
                    }
                }
            }
        }
    }
}

fun getRelativeTime(timestamp: Long): String {
    return DateUtils.getRelativeTimeSpanString(
        timestamp,
        System.currentTimeMillis(),
        DateUtils.MINUTE_IN_MILLIS,
        DateUtils.FORMAT_ABBREV_RELATIVE
    ).toString()
}
