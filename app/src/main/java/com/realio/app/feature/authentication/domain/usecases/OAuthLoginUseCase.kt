package com.realio.app.feature.authentication.domain.usecases

import com.realio.app.core.exception.ValidationException
import com.realio.app.feature.authentication.domain.entity.User
import com.realio.app.feature.authentication.domain.repository.AuthRepository

class OAuthLoginUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(provider: String, token: String): Result<User> {
        if (provider.isBlank()) {
            return Result.failure(ValidationException("Provider cannot be empty"))
        }
        if (token.isBlank()) {
            return Result.failure(ValidationException("Token cannot be empty"))
        }

        return authRepository.oauthLogin(provider, token)
    }
}