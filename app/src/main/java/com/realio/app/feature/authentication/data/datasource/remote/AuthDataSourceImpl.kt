package com.realio.app.feature.authentication.data.datasource.remote

import com.realio.app.core.exception.ApiException
import com.realio.app.feature.authentication.data.api.AuthApi
import com.realio.app.feature.authentication.data.model.request.LoginRequest
import com.realio.app.feature.authentication.data.model.request.OAuthLoginRequest
import com.realio.app.feature.authentication.data.model.request.RegisterRequest
import com.realio.app.feature.authentication.data.model.request.ResendOtpRequest
import com.realio.app.feature.authentication.data.model.request.VerifyRequest
import com.realio.app.feature.authentication.data.model.response.AuthResponse
import com.realio.app.feature.authentication.data.model.response.LogoutResponse
import com.realio.app.feature.authentication.data.model.response.OtpResponse
import com.realio.app.feature.authentication.data.model.response.UserResponse

class AuthDataSourceImpl(private val authApi: AuthApi) : AuthDataSource {
    override suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = authApi.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                Result.failure(ApiException(response.code(), errorBody))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(name: String, email: String, password: String): Result<AuthResponse> {
        return try {
            val response = authApi.register(RegisterRequest(name, email, password))
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                Result.failure(ApiException(response.code(), errorBody))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun logout(token: String): Result<LogoutResponse> {
        return try {
            val response = authApi.logout(token = token)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                Result.failure(ApiException(response.code(), errorBody))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun verifyOtp(
        email: String,
        otp: String
    ): Result<AuthResponse> {
        return try {
            val response = authApi.verifyOtp(VerifyRequest(email = email, otp = otp))
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                Result.failure(ApiException(response.code(), errorBody))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun resendOtp(email: String): Result<OtpResponse> {
        return try {
            val response = authApi.resendOtp(ResendOtpRequest(email = email))
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                Result.failure(ApiException(response.code(), errorBody))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun oauthLogin(
        provider: String,
        token: String
    ): Result<AuthResponse> {
        return try {
            val response = authApi.oauthLogin(OAuthLoginRequest(provider = provider, token = token))
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                Result.failure(ApiException(response.code(), errorBody))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun oauthRegister(
        provider: String,
        token: String,
        email: String
    ): Result<AuthResponse> {
        return try {
            val response = authApi.oauthLogin(OAuthLoginRequest(provider = provider, token = token))
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                Result.failure(ApiException(response.code(), errorBody))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserDetails(
        userId: String,
        token: String
    ): Result<UserResponse> {
        return try {
            val response = authApi.getUserDetails(userId)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                Result.failure(ApiException(response.code(), errorBody))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}