package com.realio.app.feature.authentication.data.model.response

data class OtpResponse(
    val valid: Boolean? = null,
    val message: String? = null,
    val expiresAt: Long? = null
)