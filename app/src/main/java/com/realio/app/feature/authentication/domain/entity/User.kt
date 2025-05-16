package com.realio.app.feature.authentication.domain.entity


data class User(
    val id: String,
    val name: String,
    val email: String,
    val profilePicture: String? = null,
    val isVerified: Boolean = false
)