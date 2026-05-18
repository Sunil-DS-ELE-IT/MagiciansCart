package com.sds.marketplace.ui.home.vendor

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ListAlt
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.elementzit.mc.ui.home.vendor.VendorHomeScreen
import com.elementzit.mc.ui.viewmodel.VendorProductViewModel

data class BottomNavItem(val title: String, val icon: ImageVector, val route: String)

@Composable
fun VendorNavigationWrapper(navController: NavController, onLogout: () -> Unit, viewModel: VendorProductViewModel = hiltViewModel()) {
    val bottomNavController = rememberNavController()
    val items = listOf(
        BottomNavItem("Home", Icons.Outlined.GridView, "vendor_home_screen"),
        BottomNavItem("Products", Icons.Outlined.Inventory2, "vendor_products"),
        BottomNavItem("Orders", Icons.AutoMirrored.Outlined.ListAlt, "vendor_orders"),
        BottomNavItem("Analytics", Icons.Outlined.QueryStats, "vendor_stats"),
        BottomNavItem("Profile", Icons.Outlined.PersonOutline, "vendor_profile")
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFFF8F5EF), // Match the cream background from screenshot
                tonalElevation = 0.dp
            ) {
                val currentRoute = bottomNavController.currentBackStackEntry?.destination?.route
                items.forEach { item ->
                    val isSelected = currentRoute == item.route
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { 
                            Text(
                                item.title, 
                                fontSize = 12.sp, 
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            ) 
                        },
                        selected = isSelected,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFFFF6B00),
                            unselectedIconColor = Color(0xFF8E8E93),
                            selectedTextColor = Color(0xFFFF6B00),
                            unselectedTextColor = Color(0xFF8E8E93),
                            indicatorColor = Color(0xFFF5EAD6) // The soft background bubble
                        ),
                        onClick = {
                            bottomNavController.navigate(item.route) {
                                popUpTo(bottomNavController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = bottomNavController,
            startDestination = "vendor_home_screen",
            modifier = Modifier.padding(bottom = padding.calculateBottomPadding())
        ) {
            composable("vendor_home_screen") { VendorHomeScreen(navController, onLogout) }
            composable("vendor_products") { Text("Products Screen") }
            composable("vendor_orders") { Text("Orders Screen") }
            composable("vendor_stats") { Text("Stats Screen") }
            composable("vendor_profile") { VendorProfileScreen(navController, onLogout) }
        }
    }
}
