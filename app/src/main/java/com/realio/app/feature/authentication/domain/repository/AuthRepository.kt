package com.realio.app.feature.authentication.domain.repository

import com.realio.app.feature.authentication.data.model.response.OtpResponse
import com.realio.app.feature.authentication.data.model.response.UploadImageResponse
import com.realio.app.feature.authentication.domain.entity.User
import java.io.File

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(name: String, email: String, password: String): Result<User>
    suspend fun logout(): Result<Boolean>
    suspend fun verifyOtp(email: String, otp: String): Result<OtpResponse>
    suspend fun resendOtp(email: String): Result<Boolean>
    suspend fun oauthLogin(provider: String, token: String): Result<User>
    suspend fun oauthRegister(provider: String, token: String, email: String): Result<User>
    suspend fun getUserDetails(userId: String): Result<User>
    suspend fun uploadProfileImage(imageFile: File): Result<UploadImageResponse>
}