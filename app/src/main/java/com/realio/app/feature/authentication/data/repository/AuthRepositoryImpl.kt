package com.realio.app.feature.authentication.data.repository

import com.realio.app.feature.authentication.data.datasource.local.TokenStorage
import com.realio.app.feature.authentication.data.datasource.remote.AuthDataSource
import com.realio.app.feature.authentication.data.model.response.toUser
import com.realio.app.feature.authentication.domain.entity.User
import com.realio.app.feature.authentication.domain.repository.AuthRepository
import toAuthResponse
import toUser

class AuthRepositoryImpl(
    private val authDataSource: AuthDataSource,
    private val tokenStorage: TokenStorage
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<User> {
        return authDataSource.login(email, password).fold(
            onSuccess = { response ->
                if (response?.user == null || response.session == null) {
                    return Result.failure(Exception("Invalid response from server"))
                }

                // Safe token access
                val token = response.session.token ?: ""
                tokenStorage.saveTokens(token, token)

                // Convert to User domain model with null checks
                val authResponse = response.toAuthResponse()
                Result.success(authResponse.toUser())
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }

    override suspend fun register(name: String, email: String, password: String): Result<User> {
        return authDataSource.register(name, email, password).fold(
            onSuccess = { response ->
                val authResponse = response.toAuthResponse()
                Result.success(authResponse.toUser())
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }

    override suspend fun logout(): Result<Boolean> {
        return try {
            val token = tokenStorage.getAuthToken()
            if (token != null) {
                authDataSource.logout(token).fold(
                    onSuccess = {
                        tokenStorage.clearTokens()
                        Result.success(true)
                    },
                    onFailure = { error ->
                        Result.failure(error)
                    }
                )
            } else {
                // If no token exists, just clear local storage
                tokenStorage.clearTokens()
                Result.success(true)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun verifyOtp(email: String, otp: String): Result<User> {
        return authDataSource.verifyOtp(email, otp).fold(
            onSuccess = { response ->
                // After verification, save tokens
                if (response?.user == null || response.session == null) {
                    return Result.failure(Exception("Invalid response from server"))
                }

                // Safe token access
                val token = response.session.token ?: ""
                tokenStorage.saveTokens(token, token)

                // Convert to User domain model with null checks
                val authResponse = response.toAuthResponse()
                Result.success(authResponse.toUser())
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }

    override suspend fun resendOtp(email: String): Result<Boolean> {
        return authDataSource.resendOtp(email).fold(
            onSuccess = { Result.success(true) },
            onFailure = { error -> Result.failure(error) }
        )
    }

    override suspend fun oauthLogin(provider: String, token: String): Result<User> {
        return authDataSource.oauthLogin(provider, token).fold(
            onSuccess = { response ->
                if (response?.user == null || response.session == null) {
                    return Result.failure(Exception("Invalid response from server"))
                }

                // Safe token access
                val token = response.session.token ?: ""
                tokenStorage.saveTokens(token, token)

                // Convert to User domain model with null checks
                val authResponse = response.toAuthResponse()
                Result.success(authResponse.toUser())
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }

    override suspend fun oauthRegister(provider: String, token: String, email: String): Result<User> {
        return authDataSource.oauthRegister(provider, token, email).fold(
            onSuccess = { response ->
                if (response?.user == null || response.session == null) {
                    return Result.failure(Exception("Invalid response from server"))
                }

                // Safe token access
                val token = response.session.token ?: ""
                tokenStorage.saveTokens(token, token)

                // Convert to User domain model with null checks
                val authResponse = response.toAuthResponse()
                Result.success(authResponse.toUser())
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }

    override suspend fun getUserDetails(userId: String): Result<User> {
        val token = tokenStorage.getAuthToken() ?: return Result.failure(Exception("No auth token found"))

        return authDataSource.getUserDetails(userId, token).fold(
            onSuccess = { response ->
                Result.success(response.toUser())
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }
}