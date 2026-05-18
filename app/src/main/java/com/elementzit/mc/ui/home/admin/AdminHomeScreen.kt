package com.elementzit.mc.ui.home.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.People
import androidx.compose.material.icons.rounded.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.elementzit.mc.domain.model.User
import com.elementzit.mc.domain.model.UserRole
import com.sds.marketplace.ui.home.components.MarketplaceTopAppBar
import com.sds.marketplace.ui.home.components.StatCard
import com.elementzit.mc.ui.viewmodel.AdminViewModel

/**
 * Screen displaying the Admin Home interface with system statistics and user list.
 */
@Composable
fun AdminHomeScreen(
    onLogout: () -> Unit,
    viewModel: AdminViewModel = hiltViewModel()
) {
    val users by viewModel.users.collectAsState()
    val pendingVendors by viewModel.pendingVendors.collectAsState()

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
                        value = users.size.toString(),
                        icon = Icons.Rounded.People,
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "Vendors",
                        value = users.count { it.role == UserRole.VENDOR }.toString(),
                        icon = Icons.Rounded.Storefront,
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                }
            }

            if (pendingVendors.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Pending Approvals",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                items(pendingVendors) { vendor ->
                    PendingVendorItem(
                        vendor = vendor,
                        onApprove = { viewModel.approveVendor(vendor.id) },
                        onReject = { viewModel.rejectVendor(vendor.id) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Recent Users",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            items(users.take(10)) { user ->
                UserListItem(user)
            }
        }
    }
}

@Composable
fun PendingVendorItem(
    vendor: User,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.2f))
    ) {
        ListItem(
            headlineContent = { Text(vendor.name, fontWeight = FontWeight.Bold) },
            supportingContent = { Text(vendor.email) },
            trailingContent = {
                Row {
                    IconButton(onClick = onApprove) {
                        Icon(Icons.Rounded.Check, contentDescription = "Approve", tint = Color(0xFF4CAF50))
                    }
                    IconButton(onClick = onReject) {
                        Icon(Icons.Rounded.Close, contentDescription = "Reject", tint = MaterialTheme.colorScheme.error)
                    }
                }
            },
            leadingContent = {
                Icon(Icons.Rounded.Storefront, contentDescription = null, modifier = Modifier.size(40.dp), tint = MaterialTheme.colorScheme.error)
            },
            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
        )
    }
}

/**
 * Composable for displaying a single user in a list.
 */
@Composable
fun UserListItem(user: User) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        ListItem(
            headlineContent = { Text(user.name, fontWeight = FontWeight.Medium) },
            supportingContent = { 
                Column {
                    Text(user.email)
                    if (user.role == UserRole.VENDOR) {
                        Text("Status: ${user.status}", style = MaterialTheme.typography.labelSmall, color = if(user.status == "PENDING") MaterialTheme.colorScheme.error else Color.Gray)
                    }
                }
            },
            trailingContent = {
                RoleTag(user.role)
            },
            leadingContent = {
                Icon(Icons.Rounded.AccountCircle, contentDescription = null, modifier = Modifier.size(40.dp))
            }
        )
    }
}

/**
 * Composable for displaying the role of a user as a tag.
 */
@Composable
fun RoleTag(role: UserRole) {
    Surface(
        shape = CircleShape,
        color = when(role) {
            UserRole.ADMIN -> MaterialTheme.colorScheme.errorContainer
            UserRole.VENDOR -> MaterialTheme.colorScheme.secondaryContainer
            UserRole.CUSTOMER -> MaterialTheme.colorScheme.primaryContainer
        },
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            role.name,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            color = when(role) {
                UserRole.ADMIN -> MaterialTheme.colorScheme.onErrorContainer
                UserRole.VENDOR -> MaterialTheme.colorScheme.onSecondaryContainer
                UserRole.CUSTOMER -> MaterialTheme.colorScheme.onPrimaryContainer
            }
        )
    }
}
