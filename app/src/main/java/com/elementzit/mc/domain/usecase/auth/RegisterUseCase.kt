package com.elementzit.mc.domain.usecase.auth

import com.elementzit.mc.domain.model.UserRole
import com.elementzit.mc.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(name: String, email: String, password: String, role: UserRole): Result<Unit> {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            return Result.failure(Exception("All fields are required"))
        }
        if (repository.checkEmailExists(email)) {
            return Result.failure(Exception("User with this email already exists"))
        }
        return repository.register(name, email, password, role)
    }
}
