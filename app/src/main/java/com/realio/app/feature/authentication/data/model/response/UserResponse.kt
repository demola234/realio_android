package com.realio.app.feature.authentication.data.model.response

import com.realio.app.feature.authentication.domain.entity.User

data class UserResponse(
    val userId: String,
    val name: String,
    val email: String,
    val profilePicture: String?,
    val isVerified: Boolean,
    val createdAt: String
)


fun UserResponse.toUser(): User {
    return User(
        id = userId,
        name = name,
        email = email,
        profilePicture = profilePicture,
        isVerified = isVerified
    )
}