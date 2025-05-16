package com.realio.app.feature.authentication.domain.usecases

import com.realio.app.core.exception.ValidationException
import com.realio.app.feature.authentication.domain.entity.User
import com.realio.app.feature.authentication.domain.repository.AuthRepository

class GetUserDetailsUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(userId: String): Result<User> {
        if (userId.isBlank()) {
            return Result.failure(ValidationException("User ID cannot be empty"))
        }

        return authRepository.getUserDetails(userId)
    }
}