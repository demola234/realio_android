package com.realio.app.feature.authentication.data.model.request

data class OAuthLoginRequest(
    val provider: String,
    val token: String
)