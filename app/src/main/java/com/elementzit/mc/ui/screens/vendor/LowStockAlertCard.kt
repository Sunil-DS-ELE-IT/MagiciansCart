package com.elementzit.mc.ui.screens.vendor

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.weight

@Composable
fun LowStockAlertCard(
    modifier: Modifier = Modifier,
    onRestockClick: () -> Unit = {}
) {
    val orangeColor = Color(0xFFFF6B00)
    val backgroundColor = Color(0xFFFFFCF5)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp), // Matched padding with DetailedProductItem
        shape = RoundedCornerShape(20.dp), // Consistent with other cards
        border = BorderStroke(1.dp, Color(0xFFFFD3A6)),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically // Better alignment for tighter layout
        ) {
            // Smaller Icon Box
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        orangeColor,
                        androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.WarningAmber,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Low Stock Alert",
                    fontSize = 16.sp, // Reduced to match headers
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
                Text(
                    text = "5 products running low",
                    fontSize = 12.sp, // Reduced to match subtext
                    color = Color(0xFF4B5563)
                )
            }

            TextButton(
                onClick = onRestockClick,
                colors = ButtonDefaults.textButtonColors(contentColor = orangeColor)
            ) {
                Text("Restock", fontWeight = FontWeight.Bold)
            }
        }
    }
}