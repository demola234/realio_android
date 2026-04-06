package com.realio.app.feature.authentication.domain.usecases

import com.realio.app.feature.authentication.domain.repository.AuthRepository
import com.realio.app.core.exception.ValidationException
import com.realio.app.feature.authentication.data.model.response.OtpResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import kotlin.test.assertIs
import org.junit.Before
import org.junit.Test

class VerifyOtpUseCaseTest {
    private lateinit var authRepository: AuthRepository
    private lateinit var verifyOtpUseCase: VerifyOtpUseCase

    private var email = "john@example.com"
    private var otp = "123456"

    private var otpRes = OtpResponse()

    @Before
    fun setUp() {
        authRepository = mockk()
        verifyOtpUseCase = VerifyOtpUseCase(authRepository)
    }

    @Test
    fun `verifyOtpUseCase with valid email and otp should return success`() = runTest {
        coEvery { authRepository.verifyOtp(any(), any()) } returns Result.success(otpRes)

        val result = verifyOtpUseCase(email, otp)

        assertTrue(result.isSuccess)
        assertEquals(otpRes, result.getOrNull())

        coVerify(exactly = 1) {
            authRepository.verifyOtp(any(), any())
        }
    }

    @Test
    fun `verifyOtpUseCase with blank email should return validation failure`() = runTest {
        val result = verifyOtpUseCase("", otp)

        assertTrue(result.isFailure)
        assertIs<ValidationException>(result.exceptionOrNull())

        assertEquals("Email cannot be empty", result.exceptionOrNull()?.message)

        coVerify(exactly = 0) {
            authRepository.verifyOtp(any(), any())
        }
    }

    @Test
    fun `verifyOtpUseCase with blank otp should return validation failure`() = runTest {
        val result = verifyOtpUseCase(email, "")

        assertTrue(result.isFailure)
        assertIs<ValidationException>(result.exceptionOrNull())

        assertEquals("OTP cannot be empty", result.exceptionOrNull()?.message)

        coVerify(exactly = 0) {
            authRepository.verifyOtp(any(), any())
        }
    }

    @Test
    fun `verifyOtpUseCase with otp less than 6 digits should return validation failure`() = runTest {
        val result = verifyOtpUseCase(email, "12345")

        assertTrue(result.isFailure)
        assertIs<ValidationException>(result.exceptionOrNull())

        assertEquals("Invalid OTP format", result.exceptionOrNull()?.message)

        coVerify(exactly = 0) {
            authRepository.verifyOtp(any(), any())
        }
    }
}