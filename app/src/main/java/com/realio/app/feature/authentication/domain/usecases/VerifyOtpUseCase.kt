package com.realio.app.feature.authentication.domain.usecases

import com.realio.app.core.exception.ValidationException
import com.realio.app.feature.authentication.data.model.response.OtpResponse
import com.realio.app.feature.authentication.domain.entity.User
import com.realio.app.feature.authentication.domain.repository.AuthRepository

class VerifyOtpUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, otp: String): Result<OtpResponse> {
        if (email.isBlank()) {
            return Result.failure(ValidationException("Email cannot be empty"))
        }
        if (otp.isBlank()) {
            return Result.failure(ValidationException("OTP cannot be empty"))
        }
        if (otp.length != 6 || !otp.all { it.isDigit() }) {
            return Result.failure(ValidationException("Invalid OTP format"))
        }

        return authRepository.verifyOtp(email, otp)
    }
}