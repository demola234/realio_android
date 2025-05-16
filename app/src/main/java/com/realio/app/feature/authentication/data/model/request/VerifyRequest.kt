package com.realio.app.feature.authentication.data.model.request

data class VerifyRequest(
    val email: String,
    val otp: String
)