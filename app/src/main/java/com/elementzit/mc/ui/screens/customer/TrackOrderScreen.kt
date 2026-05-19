package com.elementzit.mc.ui.screens.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.elementzit.mc.domain.model.OrderStatus
import com.elementzit.mc.ui.viewmodel.TrackOrderViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackOrderScreen(
    navController: NavController,
    orderId: String,
    viewModel: TrackOrderViewModel = hiltViewModel()
) {
    val order by viewModel.order.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(orderId) {
        viewModel.fetchOrder(orderId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Track Order", style = MaterialTheme.typography.titleMedium)
                        Text("Order ID: $orderId", style = MaterialTheme.typography.bodySmall)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFFF5722))
            }
        } else if (order == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Order details not found", color = Color.Gray)
            }
        } else {
            val currentOrder = order!!
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).background(Color(0xFFF5F5F5)),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFF5722))
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text("Current Status", color = Color.White.copy(alpha = 0.8f))
                            Text(
                                text = currentOrder.status.name,
                                color = Color.White,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            if (currentOrder.expectedDeliveryDate.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Estimated Arrival", color = Color.White.copy(alpha = 0.8f))
                                Text(currentOrder.expectedDeliveryDate, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                item {
                    Text("Delivery Progress", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            val statusIndex = currentOrder.status.ordinal
                            val steps = listOf(
                                Triple("Order Placed", "", Icons.Default.CheckCircle),
                                Triple("Order Confirmed", "", Icons.Default.CheckCircle),
                                Triple("Shipped", "", Icons.Default.LocalShipping),
                                Triple("Delivered", "", Icons.Default.CheckCircle)
                            )
                            
                            steps.forEachIndexed { index, step ->
                                val isCompleted = index <= statusIndex
                                Row(modifier = Modifier.padding(bottom = 16.dp)) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            imageVector = step.third,
                                            contentDescription = null,
                                            modifier = Modifier.size(32.dp)
                                                .background(if (isCompleted) Color(0xFF4CAF50) else Color.LightGray, CircleShape)
                                                .padding(4.dp),
                                            tint = Color.White
                                        )
                                        if (index < steps.size - 1) {
                                            Box(modifier = Modifier.width(2.dp).height(24.dp).background(if (index < statusIndex) Color(0xFF4CAF50) else Color.LightGray))
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column {
                                        Text(step.first, fontWeight = FontWeight.Bold, color = if (isCompleted) Color.Black else Color.Gray)
                                        if (index == 1 && currentOrder.expectedDeliveryDate.isNotEmpty()) {
                                            Text("Expected on ${currentOrder.expectedDeliveryDate}", style = MaterialTheme.typography.bodySmall, color = Color(0xFF4CAF50))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Text("Order Details", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            currentOrder.orderSummary.forEach { item ->
                                Row(modifier = Modifier.padding(vertical = 8.dp)) {
                                    AsyncImage(
                                        model = item.imageUrl,
                                        contentDescription = null,
                                        modifier = Modifier.size(60.dp).clip(RoundedCornerShape(8.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(item.productName, fontWeight = FontWeight.Bold)
                                        Text("Qty: ${item.quantity}", style = MaterialTheme.typography.bodySmall)
                                        Text("₹${item.price * item.quantity}", color = Color(0xFFFF5722), fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Total Amount", fontWeight = FontWeight.Bold)
                                Text("₹${currentOrder.totalAmount}", fontWeight = FontWeight.Bold, color = Color(0xFFFF5722), fontSize = 18.sp)
                            }
                        }
                    }
                }

                item {
                    Text("Delivery Address", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, null, tint = Color(0xFFFF5722))
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                currentOrder.customerAddress?.let { addr ->
                                    Text(addr.name, fontWeight = FontWeight.Bold)
                                    Text("${addr.street}, ${addr.city}")
                                    Text("${addr.state} - ${addr.zipCode}")
                                    Text("Phone: ${currentOrder.customerPhoneNumber}")
                                } ?: Text("Address not available")
                            }
                        }
                    }
                }
            }
        }
    }
}
