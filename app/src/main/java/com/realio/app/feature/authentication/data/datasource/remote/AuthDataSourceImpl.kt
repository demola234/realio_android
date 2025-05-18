package com.realio.app.feature.authentication.data.datasource.remote

import AuthApiResponse
import android.util.Log
import com.realio.app.core.exception.ApiException
import com.realio.app.feature.authentication.data.api.AuthApi
import com.realio.app.feature.authentication.data.model.request.LoginRequest
import com.realio.app.feature.authentication.data.model.request.OAuthLoginRequest
import com.realio.app.feature.authentication.data.model.request.RegisterRequest
import com.realio.app.feature.authentication.data.model.request.ResendOtpRequest
import com.realio.app.feature.authentication.data.model.request.VerifyRequest
import com.realio.app.feature.authentication.data.model.response.LogoutResponse
import com.realio.app.feature.authentication.data.model.response.OtpResponse
import com.realio.app.feature.authentication.data.model.response.UploadImageResponse
import com.realio.app.feature.authentication.data.model.response.UserResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.io.File

class AuthDataSourceImpl(private val authApi: AuthApi) : AuthDataSource {
    override suspend fun login(email: String, password: String): Result<AuthApiResponse> {
        return try {
            val response = authApi.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                Timber.tag("AuthDataSourceImpl").d("$response.body()")
                Result.success(response.body()!!)
            } else {
                val errorBody = response.message() ?: "Unknown error"
                Result.failure(ApiException(response.code(), errorBody))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(name: String, email: String, password: String): Result<AuthApiResponse> {
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
    ): Result<OtpResponse> {
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
    ): Result<AuthApiResponse> {
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
    ): Result<AuthApiResponse> {
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
    override suspend fun uploadImage(
        userId: String,
        token: String,
        imageFile: File
    ): Result<UploadImageResponse> {
        return try {
            // Log the incoming parameters
            Log.d("AuthDataSourceImpl", "uploadImage called with userId: $userId")
            Log.d("AuthDataSourceImpl", "File exists: ${imageFile.exists()}, File size: ${imageFile.length()} bytes")
            Log.d("AuthDataSourceImpl", "Original token (first 10 chars): ${token.take(10)}...")

            // Create the multipart request bodies
            val userIdRequestBody = userId.toRequestBody("text/plain".toMediaTypeOrNull())
            val imageRequestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("content", imageFile.name, imageRequestBody)

            // Format the token correctly with "Bearer " prefix
            val formattedToken = if (token.startsWith("Bearer ")) token else "Bearer $token"

            Log.d("AuthDataSourceImpl", "Formatted Token (first 15 chars): ${formattedToken.take(15)}...")
            Log.d("AuthDataSourceImpl", "Making API call to upload image...")

            // Call the API with the properly formatted request
            val response = authApi.uploadImage(formattedToken, userIdRequestBody, imagePart)

            Log.d("AuthDataSourceImpl", "API Response Code: ${response.code()}")

            if (response.isSuccessful) {
                Timber.tag("AuthDataSourceImpl")
                    .d("Upload successful! Response: ${response.body()}")
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                Timber.tag("AuthDataSourceImpl")
                    .e("Upload failed! Error code: ${response.code()}, Error body: $errorBody")
                Result.failure(ApiException(response.code(), errorBody))
            }
        } catch (e: Exception) {
            Timber.tag("AuthDataSourceImpl").e(e, "Exception during upload")
            Result.failure(e)
        }
    }
}