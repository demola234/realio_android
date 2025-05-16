package com.realio.app.feature.authentication.data.model.response

import com.realio.app.feature.authentication.domain.entity.User

data class AuthResponse(
    val userId: String,
    val token: String,
    val refreshToken: String,
    val name: String,
    val email: String,
    val isVerified: Boolean
)

fun AuthResponse.toUser(): User {
    return User(
        id = userId,
        name = name,
        email = email,
        isVerified = isVerified
    )
}