package com.elementzit.mc.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.elementzit.mc.domain.model.UserRole
import com.elementzit.mc.ui.screens.SplashScreen
import com.elementzit.mc.ui.auth.login.LoginScreen
import com.sds.marketplace.ui.auth.register.RegisterScreen
import com.elementzit.mc.ui.home.admin.AdminHomeScreen
import com.elementzit.mc.ui.home.customer.CustomerHomeScreen

import com.sds.marketplace.ui.home.vendor.VendorNavigationWrapper
import com.sds.marketplace.ui.home.vendor.VendorSalesChartScreen
import com.elementzit.mc.ui.screens.vendor.AddProductScreen
import com.elementzit.mc.ui.screens.vendor.AddProductDetailsScreen
import com.elementzit.mc.ui.screens.vendor.EditProductScreen
import com.elementzit.mc.ui.screens.vendor.EditVendorProfileScreen
import com.elementzit.mc.ui.screens.customer.CheckoutScreen
import com.elementzit.mc.ui.screens.customer.AddAddressScreen
import com.elementzit.mc.ui.screens.customer.OrderSuccessScreen
import com.elementzit.mc.ui.screens.customer.TrackOrderScreen
import com.elementzit.mc.ui.screens.vendor.AddProductMoreDetailsScreen
import com.elementzit.mc.ui.screens.vendor.AddProductPriceQtyScreen
import com.elementzit.mc.ui.screens.vendor.AddProductFlowScreen
import com.elementzit.mc.ui.screens.vendor.CategoryScreen
import com.elementzit.mc.ui.viewmodel.VendorProductViewModel

@Composable
fun MarketplaceNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "splash",
        modifier = modifier
    ) {
        composable("splash") {
            SplashScreen(onSplashFinished = {
                navController.navigate("login") {
                    popUpTo("splash") { inclusive = true }
                }
            })
        }
        composable("login") {
            LoginScreen(
                onLoginSuccess = { role ->
                    val destination = when (role) {
                        UserRole.ADMIN -> "admin_home"
                        UserRole.VENDOR -> "vendor_home"
                        UserRole.CUSTOMER -> "customer_home"
                    }
                    navController.navigate(destination) {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate("login")
                }
            )
        }
        composable("admin_home") {
            AdminHomeScreen(onLogout = {
                navController.navigate("login") {
                    popUpTo(0)
                }
            })
        }
        composable("customer_home") {
            CustomerHomeScreen(
                navController = navController,
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0)
                    }
                }
            )
        }
        composable("vendor_home") {
            VendorNavigationWrapper(
                navController = navController,
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0)
                    }
                }
            )
        }
        composable(
            route = "add_product_screen/{category}",
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category")
            AddProductScreen(navController = navController, category = category)
        }
        composable("restock_now") {
            AddProductScreen(navController = navController)
        }
        // Change nav_category to point to the new single-step flow screen
        composable("nav_category") {
            // CategoryScreen(navController = navController)
            AddProductFlowScreen(navController = navController)
        }
        composable("edit_vendor_profile") {
            EditVendorProfileScreen(navController = navController)
        }
        composable("vendor_sales_chart") {
            VendorSalesChartScreen(navController = navController)
        }

        composable(
            route = "add_product_details/{selectedCategory}",
            arguments = listOf(navArgument("selectedCategory") { type = NavType.StringType })
        ) { backStackEntry ->
            val selectedCategory = backStackEntry.arguments?.getString("selectedCategory")
            AddProductDetailsScreen(navController = navController, selectedCategory = selectedCategory)
        }

        composable("add_product_more_details") {
            AddProductMoreDetailsScreen(navController = navController)
        }

        composable("add_product_price_qty") {
            // No longer used in flow but kept for safety if referenced elsewhere
            AddProductPriceQtyScreen(navController = navController)
        }

        composable(
            "product_list/{category}",
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category")
        }
        composable(
            "edit_product/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) {
            val productId = it.arguments?.getString("productId")
            val viewModel: VendorProductViewModel = hiltViewModel()
            val products by viewModel.products.collectAsState(initial = emptyList())
            val product = products.find { it.id == productId }

            if (product != null) {
                EditProductScreen(
                    navController = navController,
                    product = product
                )
            } else {
                Text("Product not found")
            }
        }
        composable("checkout") {
            CheckoutScreen(navController = navController)
        }
        composable("add_address") {
            AddAddressScreen(navController = navController)
        }
        composable(
            "order_success/{orderId}/{totalAmount}",
            arguments = listOf(
                navArgument("orderId") { type = NavType.StringType },
                navArgument("totalAmount") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            val totalAmount = backStackEntry.arguments?.getString("totalAmount") ?: ""
            OrderSuccessScreen(navController = navController, orderId = orderId, totalAmount = totalAmount)
        }
        composable(
            "track_order/{orderId}",
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            TrackOrderScreen(navController = navController, orderId = orderId)
        }
    }
}
