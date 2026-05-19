package com.elementzit.mc.ui.screens.customer

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Payment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import com.elementzit.mc.ui.viewmodel.AddressViewModel
import com.elementzit.mc.ui.viewmodel.CartViewModel
import com.elementzit.mc.ui.viewmodel.CheckoutState
import com.elementzit.mc.ui.viewmodel.CheckoutViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController,
    cartViewModel: CartViewModel = hiltViewModel(LocalContext.current as ViewModelStoreOwner),
    addressViewModel: AddressViewModel = hiltViewModel(),
    checkoutViewModel: CheckoutViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val cartItems by cartViewModel.cartItems.collectAsState()
    val selectedAddress by addressViewModel.selectedAddress.collectAsState()
    val checkoutState by checkoutViewModel.checkoutState.collectAsState()
    
    val totalAmount = cartItems.sumOf { it.product.price * it.quantity }
    var selectedPaymentMethod by remember { mutableStateOf("UPI") }

    LaunchedEffect(checkoutState) {
        when (checkoutState) {
            is CheckoutState.Success -> {
                val orderId = (checkoutState as CheckoutState.Success).orderId
                cartViewModel.clearCart()
                navController.navigate("order_success/$orderId/${String.format(Locale.US, "%.0f", totalAmount)}") {
                    popUpTo("checkout") { inclusive = true }
                }
                checkoutViewModel.resetState()
            }
            is CheckoutState.Error -> {
                Toast.makeText(context, (checkoutState as CheckoutState.Error).message, Toast.LENGTH_SHORT).show()
                checkoutViewModel.resetState()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF8F8F8)),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Delivery Address Section
                item {
                    Text("Delivery Address", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable {
                            navController.navigate("add_address")
                        },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Rounded.LocationOn, contentDescription = null, tint = Color(0xFFFF5722))
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                if (selectedAddress != null) {
                                    Text(selectedAddress!!.name, fontWeight = FontWeight.Bold)
                                    Text("${selectedAddress!!.street}, ${selectedAddress!!.city}", color = Color.Gray, fontSize = 14.sp)
                                    Text("${selectedAddress!!.state} - ${selectedAddress!!.zipCode}", color = Color.Gray, fontSize = 14.sp)
                                    Text("Phone: ${selectedAddress!!.phone}", color = Color.Gray, fontSize = 14.sp)
                                } else {
                                    Text("No address selected", fontWeight = FontWeight.Bold, color = Color.Gray)
                                    Text("Click to add delivery address", color = Color(0xFFFF5722), fontSize = 14.sp)
                                }
                            }
                            Text("Change", color = Color(0xFFFF5722), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }

                // Order Summary Section
                item {
                    Text("Order Summary", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
                items(cartItems) { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${item.product.name} x ${item.quantity}", modifier = Modifier.weight(1f), maxLines = 1)
                        Text("$${String.format(Locale.US, "%,.2f", item.product.price * item.quantity)}")
                    }
                }

                // Payment Options Section
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Payment Method", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    PaymentOption("UPI", selectedPaymentMethod == "UPI") { selectedPaymentMethod = "UPI" }
                    PaymentOption("Credit/Debit Card", selectedPaymentMethod == "Card") { selectedPaymentMethod = "Card" }
                    PaymentOption("Net Banking", selectedPaymentMethod == "Banking") { selectedPaymentMethod = "Banking" }
                    PaymentOption("Cash on Delivery", selectedPaymentMethod == "COD") { selectedPaymentMethod = "COD" }
                }

                // Price Details
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Subtotal", color = Color.Gray)
                        Text("$${String.format(Locale.US, "%,.2f", totalAmount)}")
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Delivery Fee", color = Color.Gray)
                        Text("FREE", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total Amount", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                        Text(
                            "$${String.format(Locale.US, "%,.2f", totalAmount)}",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color(0xFFFF5722)
                        )
                    }
                }

                // Place Order Button
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { 
                            if (selectedAddress != null) {
                                checkoutViewModel.placeOrder(
                                    cartItems = cartItems,
                                    address = selectedAddress!!,
                                    paymentMethod = selectedPaymentMethod,
                                    totalAmount = totalAmount
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722)),
                        shape = RoundedCornerShape(16.dp),
                        enabled = selectedAddress != null && cartItems.isNotEmpty() && checkoutState !is CheckoutState.Loading
                    ) {
                        if (checkoutState is CheckoutState.Loading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("Place Order", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentOption(name: String, isSelected: Boolean, onSelect: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { onSelect() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        border = if (isSelected) BorderStroke(2.dp, Color(0xFFFF5722)) else null
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = isSelected, onClick = onSelect, colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFFF5722)))
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.Rounded.Payment, contentDescription = null, tint = Color.Gray)
            Spacer(modifier = Modifier.width(16.dp))
            Text(name, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
        }
    }
}
