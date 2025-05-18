package com.realio.app.feature.authentication.data.model.response

import com.google.gson.annotations.SerializedName



data class SessionDto(
    @SerializedName("token") val token: String?,
    @SerializedName("expiresAt") val expiresAt: String?
)

data class OtpResponse(
    val valid: Boolean? = null,
    val message: String? = null,
    val session: SessionDto? = null
)