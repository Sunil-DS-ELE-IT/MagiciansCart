package com.elementzit.mc.ui.screens.vendor

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.elementzit.mc.domain.model.Product
import com.elementzit.mc.ui.components.FormLabel
import com.elementzit.mc.ui.components.RoundedTextField
import com.elementzit.mc.ui.components.TagChip
import com.elementzit.mc.ui.viewmodel.AddProductViewModel

// CategoryItem definition removed to use the one from CategoryItem.kt

enum class AddProductStep {
    SELECT_CATEGORY,
    DETAILS,
    MORE_DETAILS,
    PRICE_QTY_IMAGE
}

@Composable
fun AddProductFlowScreen(
    navController: NavController,
    viewModel: AddProductViewModel = hiltViewModel(),
    category: String? = null,
    productId: String? = null
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val selectedImageUris by viewModel.selectedImageUris.collectAsState()

    var currentStep by remember { mutableStateOf(if (category == null) AddProductStep.SELECT_CATEGORY else AddProductStep.DETAILS) }
    var selectedCategory by remember { mutableStateOf(category ?: "") }

    var productName by remember { mutableStateOf("") }
    var productDescription by remember { mutableStateOf("") }
    var brandName by remember { mutableStateOf("") }
    var sku by remember { mutableStateOf("") }
    var productTags by remember { mutableStateOf(listOf<String>()) }
    var productSize by remember { mutableStateOf("") }
    var productColor by remember { mutableStateOf("") }
    var productMaterial by remember { mutableStateOf("") }
    var productWeight by remember { mutableStateOf("") }
    var productDimension by remember { mutableStateOf("") }
    var productPrice by remember { mutableStateOf("") }
    var productPriceDiscount by remember { mutableStateOf("") }
    var productQuantityAvailable by remember { mutableStateOf("") }
    var productMinQuantity by remember { mutableStateOf("") }
    var showImageUpload by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        viewModel.onImagesSelected(uris.map { it.toString() })
    }

    fun validateStep(step: AddProductStep): String? {
        return when (step) {
            AddProductStep.SELECT_CATEGORY -> if (selectedCategory.isBlank()) "Please select a category" else null
            AddProductStep.DETAILS -> when {
                productName.isBlank() -> "Product name is required"
                productDescription.isBlank() -> "Description is required"
                else -> null
            }
            AddProductStep.MORE_DETAILS -> null
            AddProductStep.PRICE_QTY_IMAGE -> when {
                productPrice.toDoubleOrNull() == null || productPrice.toDouble() <= 0 -> "Valid price is required"
                productQuantityAvailable.toIntOrNull() == null -> "Valid quantity is required"
                selectedImageUris.isEmpty() && showImageUpload -> "At least one product image is required"
                else -> null
            }
        }
    }

    LaunchedEffect(uiState) {
        if (uiState.isSuccess) {
            Toast.makeText(context, uiState.message ?: "Success", Toast.LENGTH_SHORT).show()
            viewModel.resetState()
            navController.popBackStack()
        } else if (uiState.error != null) {
            Toast.makeText(context, uiState.error, Toast.LENGTH_LONG).show()
            viewModel.resetState()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = currentStep,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
            },
            label = "AddProductFlow"
        ) { step ->
            when (step) {
                AddProductStep.SELECT_CATEGORY -> {
                    CategorySelectionView(
                        selectedCategory = selectedCategory,
                        onCategorySelected = { selectedCategory = it },
                        onBackClick = { navController.popBackStack() },
                        onNextClick = {
                            val error = validateStep(AddProductStep.SELECT_CATEGORY)
                            if (error == null) currentStep = AddProductStep.DETAILS else Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
                AddProductStep.DETAILS -> {
                    ProductDetailsStepView(
                        productName = productName,
                        onProductNameChange = { productName = it },
                        productDescription = productDescription,
                        onProductDescriptionChange = { productDescription = it },
                        brandName = brandName,
                        onBrandNameChange = { brandName = it },
                        sku = sku,
                        onSkuChange = { sku = it },
                        productTags = productTags,
                        onProductTagsChange = { productTags = it },
                        onNext = {
                            val error = validateStep(AddProductStep.DETAILS)
                            if (error == null) currentStep = AddProductStep.MORE_DETAILS else Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        },
                        onBack = { if (category == null) currentStep = AddProductStep.SELECT_CATEGORY else navController.popBackStack() }
                    )
                }
                AddProductStep.MORE_DETAILS -> {
                    MoreDetailsStepView(
                        productSize = productSize,
                        onProductSizeChange = { productSize = it },
                        productColor = productColor,
                        onProductColorChange = { productColor = it },
                        productMaterial = productMaterial,
                        onProductMaterialChange = { productMaterial = it },
                        productWeight = productWeight,
                        onProductWeightChange = { productWeight = it },
                        productDimension = productDimension,
                        onProductDimensionChange = { productDimension = it },
                        onNext = { currentStep = AddProductStep.PRICE_QTY_IMAGE },
                        onBack = { currentStep = AddProductStep.DETAILS }
                    )
                }
                AddProductStep.PRICE_QTY_IMAGE -> {
                    PriceQtyImageStepView(
                        productPrice = productPrice,
                        onProductPriceChange = { productPrice = it },
                        productPriceDiscount = productPriceDiscount,
                        onProductPriceDiscountChange = { productPriceDiscount = it },
                        productQuantityAvailable = productQuantityAvailable,
                        onProductQuantityAvailableChange = { productQuantityAvailable = it },
                        productMinQuantity = productMinQuantity,
                        onProductMinQuantityChange = { productMinQuantity = it },
                        selectedImageUris = selectedImageUris,
                        onImageSelectClick = { imagePickerLauncher.launch("image/*") },
                        onRemoveImage = { viewModel.removeImage(it) },
                        showImageUpload = showImageUpload,
                        onBack = {
                            if (showImageUpload) {
                                showImageUpload = false
                            } else {
                                currentStep = AddProductStep.MORE_DETAILS
                            }
                        },
                        onSubmit = {
                            val error = validateStep(AddProductStep.PRICE_QTY_IMAGE)
                            if (error == null) {
                                if (!showImageUpload) {
                                    showImageUpload = true
                                } else {
                                    viewModel.saveProduct(Product(
                                        id = productId ?: "",
                                        name = productName,
                                        description = productDescription,
                                        brand = brandName,
                                        sku = sku,
                                        price = productPrice.toDoubleOrNull() ?: 0.0,
                                        category = selectedCategory,
                                        stock = productQuantityAvailable.toIntOrNull() ?: 0,
                                        tags = productTags
                                    ), productId != null)
                                }
                            } else Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFFFF9800))
            }
        }
    }
}

@Composable
fun CategorySelectionView(
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

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF9F9F9)).padding(bottom = 20.dp)) {
        HeaderSection()
        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).weight(1f),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(categories.chunked(2)) { categoryPair ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    categoryPair.forEach { category ->
                        CategoryFlowCard(
                            category = category,
                            modifier = Modifier.weight(1f),
                            isSelected = selectedCategory == category.title,
                            onCategorySelected = onCategorySelected
                        )
                    }
                }
            }
        }
        StepBottomButtons(
            onBack = onBackClick,
            onNext = onNextClick,
            isNextEnabled = selectedCategory?.isNotBlank() == true
        )
    }
}

