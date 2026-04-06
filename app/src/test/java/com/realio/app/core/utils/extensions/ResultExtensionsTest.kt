package com.realio.app.core.utils.extensions

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import com.realio.app.core.utils.extensions.Validators

class ResultExtensionsTest {
    private lateinit var validators: Validators

    @Before
    fun setUp() {
        validators = Validators
    }

    @Test
    fun `validateEmail should return true for valid email`() {
        val result = validators.validateEmails("william.henry.harrison@example-pet-store.com")
        assertTrue(result)
    }

    @Test
    fun `validateEmail should return false for invalid email`() {
        val result = validators.validateEmails("invalid-email")
        assertFalse(result)
    }


    @Test
    fun `validatePassword should return true for valid password`() {
        val result = validators.validatePassword("ValidPass123!")
        assertTrue(result)
    }

    @Test
    fun `validatePassword should return false for invalid password`() {
        val result = validators.validatePassword("short")
        assertFalse(result)
    }

    @Test
    fun `validateOtp should return true for valid OTP`() {
        val result = validators.validateOtp("123456")
        assertTrue(result)
    }


    @Test
    fun `validateOtp should return false for invalid OTP`() {
        val result = validators.validateOtp("12345")
        assertFalse(result)
    }

    @Test
    fun `validateOtp should return false for non-numeric OTP`() {
        val result = validators.validateOtp("12345A")
        assertFalse(result)
    }

    @Test
    fun `validateOtp should return false for empty OTP`() {
        val result = validators.validateOtp("")
        assertFalse(result)
    }
}