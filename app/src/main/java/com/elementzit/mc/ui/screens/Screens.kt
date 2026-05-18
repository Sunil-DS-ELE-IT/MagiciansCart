/*
package com.sds.marketplace.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.elementzit.mc.model.Product
import com.elementzit.mc.model.User
import com.elementzit.mc.model.UserRole
import com.sds.marketplace.ui.theme.MarketplaceTheme
import java.util.Locale

*/
/**
 * Mock data for demonstration purposes.
 *//*

val mockProducts = listOf(
    Product("1", "Premium Coffee", "Rich and dark roast", 15.99, "https://picsum.photos/seed/coffee/400", "v1", "Beverages"),
    Product("2", "Wireless Mouse", "Ergonomic design", 25.50, "https://picsum.photos/seed/mouse/400", "v2", "Electronics"),
    Product("3", "Cotton T-Shirt", "100% organic cotton", 19.99, "https://picsum.photos/seed/tshirt/400", "v1", "Clothing"),
    Product("4", "Succulent Plant", "Easy to care for", 12.00, "https://picsum.photos/seed/plant/400", "v3", "Home & Garden"),
    Product("5", "Leather Wallet", "Handcrafted leather", 45.00, "https://picsum.photos/seed/wallet/400", "v2", "Accessories"),
    Product("6", "Noise Cancelling Headphones", "Studio quality sound", 199.99, "https://picsum.photos/seed/headphones/400", "v1", "Electronics")
)

val mockUsers = listOf(
    User("u1", "John Doe", "john@example.com", UserRole.CUSTOMER),
    User("u2", "Alice Smith", "alice@vendor.com", UserRole.VENDOR),
    User("u3", "Bob Wilson", "bob@admin.com", UserRole.ADMIN),
    User("u4", "Emma Brown", "emma@example.com", UserRole.CUSTOMER)
)

*/
/**
 * Reusable Top App Bar for the Marketplace screens.
 *//*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketplaceTopAppBar(
    title: String,
    onLogout: () -> Unit
) {
    TopAppBar(
        title = { Text(title, fontWeight = FontWeight.Bold) },
        actions = {
            IconButton(onClick = onLogout) {
                Icon(Icons.AutoMirrored.Rounded.Logout, contentDescription = "Logout")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

*/
/**
 * Screen displaying the Admin Home interface.
 *//*

@Composable
fun AdminHomeScreen(onLogout: () -> Unit) {
    Scaffold(
        topBar = { MarketplaceTopAppBar("Admin Dashboard", onLogout) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "System Overview",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "Total Users",
                        value = "1,234",
                        icon = Icons.Rounded.People,
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "Vendors",
                        value = "56",
                        icon = Icons.Rounded.Storefront,
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                }
            }

            item {
                StatCard(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Monthly Sales",
                    value = "$12,450.00",
                    icon = Icons.AutoMirrored.Rounded.TrendingUp,
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Recent Users",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            items(mockUsers) { user ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    ListItem(
                        headlineContent = { Text(user.name, fontWeight = FontWeight.Medium) },
                        supportingContent = { Text(user.email) },
                        trailingContent = {
                            Surface(
                                shape = CircleShape,
                                color = when(user.role) {
                                    UserRole.ADMIN -> MaterialTheme.colorScheme.errorContainer
                                    UserRole.VENDOR -> MaterialTheme.colorScheme.secondaryContainer
                                    UserRole.CUSTOMER -> MaterialTheme.colorScheme.primaryContainer
                                },
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    user.role.name,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = when(user.role) {
                                        UserRole.ADMIN -> MaterialTheme.colorScheme.onErrorContainer
                                        UserRole.VENDOR -> MaterialTheme.colorScheme.onSecondaryContainer
                                        UserRole.CUSTOMER -> MaterialTheme.colorScheme.onPrimaryContainer
                                    }
                                )
                            }
                        },
                        leadingContent = {
                            Icon(Icons.Rounded.AccountCircle, contentDescription = null, modifier = Modifier.size(40.dp))
                        }
                    )
                }
            }
        }
    }
}

*/
/**
 * Screen displaying the Customer Home interface.
 *//*

@Composable
fun CustomerHomeScreen(onLogout: () -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    val categories = listOf("All", "Beverages", "Electronics", "Clothing", "Home & Garden", "Accessories")
    var selectedCategory by remember { mutableStateOf("All") }

    Scaffold(
        topBar = { MarketplaceTopAppBar("Marketplace", onLogout) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search products...") },
                leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                shape = RoundedCornerShape(24.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            // Categories
            LazyRow(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(categories) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category) }
                    )
                }
            }

            // Products Grid
            val filteredProducts = mockProducts.filter { 
                (selectedCategory == "All" || it.category == selectedCategory) &&
                (it.name.contains(searchQuery, ignoreCase = true) || it.category.contains(searchQuery, ignoreCase = true))
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredProducts) { product ->
                    ProductCard(product)
                }
            }
        }
    }
}

*/
/**
 * Screen displaying the Vendor Home interface.
 *//*

@Composable
fun VendorHomeScreen(onLogout: () -> Unit) {
    Scaffold(
        topBar = { MarketplaceTopAppBar("Vendor Console", onLogout) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { */
/* Add product *//*
 },
                icon = { Icon(Icons.Rounded.Add, contentDescription = null) },
                text = { Text("Add Product") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            "Sales Summary",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    "$1,450.00",
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    "Earnings this month",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                )
                            }
                            Icon(
                                Icons.Rounded.BarChart,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    "Your Inventory",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Vendor's products (mocking for vendor v1)
            val vendorProducts = mockProducts.filter { it.vendorId == "v1" }
            items(vendorProducts) { product ->
                InventoryItem(product)
            }
        }
    }
}

*/
/**
 * Reusable card for displaying statistical information.
 *//*

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    containerColor: Color
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(title, style = MaterialTheme.typography.labelMedium)
        }
    }
}

*/
/**
 * Card for displaying product details.
 *//*

@Composable
fun ProductCard(product: Product) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    product.category,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "$${String.format(Locale.US, "%.2f", product.price)}",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        onClick = { */
/* Add to cart *//*
 },
                        modifier = Modifier
                            .size(36.dp)
                            .background(MaterialTheme.colorScheme.primary, CircleShape)
                    ) {
                        Icon(
                            Icons.Rounded.Add,
                            contentDescription = "Add to cart",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

*/
/**
 * Card for displaying inventory items (used in VendorHomeScreen).
 *//*

@Composable
fun InventoryItem(product: Product) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(product.name, fontWeight = FontWeight.Bold)
                Text("$${product.price}", color = MaterialTheme.colorScheme.primary)
                Text("Category: ${product.category}", style = MaterialTheme.typography.labelSmall)
            }
            Row {
                IconButton(onClick = { */
/* Edit *//*
 }) {
                    Icon(Icons.Rounded.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = { */
/* Delete *//*
 }) {
                    Icon(Icons.Rounded.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun CustomerHomePreview() {
    MarketplaceTheme {
        CustomerHomeScreen(onLogout = {})
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun VendorHomePreview() {
    MarketplaceTheme {
        VendorHomeScreen(onLogout = {})
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun AdminHomePreview() {
    MarketplaceTheme {
        AdminHomeScreen(onLogout = {})
    }
}
*/
