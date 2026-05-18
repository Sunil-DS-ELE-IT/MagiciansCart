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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.elementzit.mc.ui.viewmodel.AddressViewModel
import com.elementzit.mc.ui.viewmodel.CartViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackOrderScreen(
    navController: NavController,
    orderId: String,
    cartViewModel: CartViewModel = hiltViewModel(LocalContext.current as ViewModelStoreOwner),
    addressViewModel: AddressViewModel = hiltViewModel()
) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    val selectedAddress by addressViewModel.selectedAddress.collectAsState()
    val totalAmount = cartItems.sumOf { it.product.price * it.quantity }
    
    val dateFormat = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault())
    val now = dateFormat.format(Date())

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
                        Text("Out for Delivery", color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Estimated Arrival", color = Color.White.copy(alpha = 0.8f))
                        Text("Today by 6:00 PM", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            item {
                Text("Delivery Progress", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        val steps = listOf(
                            Triple("Order Confirmed", now, Icons.Default.CheckCircle),
                            Triple("Packed", now, Icons.Default.Inventory),
                            Triple("Shipped", now, Icons.Default.LocalShipping),
                            Triple("Out for Delivery", now, Icons.Default.LocationOn),
                            Triple("Delivered", "Expected by 6:00 PM", Icons.Default.CheckCircle)
                        )
                        
                        steps.forEachIndexed { index, step ->
                            Row(modifier = Modifier.padding(bottom = 16.dp)) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(step.third, null, modifier = Modifier.size(32.dp).background(if (index <= 3) Color(0xFFFF5722) else Color.LightGray, CircleShape).padding(4.dp), tint = Color.White)
                                    if (index < steps.size - 1) Box(modifier = Modifier.width(2.dp).height(24.dp).background(if (index < 3) Color(0xFFFF5722) else Color.LightGray))
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(step.first, fontWeight = FontWeight.Bold, color = if (index <= 3) Color.Black else Color.Gray)
                                    Text(step.second, style = MaterialTheme.typography.bodySmall, color = if (index <= 3) Color(0xFFFF5722) else Color.Gray)
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
                        cartItems.forEach { item ->
                            Row(modifier = Modifier.padding(vertical = 8.dp)) {
                                AsyncImage(model = item.product.imageUrl, contentDescription = null, modifier = Modifier.size(60.dp).clip(RoundedCornerShape(8.dp)), contentScale = ContentScale.Crop)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(item.product.name, fontWeight = FontWeight.Bold)
                                    Text("Qty: ${item.quantity}", style = MaterialTheme.typography.bodySmall)
                                    Text("₹${item.product.price * item.quantity}", color = Color(0xFFFF5722), fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Amount", fontWeight = FontWeight.Bold)
                            Text("₹$totalAmount", fontWeight = FontWeight.Bold)
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
                            selectedAddress?.let { addr ->
                                Text(addr.name, fontWeight = FontWeight.Bold)
                                Text("${addr.street}, ${addr.city}")
                                Text("${addr.state} - ${addr.zipCode}")
                                Text(addr.phone)
                            } ?: Text("Address not available")
                        }
                    }
                }
            }
        }
    }
}
