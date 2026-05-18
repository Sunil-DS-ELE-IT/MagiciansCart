package com.elementzit.mc.ui.screens.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.ShoppingCart
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.elementzit.mc.domain.model.CartItem
import com.elementzit.mc.ui.viewmodel.CartViewModel
import java.util.Locale

@Composable
fun CartScreen(
    navController: NavController,
    viewModel: CartViewModel = hiltViewModel(LocalContext.current as ViewModelStoreOwner)
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val totalAmount = cartItems.sumOf { it.product.price * it.quantity }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
    ) {
        Text(
            text = "My Cart",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        if (cartItems.isEmpty()) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Rounded.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Your cart is empty", color = Color.Gray)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(cartItems) { item ->
                    CartItemRow(
                        item = item,
                        onIncrement = { viewModel.updateQuantity(item.product.id, item.quantity + 1) },
                        onDecrement = {
                            if (item.quantity > 1) {
                                viewModel.updateQuantity(
                                    item.product.id,
                                    item.quantity - 1
                                )
                            } else {
                                viewModel.removeFromCart(item.product.id)
                            }
                        },
                        onRemove = { viewModel.removeFromCart(item.product.id) }
                    )
                }
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 8.dp,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total Amount", style = MaterialTheme.typography.titleMedium, color = Color.Gray)
                        Text(
                            "$${String.format(Locale.US, "%,.2f", totalAmount)}",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF5722)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { navController.navigate("checkout") }, // Navigate to checkout
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Checkout", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }

                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    item: CartItem,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.product.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.product.name, fontWeight = FontWeight.Bold, maxLines = 1)
                Text(
                    "$${String.format(Locale.US, "%,.2f", item.product.price)}",
                    color = Color(0xFFFF5722),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onDecrement,
                        modifier = Modifier.size(32.dp).background(Color(0xFFF0F0F0), CircleShape)
                    ) {
                        Icon(Icons.Rounded.Remove, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                    Text(
                        text = item.quantity.toString(),
                        modifier = Modifier.padding(horizontal = 12.dp),
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        onClick = onIncrement,
                        modifier = Modifier.size(32.dp).background(Color(0xFFF0F0F0), CircleShape)
                    ) {
                        Icon(Icons.Rounded.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
            }
            IconButton(onClick = onRemove) {
                Icon(Icons.Rounded.Delete, contentDescription = "Remove", tint = Color.LightGray)
            }
        }
    }
}