@Composable
fun CategoryFlowCard(
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
            .border(width = 1.dp, color = if (isSelected) orangeColor else Color(0xFFE5E7EB), shape = RoundedCornerShape(28.dp))
            .clickable { onCategorySelected(category.title) },
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = if (isSelected) orangeColor.copy(alpha = 0.1f) else Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Box(modifier = Modifier.size(108.dp).background(brush = Brush.linearGradient(category.gradient), shape = RoundedCornerShape(30.dp)), contentAlignment = Alignment.Center) {
                Text(text = category.emoji, fontSize = 46.sp)
            }
            Spacer(modifier = Modifier.height(22.dp))
            Text(text = category.title, fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF111827))
        }
    }
}

@Composable
private fun HeaderSection() {
    val orangeColor = Color(0xFFFF9800)
    Column(modifier = Modifier.padding(16.dp).padding(top = 50.dp).padding(20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = RoundedCornerShape(12.dp), color = orangeColor, modifier = Modifier.size(40.dp)) {
                    Box(contentAlignment = Alignment.Center) { Text("✨", fontSize = 20.sp) }
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Magician's Cart", fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
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
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color(0xFFF5F5F5), unfocusedContainerColor = Color(0xFFF5F5F5), focusedBorderColor = Color.Transparent, unfocusedBorderColor = Color.Transparent)
        )
        Spacer(Modifier.height(16.dp))
        Text("Welcome Back, Alwyn 👋", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
        Text("Here's what's happening with your store today", color = Color.Gray, fontSize = 14.sp)
    }
}

