package com.realio.app.core.common

sealed class Result<out T> {
    object Loading : Result<Nothing>()
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
    data class Exception(val e: Exception) : Result<Nothing>()
}