package com.realio.app.feature.authentication.data.model.response

data class OtpResponse(
    val success: Boolean,
    val message: String,
    val expiresAt: Long
)