@Composable
private fun StepBottomButtons(onBack: () -> Unit, onNext: () -> Unit, nextText: String = "Next", isNextEnabled: Boolean = true) {
    val orangeColor = Color(0xFFFF9800)
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 20.dp).padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Button(
            onClick = onBack,
            modifier = Modifier.weight(1f).height(70.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE5E7EB))
        ) {
            Text("Back", color = Color(0xFF374151), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Button(
            onClick = onNext,
            modifier = Modifier.weight(1f).height(70.dp).shadow(elevation = 12.dp, shape = RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = orangeColor),
            enabled = isNextEnabled
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(nextText, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(10.dp))
                Text("›", fontSize = 26.sp)
            }
        }
    }
}

@Composable
fun ProductDetailsStepView(
    productName: String, onProductNameChange: (String) -> Unit,
    productDescription: String, onProductDescriptionChange: (String) -> Unit,
    brandName: String, onBrandNameChange: (String) -> Unit,
    sku: String, onSkuChange: (String) -> Unit,
    productTags: List<String>, onProductTagsChange: (List<String>) -> Unit,
    onNext: () -> Unit, onBack: () -> Unit
) {
    val scrollState = rememberScrollState()
    var newTagInput by remember { mutableStateOf("") }
    val orangeColor = Color(0xFFFF9800)

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF9F9F9)).padding(bottom = 50.dp)) {
            HeaderSection()
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).weight(1f).verticalScroll(scrollState)) {
                FormLabel("Product Name *")
                Spacer(modifier = Modifier.height(12.dp))
                RoundedTextField(value = productName, onValueChange = onProductNameChange, placeholder = "e.g., Handcrafted Wooden Bowl")
                Spacer(modifier = Modifier.height(8.dp))
                FormLabel("Product Description *")
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = productDescription, onValueChange = onProductDescriptionChange,
                    modifier = Modifier.fillMaxWidth().height(75.dp),
                    placeholder = { Text("Describe your product in detail...", color = Color.Gray) },
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color(0xFFF5F5F5), unfocusedContainerColor = Color(0xFFF5F5F5), focusedBorderColor = Color.Transparent, unfocusedBorderColor = Color.Transparent)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(top=8.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        FormLabel("Brand Name")
                        Spacer(modifier = Modifier.height(12.dp))
                        RoundedTextField(value = brandName, onValueChange = onBrandNameChange, placeholder = "Brand")
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        FormLabel("SKU")
                        Spacer(modifier = Modifier.height(12.dp))
                        RoundedTextField(value = sku, onValueChange = onSkuChange, placeholder = "SKU123")
                    }
                }
                FormLabel("Product Tags")
                Spacer(modifier = Modifier.height(16.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(productTags) { tag ->
                        TagChip(tag) { tagToRemove -> onProductTagsChange(productTags.filter { it != tagToRemove }) }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    RoundedTextField(value = newTagInput, onValueChange = { newTagInput = it }, placeholder = "Add a new tag", modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (newTagInput.isNotBlank() && !productTags.contains(newTagInput.trim())) {
                                onProductTagsChange(productTags + newTagInput.trim())
                                newTagInput = ""
                            }
                        },
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = orangeColor),
                        modifier = Modifier.height(50.dp)
                    ) {
                        Text("Add", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
            StepBottomButtons(onBack = onBack, onNext = onNext)
        }
    }
}

