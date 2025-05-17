package com.realio.app.feature.authentication.domain.usecases

import android.util.Patterns
import com.realio.app.core.exception.ValidationException
import com.realio.app.core.utils.isValidEmail
import com.realio.app.feature.authentication.domain.repository.AuthRepository
import com.realio.app.feature.authentication.domain.entity.User

class LoginUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        // Validate input
        if (email.isBlank()) {
            return Result.failure(ValidationException("Email cannot be empty"))
        }
        if (!email.isValidEmail()) {
            return Result.failure(ValidationException("Invalid email format"))
        }
        if (password.isBlank()) {
            return Result.failure(ValidationException("Password cannot be empty"))
        }

        return authRepository.login(email, password)
    }
}

