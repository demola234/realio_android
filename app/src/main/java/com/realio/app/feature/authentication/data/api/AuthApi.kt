package com.realio.app.feature.authentication.data.api

import AuthApiResponse
import com.realio.app.feature.authentication.data.model.request.LoginRequest
import com.realio.app.feature.authentication.data.model.request.OAuthLoginRequest
import com.realio.app.feature.authentication.data.model.request.OAuthRegisterRequest
import com.realio.app.feature.authentication.data.model.request.RegisterRequest
import com.realio.app.feature.authentication.data.model.request.ResendOtpRequest
import com.realio.app.feature.authentication.data.model.request.VerifyRequest
import com.realio.app.feature.authentication.data.model.response.LogoutResponse
import com.realio.app.feature.authentication.data.model.response.OtpResponse
import com.realio.app.feature.authentication.data.model.response.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthApi {
    @POST("v1/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<AuthApiResponse>

    @POST("v1/auth/logout")
    suspend fun logout(@Header("Authorization") token: String): Response<LogoutResponse>

    @POST("v1/auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<AuthApiResponse>

    @POST("api/v1/resend-otp")
    suspend fun resendOtp(@Body resendOtpRequest: ResendOtpRequest): Response<OtpResponse>

    @POST("api/v1/verify")
    suspend fun verifyOtp(@Body verifyRequest: VerifyRequest): Response<AuthApiResponse>

    @POST("api/v1/oauth/login")
    suspend fun oauthLogin(@Body oauthLoginRequest: OAuthLoginRequest): Response<AuthApiResponse>

    @POST("api/v1/oauth/register")
    suspend fun oauthRegister(@Body oauthRegisterRequest: OAuthRegisterRequest): Response<AuthApiResponse>

    @GET("api/v1/user/{userId}")
    suspend fun getUserDetails(@Path("userId") userId: String): Response<UserResponse>
}