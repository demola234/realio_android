package com.realio.app.feature.authentication.domain.usecases

import com.realio.app.feature.authentication.domain.repository.AuthRepository
import com.realio.app.core.exception.ValidationException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import kotlin.test.assertIs
import org.junit.Before
import org.junit.Test

class ResendOtpUseCaseTest {
    private lateinit var authRepository: AuthRepository
    private lateinit var resendOtpUseCase: ResendOtpUseCase

    private var email = "testUser@gmail.com"

    @Before
    fun setUp() {
        authRepository = mockk()
        resendOtpUseCase = ResendOtpUseCase(authRepository)
    }

    @Test
    fun `resendOtpUseCase with valid email should return success`() = runTest {
        coEvery { authRepository.resendOtp(any()) } returns Result.success(true)

        val result = resendOtpUseCase(email)

        assertTrue(result.isSuccess)

        coVerify(exactly = 1) {
            authRepository.resendOtp(any())
        }
    }

    @Test
    fun `resendOtpUseCase with blank email should return validation failure`() = runTest {
        val result = resendOtpUseCase("")

        assertTrue(result.isFailure)
        assertIs<ValidationException>(result.exceptionOrNull())

        assertEquals("Email cannot be empty", result.exceptionOrNull()?.message)


        coVerify(exactly = 0) {
            authRepository.resendOtp(any())
        }
    }
}