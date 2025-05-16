package com.realio.app.core.exception

/**
 * Exception thrown when API returns an error response
 */
class ApiException(val code: Int, message: String) : Exception(message)