package com.sds.marketplace.ui.home.vendor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.elementzit.mc.ui.viewmodel.VendorProductViewModel

@Composable
fun VendorProfileScreen(
    navController: NavController,
    onLogout: () -> Unit,
    viewModel: VendorProductViewModel = hiltViewModel()
) {
    val profile by viewModel.vendorProfile.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5))) {
        // Header
        Column(
            modifier = Modifier.fillMaxWidth().background(Color(0xFFFF5722)).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(modifier = Modifier.size(80.dp), shape = CircleShape, color = Color.White) { }
            Spacer(modifier = Modifier.height(8.dp))
            Text(profile?.get("shopName")?.toString() ?: "Shop Name", color = Color.White, style = MaterialTheme.typography.headlineSmall)
            Text("Vendor Account", color = Color.White.copy(alpha = 0.8f))
        }

        // Menu Items
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ProfileMenuItem("Edit Profile", Icons.Default.Person) { navController.navigate("edit_vendor_profile") }
            ProfileMenuItem("Shop Address", Icons.Default.LocationOn) { }
            ProfileMenuItem("Settings", Icons.Default.Settings) { }
            ProfileMenuItem("Help & Support", Icons.AutoMirrored.Filled.Help) { }
            ProfileMenuItem("Logout", Icons.AutoMirrored.Filled.ExitToApp, Color.Red) { onLogout() }
        }
    }
}

@Composable
fun ProfileMenuItem(title: String, icon: ImageVector, tint: Color = Color.Unspecified, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = tint)
            Spacer(modifier = Modifier.width(16.dp))
            Text(title, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
            Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Gray)
        }
    }
}
