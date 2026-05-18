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

/**
 * ViewModel for managing user authentication state, including registration and login.
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    /**
     * Attempts to register a new user with the provided details.
     */
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
     * Attempts to log in a user with the provided credentials.
     */
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

    /**
     * Resets the authentication state to [AuthState.Idle].
     */
    fun resetState() {
        _authState.value = AuthState.Idle
    }
}

/**
 * Represents the possible states of the authentication process.
 */
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object RegisterSuccess : AuthState()
    data class LoginSuccess(val role: UserRole) : AuthState()
    data class Error(val message: String) : AuthState()
}
