package com.elementzit.mc.ui.screens.vendor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.elementzit.mc.domain.model.Order
import com.elementzit.mc.domain.model.OrderStatus
import com.elementzit.mc.ui.viewmodel.VendorOrderViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendorOrdersScreen(
    navController: NavController,
    viewModel: VendorOrderViewModel = hiltViewModel()
) {
    val orders by viewModel.orders.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Customer Orders", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFFF5722))
            }
        } else if (orders.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No orders received yet", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF8F8F8)),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(orders) { order ->
                    OrderCard(
                        order = order,
                        onConfirm = { date -> viewModel.confirmOrder(order.orderId, date) }
                    )
                }
            }
        }
    }
}

@Composable
fun OrderCard(
    order: Order,
    onConfirm: (String) -> Unit
) {
    var showConfirmDialog by remember { mutableStateOf(false) }
    var deliveryDate by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Order #${order.orderId.takeLast(8)}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                StatusBadge(order.status)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
            Spacer(modifier = Modifier.height(8.dp))

            order.orderSummary.forEach { item ->
                Text("${item.productName} x ${item.quantity}", fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("Total Amount: ₹${order.totalAmount}", fontWeight = FontWeight.Bold, color = Color(0xFFFF5722))
            
            Spacer(modifier = Modifier.height(8.dp))
            Text("Customer: ${order.customerAddress?.name}", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text("Address: ${order.customerAddress?.street}, ${order.customerAddress?.city}", fontSize = 12.sp, color = Color.Gray)
            Text("Phone: ${order.customerPhoneNumber}", fontSize = 12.sp, color = Color.Gray)

            if (order.status == OrderStatus.PENDING) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { showConfirmDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Confirm Order")
                }
            } else if (order.expectedDeliveryDate.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Expected Delivery: ${order.expectedDeliveryDate}", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Confirm Order") },
            text = {
                Column {
                    Text("Enter expected delivery date and time:")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = deliveryDate,
                        onValueChange = { deliveryDate = it },
                        placeholder = { Text("e.g., 25th Oct, 10:00 AM") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (deliveryDate.isNotBlank()) {
                            onConfirm(deliveryDate)
                            showConfirmDialog = false
                        }
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun StatusBadge(status: OrderStatus) {
    val color = when (status) {
        OrderStatus.PENDING -> Color(0xFFFFA000)
        OrderStatus.CONFIRMED -> Color(0xFF4CAF50)
        OrderStatus.SHIPPED -> Color(0xFF2196F3)
        OrderStatus.DELIVERED -> Color(0xFF7B1FA2)
        OrderStatus.CANCELLED -> Color(0xFFF44336)
    }
    
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = status.name,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = color,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
