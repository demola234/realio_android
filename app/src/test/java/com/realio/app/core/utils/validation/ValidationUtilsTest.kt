package com.realio.app.core.utils.validation

import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ValidationUtilsTest {

    private lateinit var validationUtils: ValidationUtils

    @Before
    fun setUp() {
        validationUtils = ValidationUtils
    }

    @Test
    fun `isValidEmail() should return true for valid email`() = runTest {
        val result = validationUtils.isValidEmail("harrison@example.com")

        assertTrue(result)
        assertEquals(true, result)
    }

    @Test
    fun `isValidEmail() should return false for an invalid email`() = runTest {
        val result = validationUtils.isValidEmail("notanemail")

        assertFalse(result)
        assertEquals(false, result)
    }

    @Test
    fun `isValidEmail() should return false for an empty email`() = runTest {
        val result = validationUtils.isValidEmail("")

        assertFalse(result)
        assertEquals(false, result)
    }

    @Test
    fun `getPasswordStrength() should return WEAK for a short password`() = runTest {
        val result = validationUtils.getPasswordStrength("Pass1!")

        assertEquals(PasswordStrength.WEAK, result)
        assertEquals("Weak", result.getLabel())
    }

    @Test
    fun `getPasswordStrength() should return MEDIUM for a medium password`() = runTest {
        val result = validationUtils.getPasswordStrength("Abcdefgh1")

        assertEquals(PasswordStrength.MEDIUM, result)
        assertEquals("Medium", result.getLabel())
    }

    @Test
    fun `getPasswordStrength() should return STRONG for a strong password`() = runTest {
        val result = validationUtils.getPasswordStrength("Abcdefgh1!")

        assertEquals(PasswordStrength.STRONG, result)
        assertEquals("Strong", result.getLabel())
    }

    @Test
    fun `doPasswordsMatch() should return true for matching passwords`() = runTest {
        val result = validationUtils.doPasswordsMatch("TestPass1!", "TestPass1!")

        assertTrue(result)
        assertEquals(true, result)
    }

    @Test
    fun `doPasswordsMatch() should return false for non-matching passwords`() = runTest {
        val result = validationUtils.doPasswordsMatch("TestPass1!", "TestPass2!")

        assertFalse(result)
        assertEquals(false, result)
    }

    @Test
    fun `doPasswordsMatch() should return false for case-sensitive passwords`() = runTest {
        val result = validationUtils.doPasswordsMatch("TestPass1!", "testpass1!")

        assertFalse(result)
        assertEquals(false, result)
    }

    @Test
    fun `doPasswordsMatch() should return true for empty passwords`() = runTest {
        val result = validationUtils.doPasswordsMatch("", "")

        assertTrue(result)
        assertEquals(true, result)
    }

    @Test
    fun `doPasswordsMatch() should return false for one empty password`() = runTest {
        val result = validationUtils.doPasswordsMatch("TestPass1!", "")

        assertFalse(result)
        assertEquals(false, result)
    }

    @Test
    fun `doPasswordsMatch() should return false for trailing whitespace`() = runTest {
        val result = validationUtils.doPasswordsMatch("TestPass1!", "TestPass1! ")

        assertFalse(result)
        assertEquals(false, result)
    }

    @Test
    fun `getPasswordRequirementsMessage() should return the correct message`() = runTest {
        val result = validationUtils.getPasswordRequirementsMessage()

        assertEquals("Password should have at least 8 characters and include uppercase, lowercase, numbers, and special characters.", result)
    }

    @Test
    fun `isValidEmail() should return false for an email with whitespace`() = runTest {
        val result = validationUtils.isValidEmail("notanemail@example.com ")

        assertFalse(result)
        assertEquals(false, result)
    }
}