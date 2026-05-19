package com.elementzit.mc.ui.home.customer

import android.location.Location
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.DirectionsWalk
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.elementzit.mc.domain.model.Product
import com.sds.marketplace.ui.home.components.ProductCard
import com.elementzit.mc.ui.screens.customer.CartScreen
import com.elementzit.mc.ui.screens.customer.CustomerOrdersScreen
import com.elementzit.mc.ui.viewmodel.CartViewModel
import com.elementzit.mc.ui.viewmodel.CustomerProductViewModel
import com.elementzit.mc.utils.rememberCurrentLocationData
import com.elementzit.mc.R
import java.util.Locale
import kotlin.math.*

private data class NavItem(
    val label: Int,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
    val badgeCount: Int? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerHomeScreen(
    navController: NavController,
    onLogout: () -> Unit,
    viewModel: CustomerProductViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel(LocalContext.current as ViewModelStoreOwner)
) {
    var selectedNavItem by remember { mutableIntStateOf(0) }
    var fullScreenList: List<Product>? by remember { mutableStateOf(null) }
    var fullScreenTitle by remember { mutableStateOf("") }
    var fullScreenSearchQuery by remember { mutableStateOf("") }
    var showCustomerOrders by remember { mutableStateOf(false) }

    val products by viewModel.products.collectAsState(initial = emptyList())
    val cartItems by cartViewModel.cartItems.collectAsState()
    
    val (locationName, currentLocation) = rememberCurrentLocationData()
    var vendorLocations by remember { mutableStateOf<Map<String, Pair<Double, Double>>>(emptyMap()) }

    LaunchedEffect(Unit) {
        FirebaseFirestore.getInstance().collection("vendors").get()
            .addOnSuccessListener { snapshot ->
                val map = mutableMapOf<String, Pair<Double, Double>>()
                snapshot.documents.forEach { doc ->
                    val lat = doc.getDouble("latitude") ?: 0.0
                    val lng = doc.getDouble("longitude") ?: 0.0
                    map[doc.id] = lat to lng
                }
                vendorLocations = map
            }
    }

    if (fullScreenList != null) {
        val filteredList: List<Product> = fullScreenList!!.filter { p: Product -> p.name.contains(fullScreenSearchQuery, true) || p.category.contains(fullScreenSearchQuery, true) }
        Scaffold(
            topBar = {
                Column(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
                    TopAppBar(
                        title = { Text(fullScreenTitle, fontWeight = FontWeight.Bold) },
                        navigationIcon = {
                            IconButton(onClick = { fullScreenList = null; fullScreenSearchQuery = "" }) {
                                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                            }
                        }
                    )
                    OutlinedTextField(
                        value = fullScreenSearchQuery,
                        onValueChange = { fullScreenSearchQuery = it },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                        placeholder = { Text("Search in $fullScreenTitle...") },
                        leadingIcon = { Icon(Icons.Rounded.Search, null) },
                        shape = RoundedCornerShape(24.dp),
                        singleLine = true
                    )
                }
            }
        ) { padding ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(padding).fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(filteredList) { p -> 
                    val distance = if (currentLocation != null) {
                        val (vLat, vLng) = vendorLocations[p.vendorId] ?: (0.0 to 0.0)
                        calculateDistance(currentLocation.latitude, currentLocation.longitude, vLat, vLng)
                    } else null
                    ProductBox(product = p, onAddToCart = { cartViewModel.addToCart(it) }, distance = distance?.let { if (it < 1.0) "${(it * 1000).toInt()}m away" else String.format(Locale.US, "%.1fkm away", it) })
                }
            }
        }
    } else {
        Scaffold(
            bottomBar = {
                NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                    val navItems = listOf(
                        NavItem(R.string.nav_home, Icons.Rounded.Home, Icons.Rounded.Home),
                        NavItem(R.string.nav_cart, Icons.Rounded.ShoppingCart, Icons.Rounded.ShoppingCart, badgeCount = if (cartItems.isNotEmpty()) cartItems.sumOf { it.quantity } else null),
                        NavItem(R.string.nav_wishlist, Icons.Rounded.FavoriteBorder, Icons.Rounded.Favorite),
                        NavItem(R.string.nav_profile, Icons.Rounded.PersonOutline, Icons.Rounded.Person)
                    )
                    navItems.forEachIndexed { index, item ->
                        val isSelected = selectedNavItem == index
                        val activeColor = Color(0xFFFF5722)
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { 
                                selectedNavItem = index
                                if (index != 3) showCustomerOrders = false
                            },
                            icon = {
                                BadgedBox(
                                    badge = {
                                        if (item.badgeCount != null) {
                                            Badge { Text(item.badgeCount.toString()) }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (isSelected) item.selectedIcon else item.icon,
                                        contentDescription = stringResource(item.label),
                                        tint = if (isSelected) activeColor else Color(0xFF9E9E9E)
                                    )
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                        )
                    }
                }
            }
        ) { padding ->
            Box(modifier = Modifier.padding(padding).fillMaxSize().background(Color(0xFFF8F8F8))) {
                Crossfade(targetState = selectedNavItem, label = "ScreenTransition") { screenIndex ->
                    when (screenIndex) {
                        0 -> HomeContent(
                            products = products,
                            onSeeAll = { title, productList ->
                                fullScreenTitle = title
                                fullScreenList = productList
                            },
                            onAddToCart = { cartViewModel.addToCart(it) },
                            currentLocation = currentLocation,
                            vendorLocations = vendorLocations
                        )
                        1 -> CartScreen(navController = navController, viewModel = cartViewModel)
                        2 -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Wishlist") }
                        3 -> {
                            if (showCustomerOrders) {
                                CustomerOrdersScreen(onBack = { showCustomerOrders = false })
                            } else {
                                ProfileContent(
                                    onLogout = onLogout, 
                                    navController = navController,
                                    onMyOrdersClick = { showCustomerOrders = true }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeContent(
    products: List<Product>,
    onSeeAll: (String, List<Product>) -> Unit,
    onAddToCart: (Product) -> Unit,
    currentLocation: Location?,
    vendorLocations: Map<String, Pair<Double, Double>>
) {
    var selectedCategory by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    val (locationName, _) = rememberCurrentLocationData()

    val sortedProducts: List<Product> = if (currentLocation != null) {
        products.sortedBy { p: Product ->
            val (vLat, vLng) = vendorLocations[p.vendorId] ?: (0.0 to 0.0)
            calculateDistance(currentLocation.latitude, currentLocation.longitude, vLat, vLng)
        }
    } else products

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize().animateContentSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item(span = { GridItemSpan(2) }) { LocationHeader(locationName) }
        item(span = { GridItemSpan(2) }) { HomeSearchBar(searchQuery) { searchQuery = it } }
        
        if (searchQuery.isEmpty()) {
            item(span = { GridItemSpan(2) }) { PromoSlider() }
            item(span = { GridItemSpan(2) }) { CategorySection(selectedCategory) { selectedCategory = it } }
        }
        
        val filteredProducts: List<Product> = if (searchQuery.isEmpty()) {
            if (selectedCategory == "All") sortedProducts else sortedProducts.filter { it.category == selectedCategory }
        } else sortedProducts.filter { p: Product -> p.name.contains(searchQuery, true) }

        val sections = listOf(
            Triple(if (selectedCategory == "All") "All Products" else selectedCategory, "🛍️", filteredProducts)
        )

        sections.forEach { (title, icon, items) ->
            if (items.isNotEmpty()) {
                item(span = { GridItemSpan(2) }) { 
                    SectionHeader(title, icon, showSeeAll = searchQuery.isEmpty()) { onSeeAll(title, items) } 
                }
                val displayItems = if (searchQuery.isNotEmpty()) items else items.take(4)
                items(displayItems) { p -> 
                    val distance = if (currentLocation != null) {
                        val (vLat, vLng) = vendorLocations[p.vendorId] ?: (0.0 to 0.0)
                        calculateDistance(currentLocation.latitude, currentLocation.longitude, vLat, vLng)
                    } else null
                    ProductBox(product = p, onAddToCart = onAddToCart, distance = distance?.let { if (it < 1.0) "${(it * 1000).toInt()}m away" else String.format(Locale.US, "%.1fkm away", it) })
                }
            }
        }
    }
}

@Composable
fun ProductBox(
    product: Product,
    onAddToCart: (Product) -> Unit,
    distance: String?
) {
    Box(modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)) {
        ProductCard(product = product, onAddToCart = onAddToCart, distance = distance)
    }
}

private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val r = 6371
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = sin(dLat / 2) * sin(dLat / 2) + cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(dLon / 2) * sin(dLon / 2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return r * c
}

@Composable
private fun SectionHeader(title: String, icon: String, showSeeAll: Boolean, onSeeAllClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            if (icon.isNotEmpty()) {
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = icon, fontSize = 20.sp)
            }
        }
        if (showSeeAll) {
            Text(
                text = "See All >",
                color = Color(0xFFFF5722),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.clickable { onSeeAllClick() }
            )
        }
    }
}

@Composable
private fun LocationHeader(location: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.LocationOn, contentDescription = null, tint = Color(0xFFFF5722), modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = "Deliver to", fontSize = 12.sp, color = Color.Gray)
                Text(text = location, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1, color = Color(0xFFFF5722))
            }
        }
        Icon(Icons.Rounded.NotificationsNone, contentDescription = "Notifications", modifier = Modifier.size(28.dp))
    }
}

@Composable
fun HomeSearchBar(query: String, onQueryChange: (String) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.Search, contentDescription = null, tint = Color.Gray)
            Spacer(modifier = Modifier.width(8.dp))
            TextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text("Search products...") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.weight(1f)
            )
            Icon(Icons.Rounded.MicNone, contentDescription = null, tint = Color(0xFFFF5722))
        }
    }
}

