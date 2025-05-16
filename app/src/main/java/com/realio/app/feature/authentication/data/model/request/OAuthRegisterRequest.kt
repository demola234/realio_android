package com.realio.app.feature.authentication.data.model.request

data class OAuthRegisterRequest(
    val provider: String,
    val token: String,
    val email: String
)