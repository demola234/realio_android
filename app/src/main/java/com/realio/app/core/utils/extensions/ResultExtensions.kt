package com.realio.app.core.utils.extensions

import android.util.Patterns

object Validators {
    fun validateEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    fun validatePassword(password: String): Boolean {
        return password.length >= 8
    }

    fun validateOtp(otp: String): Boolean {
        return otp.length == 6 && otp.all { it.isDigit() }
    }
}