@Composable
fun MoreDetailsStepView(
    productSize: String, onProductSizeChange: (String) -> Unit,
    productColor: String, onProductColorChange: (String) -> Unit,
    productMaterial: String, onProductMaterialChange: (String) -> Unit,
    productWeight: String, onProductWeightChange: (String) -> Unit,
    productDimension: String, onProductDimensionChange: (String) -> Unit,
    onNext: () -> Unit, onBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF9F9F9)).padding(bottom = 50.dp)) {
            HeaderSection()
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).weight(1f).verticalScroll(scrollState)) {
                FormLabel("Size")
                Spacer(modifier = Modifier.height(12.dp))
                RoundedTextField(value = productSize, onValueChange = onProductSizeChange, placeholder = "Enter Size")
                Spacer(modifier = Modifier.height(8.dp))
                FormLabel("Color")
                Spacer(modifier = Modifier.height(12.dp))
                RoundedTextField(value = productColor, onValueChange = onProductColorChange, placeholder = "Enter color")
                Spacer(modifier = Modifier.height(8.dp))
                FormLabel("Material")
                Spacer(modifier = Modifier.height(12.dp))
                RoundedTextField(value = productMaterial, onValueChange = onProductMaterialChange, placeholder = "Enter Material")
                Spacer(modifier = Modifier.height(8.dp))
                FormLabel("Weight")
                Spacer(modifier = Modifier.height(12.dp))
                RoundedTextField(value = productWeight, onValueChange = onProductWeightChange, placeholder = "Enter Weight")
                Spacer(modifier = Modifier.height(8.dp))
                FormLabel("Dimensions")
                Spacer(modifier = Modifier.height(12.dp))
                RoundedTextField(value = productDimension, onValueChange = onProductDimensionChange, placeholder = "Enter Dimensions")
                Spacer(modifier = Modifier.height(8.dp))
            }
            StepBottomButtons(onBack = onBack, onNext = onNext)
        }
    }
}

@Composable
fun PriceQtyImageStepView(
    productPrice: String, onProductPriceChange: (String) -> Unit,
    productPriceDiscount: String, onProductPriceDiscountChange: (String) -> Unit,
    productQuantityAvailable: String, onProductQuantityAvailableChange: (String) -> Unit,
    productMinQuantity: String, onProductMinQuantityChange: (String) -> Unit,
    selectedImageUris: List<String>,
    onImageSelectClick: () -> Unit,
    onRemoveImage: (String) -> Unit,
    showImageUpload: Boolean,
    onBack: () -> Unit, onSubmit: () -> Unit
) {
    val scrollState = rememberScrollState()
    val orangeColor = Color(0xFFFF9800)

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF9F9F9)).padding(bottom = 50.dp)) {
            HeaderSection()
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).weight(1f).verticalScroll(scrollState)) {
                if (!showImageUpload) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Column(modifier = Modifier.weight(1f)) {
                            FormLabel("Price *")
                            Spacer(modifier = Modifier.height(12.dp))
                            RoundedTextField(value = productPrice, onValueChange = onProductPriceChange, placeholder = "10")
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            FormLabel("Discount Price")
                            Spacer(modifier = Modifier.height(12.dp))
                            RoundedTextField(value = productPriceDiscount, onValueChange = onProductPriceDiscountChange, placeholder = "0")
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    FormLabel("Quantity Available *")
                    Spacer(modifier = Modifier.height(12.dp))
                    RoundedTextField(value = productQuantityAvailable, onValueChange = onProductQuantityAvailableChange, placeholder = "Enter Quantity")
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
                    Spacer(modifier = Modifier.height(16.dp))
                    FormLabel("Minimum Order Quantity")
                    Spacer(modifier = Modifier.height(12.dp))
                    RoundedTextField(value = productMinQuantity, onValueChange = onProductMinQuantityChange, placeholder = "1")
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .border(2.dp, Color.LightGray, RoundedCornerShape(24.dp))
                            .background(Color.Transparent, RoundedCornerShape(24.dp))
                            .clickable { onImageSelectClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.padding(vertical = 24.dp)) {
                            Surface(shape = RoundedCornerShape(12.dp), color = orangeColor, modifier = Modifier.size(60.dp)) {
                                Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.Upload, "Upload Icon", tint = Color.White, modifier = Modifier.size(30.dp)) }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Upload Product Images", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF374151))
                            Text("Select one or more images", fontSize = 12.sp, color = Color.Gray)
                        }
                    }

                    if (selectedImageUris.isNotEmpty()) {
                        FormLabel("Selected Images")
                        Spacer(Modifier.height(8.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp)
                        ) {
                            items(selectedImageUris) { uri ->
                                Box(modifier = Modifier.size(100.dp)) {
                                    AsyncImage(
                                        model = uri,
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(12.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                    IconButton(
                                        onClick = { onRemoveImage(uri) },
                                        modifier = Modifier.align(Alignment.TopEnd).size(24.dp).background(Color.Black.copy(alpha = 0.5f), CircleShape)
                                    ) {
                                        Icon(Icons.Default.Close, contentDescription = "Remove", tint = Color.White, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
            StepBottomButtons(
                onBack = onBack,
                onNext = onSubmit,
                nextText = if(showImageUpload) "Submit" else "Next",
                isNextEnabled = if(showImageUpload) selectedImageUris.isNotEmpty() else true
            )
        }
    }
}
