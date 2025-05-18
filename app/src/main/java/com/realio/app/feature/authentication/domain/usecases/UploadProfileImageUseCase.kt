package com.realio.app.feature.authentication.domain.usecases

import com.realio.app.core.exception.ValidationException
import com.realio.app.feature.authentication.data.model.response.UploadImageResponse
import com.realio.app.feature.authentication.domain.repository.AuthRepository
import java.io.File

class UploadProfileImageUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(image: File): Result<UploadImageResponse> {
        if (image.path.isBlank()) {
            return Result.failure(ValidationException("Image path cannot be empty"))
        }
        return authRepository.uploadProfileImage(image)
    }
}

