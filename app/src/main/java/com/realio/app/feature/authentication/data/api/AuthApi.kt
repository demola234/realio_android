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

    @POST("v1/auth/resend-otp")
    suspend fun resendOtp(@Body resendOtpRequest: ResendOtpRequest): Response<OtpResponse>

    @POST("v1/auth/verify")
    suspend fun verifyOtp(@Body verifyRequest: VerifyRequest): Response<OtpResponse>

    @POST("v1/auth/oauth/login")
    suspend fun oauthLogin(@Body oauthLoginRequest: OAuthLoginRequest): Response<AuthApiResponse>

    @POST("v1/auth/register")
    suspend fun oauthRegister(@Body oauthRegisterRequest: OAuthRegisterRequest): Response<AuthApiResponse>

    @GET("v1/auth/user/{userId}")
    suspend fun getUserDetails(@Path("userId") userId: String): Response<UserResponse>
}