@Composable
private fun PromoSlider() {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { PromoCard("Flat 50% OFF", "on Fashion", "Shop Now", Color(0xFFFF5722)) }
        item { PromoCard("UP TO 30% OFF", "on Electronics", "Explore", Color(0xFF191E29)) }
    }
}

@Composable
private fun PromoCard(title: String, subtitle: String, buttonText: String, containerColor: Color) {
    Card(modifier = Modifier.width(300.dp).height(160.dp), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = containerColor)) {
        Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(text = title, color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Text(text = subtitle, color = Color.White.copy(alpha = 0.8f), fontSize = 18.sp)
            }
            Button(onClick = { }, colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black), shape = RoundedCornerShape(24.dp)) {
                Text(text = buttonText, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun CategorySection(selectedCategory: String, onCategorySelected: (String) -> Unit) {
    val categories = listOf(
        CategoryItemData("All", Icons.Rounded.Widgets),
        CategoryItemData("Fashion", Icons.Rounded.ShoppingBag),
        CategoryItemData("Grocery", Icons.Rounded.ShoppingBasket),
        CategoryItemData("Electronics", Icons.Rounded.Devices),
        CategoryItemData("Footwear", Icons.AutoMirrored.Rounded.DirectionsWalk),
        CategoryItemData("Beauty", Icons.Rounded.AutoAwesome)
    )

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(categories) { category ->
            val isSelected = category.name == selectedCategory
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onCategorySelected(category.name) }
            ) {
                Box(
                    modifier = Modifier.size(64.dp).clip(RoundedCornerShape(16.dp)).background(if (isSelected) Color(0xFFFF5722) else Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = category.icon, contentDescription = null, tint = if (isSelected) Color.White else Color.Black, modifier = Modifier.size(32.dp))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = category.name, 
                    fontSize = 12.sp, 
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) Color.Black else Color.Gray
                )
            }
        }
    }
}

@Composable
private fun ProfileContent(
    navController: NavController, 
    onLogout: () -> Unit,
    onMyOrdersClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(100.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Icon(
                Icons.Rounded.Person,
                contentDescription = null,
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Customer Name", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(text = "customer@example.com", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        val menuItems = listOf("My Orders", "Shipping Address", "Payment Methods", "Settings")
        menuItems.forEach { item ->
            OutlinedButton(
                onClick = { 
                    if (item == "My Orders") onMyOrdersClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(item)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                FirebaseAuth.getInstance().signOut()
                navController.navigate("login") {
                    popUpTo(0) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722))
        ) {
            Text("Logout", color = Color.White)
        }
    }
}

data class CategoryItemData(val name: String, val icon: ImageVector)
