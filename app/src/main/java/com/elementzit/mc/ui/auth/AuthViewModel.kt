package com.elementzit.mc.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elementzit.mc.domain.model.UserRole
import com.elementzit.mc.domain.usecase.auth.LoginUseCase
import com.elementzit.mc.domain.usecase.auth.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun register(name: String, email: String, role: UserRole, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = registerUseCase(name, email, password, role)
            result.onSuccess {
                _authState.value = AuthState.RegisterSuccess
            }.onFailure { e ->
                _authState.value = AuthState.Error(e.message ?: "Registration failed")
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = loginUseCase(email, password)
            result.onSuccess { user ->
                _authState.value = AuthState.LoginSuccess(user.role)
            }.onFailure { e ->
                _authState.value = AuthState.Error(e.message ?: "Login failed")
            }
        }
    }

    fun sendOtp(phone: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            delay(1000)
            _authState.value = AuthState.OtpSent
        }
    }

    fun verifyOtp(phone: String, email: String, otp: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            delay(1000)
            if (otp == "123456") {
                // Determine role based on email domain or hardcoded logic
                val role = when {
                    email.contains("admin") -> UserRole.ADMIN
                    email.contains("vendor") -> UserRole.VENDOR
                    else -> UserRole.CUSTOMER
                }
                _authState.value = AuthState.LoginSuccess(role)
            } else {
                _authState.value = AuthState.Error("Invalid OTP. Please try again.")
            }
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object RegisterSuccess : AuthState()
    object OtpSent : AuthState()
    data class LoginSuccess(val role: UserRole) : AuthState()
    data class Error(val message: String) : AuthState()
}
