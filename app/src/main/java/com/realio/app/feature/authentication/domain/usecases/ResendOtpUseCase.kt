package com.realio.app.feature.authentication.domain.usecases

import com.realio.app.core.exception.ValidationException
import com.realio.app.core.utils.isValidEmail
import com.realio.app.feature.authentication.domain.repository.AuthRepository

class ResendOtpUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String): Result<Boolean> {
        if (email.isBlank()) {
            return Result.failure(ValidationException("Email cannot be empty"))
        }
        if (!email.isValidEmail()) {
            return Result.failure(ValidationException("Invalid email format"))
        }

        return authRepository.resendOtp(email)
    }
}
