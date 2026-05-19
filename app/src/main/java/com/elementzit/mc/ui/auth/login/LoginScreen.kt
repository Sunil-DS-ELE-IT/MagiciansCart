package com.elementzit.mc.ui.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Sms
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.elementzit.mc.domain.model.UserRole
import com.elementzit.mc.ui.auth.AuthState
import com.elementzit.mc.ui.auth.AuthViewModel

// Theme constants
private val Orange = Color(0xFFFF6B00)
private val CreamBg = Color(0xFFF8F5EF)
private val TextDark = Color(0xFF172033)

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: (UserRole) -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var emailOrPhone by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var otpValue by remember { mutableStateOf("") }
    
    var passwordVisible by remember { mutableStateOf(false) }
    var isOtpSent by remember { mutableStateOf(false) }

    var emailOrPhoneError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var otpError by remember { mutableStateOf<String?>(null) }

    val authState by viewModel.authState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthState.LoginSuccess -> {
                onLoginSuccess(state.role)
                viewModel.resetState()
            }
            is AuthState.OtpSent -> {
                isOtpSent = true
                snackbarHostState.showSnackbar("OTP sent successfully")
            }
            is AuthState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetState()
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = CreamBg
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Main content area that scrolls
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Section
                LogoSection()
                
                Spacer(modifier = Modifier.height(20.dp))

                // Toggle Section
                LoginToggle(onNavigateToRegister)

                Spacer(modifier = Modifier.height(20.dp))

                if (!isOtpSent) {
                    // Standard Login View
                    AuthLabel("Email Address")
                    CustomTextField(
                        value = emailOrPhone,
                        onValueChange = { 
                            emailOrPhone = it
                            emailOrPhoneError = null
                        },
                        placeholder = "vendor@example.com",
                        leadingIcon = Icons.Outlined.Email,
                        keyboardType = KeyboardType.Email,
                        isError = emailOrPhoneError != null
                    )
                    ErrorText(emailOrPhoneError)
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    AuthLabel("Phone Number")
                    CustomTextField(
                        value = phone,
                        onValueChange = { 
                            phone = it
                            phoneError = null 
                        },
                        placeholder = "+91 98765 43210",
                        leadingIcon = Icons.Outlined.Phone,
                        keyboardType = KeyboardType.Phone,
                        isError = phoneError != null
                    )
                    ErrorText(phoneError)

                    Spacer(modifier = Modifier.height(12.dp))

                    AuthLabel("Password")
                    CustomTextField(
                        value = password,
                        onValueChange = { 
                            password = it 
                            passwordError = null
                        },
                        placeholder = "••••••••",
                        leadingIcon = Icons.Outlined.Lock,
                        keyboardType = KeyboardType.Password,
                        isError = passwordError != null,
                        isPassword = true,
                        passwordVisible = passwordVisible,
                        onVisibilityChange = { passwordVisible = !passwordVisible }
                    )
                    ErrorText(passwordError)
                } else {
                    // OTP Verification View
                    AuthLabel("Verify OTP")
                    CustomTextField(
                        value = otpValue,
                        onValueChange = { if (it.length <= 6) otpValue = it },
                        placeholder = "123456",
                        leadingIcon = Icons.Outlined.Sms,
                        keyboardType = KeyboardType.NumberPassword,
                        isError = otpError != null
                    )
                    ErrorText(otpError)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action Button
                Button(
                    onClick = {
                        if (!isOtpSent) {
                            var isValid = true
                            if (emailOrPhone.isBlank() || !emailOrPhone.contains("@")) {
                                emailOrPhoneError = "Invalid email"
                                isValid = false
                            }
                            if (phone.length < 10) {
                                phoneError = "Invalid phone number"
                                isValid = false
                            }
                            if (password.isBlank()) {
                                passwordError = "Password cannot be empty"
                                isValid = false
                            }
                            
                            // IF ALL FIELDS ARE FILLED PROPERLY, VERIFY PASSWORD WITH FIREBASE
                            if (isValid) {
                                viewModel.loginAndSendOtp(emailOrPhone, password)
                            }
                        } else {
                            if (otpValue.length < 6) {
                                otpError = "Enter 6-digit OTP"
                            } else {
                                viewModel.verifyOtp(otpValue)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Orange),
                    shape = RoundedCornerShape(16.dp),
                    enabled = authState !is AuthState.Loading
                ) {
                    if (authState is AuthState.Loading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                    } else {
                        Text(
                            text = if (!isOtpSent) "Continue with OTP" else "Verify & Login",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            // Footer Info pinned to the bottom
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("✔ Secure vendor authentication", color = Color.Gray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                val termsText = buildAnnotatedString {
                    append("By continuing, you agree to our ")
                    withStyle(style = SpanStyle(color = Orange, fontWeight = FontWeight.Bold)) {
                        append("Terms of Service")
                    }
                }
                Text(text = termsText, color = Color.Gray, fontSize = 12.sp, textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
private fun LogoSection() {
    Surface(modifier = Modifier.size(72.dp), shape = RoundedCornerShape(20.dp), color = Orange, shadowElevation = 4.dp) {
        Box(contentAlignment = Alignment.Center) {
            Text("✦", color = Color.Yellow, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
    Text("Magician's Cart", color = TextDark, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
    Text("Vendor Management Portal", color = Color.Gray, fontSize = 12.sp)
}

@Composable
private fun LoginToggle(onNavigateToRegister: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .background(Color(0xFFECECEC), RoundedCornerShape(50.dp))
            .padding(4.dp)
    ) {
        Box(modifier = Modifier.weight(1f).fillMaxHeight().background(Orange, RoundedCornerShape(50.dp)), contentAlignment = Alignment.Center) {
            Text("Login", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
        Box(modifier = Modifier.weight(1f).fillMaxHeight().clickable { onNavigateToRegister() }, contentAlignment = Alignment.Center) {
            Text("Create Account", color = Color.Gray, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}

@Composable
private fun ErrorText(text: String?) {
    text?.let {
        Spacer(Modifier.height(2.dp))
        Text(it, color = MaterialTheme.colorScheme.error, fontSize = 10.sp, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun AuthLabel(text: String) {
    Text(text, modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp), color = TextDark, fontSize = 14.sp, fontWeight = FontWeight.Bold)
}

@Composable
private fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    keyboardType: KeyboardType,
    isError: Boolean = false,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onVisibilityChange: () -> Unit = {}
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth().height(60.dp).shadow(2.dp, RoundedCornerShape(16.dp)),
        placeholder = { Text(placeholder, color = Color.Gray, fontSize = 14.sp) },
        leadingIcon = { Icon(leadingIcon, null, tint = Color.Gray, modifier = Modifier.size(20.dp)) },
        trailingIcon = if (isPassword) {
            { IconButton(onClick = onVisibilityChange) { Icon(if (passwordVisible) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff, null, tint = Color.Gray, modifier = Modifier.size(20.dp)) } }
        } else null,
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        shape = RoundedCornerShape(16.dp),
        isError = isError,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = Orange,
            unfocusedBorderColor = Color(0xFFE3E3E3)
        )
    )
}
