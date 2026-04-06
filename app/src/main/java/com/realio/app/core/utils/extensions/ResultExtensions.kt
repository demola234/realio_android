package com.realio.app.core.utils.extensions

import java.util.regex.Pattern

object Validators {
    private val EMAIL_PATTERN = Pattern.compile(
        "[a-zA-Z0-9+._%\\-]{1,256}" +
        "@" +
        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
        "(" +
        "\\." +
        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
        ")+"
    )

    fun validateEmails(email: String): Boolean {
        if (email.isEmpty()) {
            return false
        }
        return EMAIL_PATTERN.matcher(email).matches()
    }

    fun validatePassword(password: String): Boolean {
        return password.length >= 8
    }

    fun validateOtp(otp: String): Boolean {
        return otp.length == 6 && otp.all { it.isDigit() }
    }
}