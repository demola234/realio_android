package com.realio.app.feature.authentication.domain.usecases

import com.realio.app.core.exception.ValidationException
import com.realio.app.core.utils.isValidEmail
import com.realio.app.feature.authentication.domain.entity.User
import com.realio.app.feature.authentication.domain.repository.AuthRepository
import kotlin.Result.Companion.failure

class RegisterUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(name: String, email: String, password: String): Result<User> {
        // Validate input
        if (name.isBlank()) {
            return failure(ValidationException("Name cannot be empty"))
        }
        if (email.isBlank()) {
            return failure(ValidationException("Email cannot be empty"))
        }
        if (!email.isValidEmail()) {
            return failure(ValidationException("Invalid email format"))
        }
        if (password.isBlank()) {
            return failure(ValidationException("Password cannot be empty"))
        }
        if (password.length < 8) {
            return failure(ValidationException("Password must be at least 8 characters"))
        }

        return authRepository.register(name, email, password)
    }
}