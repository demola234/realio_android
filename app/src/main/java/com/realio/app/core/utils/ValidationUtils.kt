package com.realio.app.core.utils

import android.util.Patterns
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.realio.app.core.ui.theme.AccentTertiaryColorDark
import com.realio.app.core.ui.theme.GreenOneColorLight
import java.util.regex.Pattern

/**
 * Utility class to handle form validation for authentication screens.
 */
object ValidationUtils {

    /**
     * Validates email format using a regular expression.
     * @param email The email to validate.
     * @return true if the email format is valid, false otherwise.
     */
    fun isValidEmail(email: String): Boolean {
        val emailPattern = Pattern.compile(
            "[a-zA-Z0-9+._%-+]{1,256}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" +
                    "(" +
                    "." +
                    "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" +
                    ")+"
        )
        return email.isNotEmpty() && emailPattern.matcher(email).matches()
    }

    /**
     * Validates password strength based on several criteria.
     * @param password The password to validate.
     * @return PasswordStrength enum representing the strength of the password.
     */
    fun getPasswordStrength(password: String): PasswordStrength {
        if (password.length < 8) {
            return PasswordStrength.WEAK
        }

        var hasLowerCase = false
        var hasUpperCase = false
        var hasDigit = false
        var hasSpecialChar = false

        for (char in password) {
            when {
                char.isLowerCase() -> hasLowerCase = true
                char.isUpperCase() -> hasUpperCase = true
                char.isDigit() -> hasDigit = true
                !char.isLetterOrDigit() -> hasSpecialChar = true
            }
        }

        val criteriaCount = listOf(hasLowerCase, hasUpperCase, hasDigit, hasSpecialChar)
            .count { it }

        return when {
            criteriaCount == 4 -> PasswordStrength.STRONG
            criteriaCount >= 2 -> PasswordStrength.MEDIUM
            else -> PasswordStrength.WEAK
        }
    }

    /**
     * Checks if passwords match.
     * @param password The original password.
     * @param confirmPassword The confirmation password.
     * @return true if passwords match, false otherwise.
     */
    fun doPasswordsMatch(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }

    /**
     * Gets a detailed message about password criteria.
     * @return String explaining password requirements.
     */
    fun getPasswordRequirementsMessage(): String {
        return "Password should have at least 8 characters and include uppercase, lowercase, numbers, and special characters."
    }
}

/**
 * Enum representing password strength.
 */
enum class PasswordStrength {
    WEAK,
    MEDIUM,
    STRONG;

    @Composable
    fun getColor(): Color {
        return when (this) {
            WEAK -> MaterialTheme.colorScheme.error
            MEDIUM -> AccentTertiaryColorDark
            STRONG -> GreenOneColorLight
        }
    }

    fun getLabel(): String {
        return when (this) {
            WEAK -> "Weak"
            MEDIUM -> "Medium"
            STRONG -> "Strong"
        }
    }
}


fun String.isValidEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}