package com.realio.app.feature.authentication.data.model.request

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)