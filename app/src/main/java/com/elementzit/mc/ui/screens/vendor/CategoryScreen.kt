package com.elementzit.mc.ui.screens.vendor

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun CategoryScreen(
    navController: NavController,
    onBackClick: () -> Unit = {}
) {
    var showAddProductFlow by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    AnimatedContent(
        targetState = showAddProductFlow,
        transitionSpec = {
            fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
        },
        label = "CategoryFlow"
    ) { showFlow ->
        if (showFlow) {
            AddProductFlowScreen(
                navController = navController,
                category = selectedCategory
            )
        } else {
            CategorySelectionContent(
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it },
                onBackClick = onBackClick,
                onNextClick = { showAddProductFlow = true }
            )
        }
    }
}

@Composable
fun CategorySelectionContent(
    selectedCategory: String?,
    onCategorySelected: (String) -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    val categories = listOf(
        CategoryItem("Grocery", "🛒", listOf(Color(0xFF1ED760), Color(0xFF00C27A))),
        CategoryItem("Fashion", "👔", listOf(Color(0xFFC86BFF), Color(0xFFFF3D8E))),
        CategoryItem("Electronics", "📱", listOf(Color(0xFF5CA9FF), Color(0xFF00B4D8))),
        CategoryItem("Shoes", "👟", listOf(Color(0xFFFF8A00), Color(0xFFFF5E3A))),
        CategoryItem("Furniture", "🛋️", listOf(Color(0xFFFFC107), Color(0xFFFF8F00))),
        CategoryItem("Handmade", "🎨", listOf(Color(0xFFFF5AA5), Color(0xFFFF2D55))),
        CategoryItem("Beauty", "✨", listOf(Color(0xFFB16CFF), Color(0xFF8E44FF))),
        CategoryItem("Jewelry", "💍", listOf(Color(0xFFFFC107), Color(0xFFFFA000)))
    )

    val orangeColor = Color(0xFFFF6B00)

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF9F9F9))) {
        Column(modifier = Modifier
            .padding(16.dp)
            .padding(top = 50.dp)
            .padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFF9800),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) { Text("✨", fontSize = 20.sp) }
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            "Magician's Cart",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text("Vendor Panel", fontSize = 12.sp, color = Color.Gray, maxLines = 1)
                    }
                }
                Icon(Icons.Default.Notifications, null, tint = Color.Gray)
            }
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = "", onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search products, orders...") },
                leadingIcon = { Icon(Icons.Rounded.Search, null) },
                shape = RoundedCornerShape(24.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text("Welcome Back, Alwyn 👋", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
            Text(
                "Here's what's happening with your store today",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(categories.chunked(2)) { categoryPair ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    categoryPair.forEach { category ->
                        CategoryCard(
                            category = category,
                            modifier = Modifier.weight(1f),
                            isSelected = selectedCategory == category.title,
                            onCategorySelected = onCategorySelected
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F5F5))
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(18.dp)
                ) {

                    Button(
                        onClick = onBackClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(70.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE5E7EB)
                        )
                    ) {

                        Text(
                            text = "Back",
                            color = Color(0xFF374151),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = onNextClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(70.dp)
                            .shadow(
                                elevation = 12.dp,
                                shape = RoundedCornerShape(24.dp)
                            ),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = orangeColor
                        ),
                        enabled = selectedCategory != null
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = "Next",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = "›",
                                fontSize = 26.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryCard(
    category: CategoryItem,
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    onCategorySelected: (String) -> Unit
) {
    val orangeColor = Color(0xFFFF6B00)
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
            .border(
                width = 1.dp,
                color = if (isSelected) orangeColor else Color(0xFFE5E7EB),
                shape = RoundedCornerShape(28.dp)
            )
            .clickable { onCategorySelected(category.title) },
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) orangeColor.copy(alpha = 0.1f) else Color.White
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(108.dp)
                    .background(
                        brush = Brush.linearGradient(category.gradient),
                        shape = RoundedCornerShape(30.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = category.emoji,
                    fontSize = 46.sp
                )
            }
            Spacer(modifier = Modifier.height(22.dp))
            Text(
                text = category.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF111827)
            )
        }
    }
}
