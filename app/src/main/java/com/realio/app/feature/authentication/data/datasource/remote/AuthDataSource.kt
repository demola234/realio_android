package com.realio.app.feature.authentication.data.datasource.remote

import AuthApiResponse
import com.realio.app.feature.authentication.data.model.response.LogoutResponse
import com.realio.app.feature.authentication.data.model.response.OtpResponse
import com.realio.app.feature.authentication.data.model.response.UploadImageResponse
import com.realio.app.feature.authentication.data.model.response.UserResponse
import java.io.File


interface AuthDataSource {
    suspend fun login(email: String, password: String): Result<AuthApiResponse>
    suspend fun register(name: String, email: String, password: String): Result<AuthApiResponse>
    suspend fun logout(token: String): Result<LogoutResponse>
    suspend fun verifyOtp(email: String, otp: String): Result<OtpResponse>
    suspend fun resendOtp(email: String): Result<OtpResponse>
    suspend fun oauthLogin(provider: String, token: String): Result<AuthApiResponse>
    suspend fun oauthRegister(provider: String, token: String, email: String): Result<AuthApiResponse>
    suspend fun getUserDetails(userId: String, token: String): Result<UserResponse>
    suspend fun uploadImage( token: String, userId: String, imageFile: File): Result<UploadImageResponse>
}