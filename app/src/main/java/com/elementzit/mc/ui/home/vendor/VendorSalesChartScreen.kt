package com.sds.marketplace.ui.home.vendor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendorSalesChartScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sales Summary Chart", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Monthly Sales Performance",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Dummy Chart View
            Card(
                modifier = Modifier.fillMaxWidth().height(250.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.2f))
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = androidx.compose.ui.Alignment.Start
                ) {
                    Text("Sales Data (Dummy)", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    // Representing bars with simple colored boxes
                    Row(modifier = Modifier.fillMaxWidth().height(150.dp), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = androidx.compose.ui.Alignment.Bottom) {
                        // Dummy data for chart bars
                        val salesData = listOf(0.6f, 0.8f, 0.4f, 0.9f, 0.7f, 0.5f)
                        val colors = listOf(Color(0xFF4CAF50), Color(0xFF2196F3), Color(0xFFFFC107), Color(0xFFE91E63), Color(0xFF00BCD4), Color(0xFF8BC34A))

                        salesData.forEachIndexed { index, heightFactor ->
                            Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                                Text(
                                    text = "$" + (heightFactor * 1000).toInt(),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .width(30.dp)
                                        .fillMaxHeight(heightFactor)
                                        .background(colors[index % colors.size], RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                )
                            }
                        }
                    }
                }
            }

            Text(
                text = "Total Sales: $1450.00 (Dummy)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Average Order Value: $25.00 (Dummy)",
                style = MaterialTheme.typography.titleSmall,
                color = Color.Gray
            )
        }
    }
}
