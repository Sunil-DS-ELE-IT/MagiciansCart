package com.elementzit.mc.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elementzit.mc.domain.model.UserRole
import com.elementzit.mc.domain.usecase.auth.LoginUseCase
import com.elementzit.mc.domain.usecase.auth.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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

    // Store the role from the first step to use in final success
    private var authenticatedRole: UserRole = UserRole.CUSTOMER

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

    /**
     * Step 1: Real Firebase authentication with Email and Password
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = loginUseCase(email, password)
            result.onSuccess { user ->
                // Authentication successful, now proceed to dummy OTP step
                authenticatedRole = user.role
                _authState.value = AuthState.OtpRequired
            }.onFailure { e ->
                _authState.value = AuthState.Error(e.message ?: "Invalid Email or Password")
            }
        }
    }

    /**
     * Step 2: Dummy OTP verification
     */
    fun verifyOtp(otp: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            // Dummy OTP validation (any non-empty input is accepted)
            if (otp.isNotEmpty()) {
                _authState.value = AuthState.LoginSuccess(authenticatedRole)
            } else {
                _authState.value = AuthState.Error("Invalid OTP")
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
    object OtpRequired : AuthState()
    data class LoginSuccess(val role: UserRole) : AuthState()
    data class Error(val message: String) : AuthState()
}
