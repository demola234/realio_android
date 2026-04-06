package com.realio.app.feature.authentication.domain.usecases

import com.realio.app.feature.authentication.domain.repository.AuthRepository
import com.realio.app.core.exception.ValidationException
import com.realio.app.feature.authentication.domain.entity.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import kotlin.test.assertIs
import org.junit.Before
import org.junit.Test

class OAuthLoginUseCaseTest {
    private lateinit var authRepository: AuthRepository
    private lateinit var oAuthLoginUseCase: OAuthLoginUseCase

    private val mockUser = User(
        id = "user123", name = "John Doe", email = "johndoe@example.com", isVerified = true
    )

    @Before
    fun setUp() {
        authRepository = mockk()
        oAuthLoginUseCase = OAuthLoginUseCase(authRepository)
    }

    @Test
    fun `oAuthLoginUseCase with valid provider and token should return success`() = runTest {
        coEvery { authRepository.oauthLogin(any(), any()) } returns Result.success(mockUser)
        val result = oAuthLoginUseCase("google", "valid_token_xyz")

        assertTrue(result.isSuccess)
        assertEquals(mockUser, result.getOrNull())

        coVerify(exactly = 1) {
            authRepository.oauthLogin(any(), any())
        }
    }

    @Test
    fun `oAuthLoginUseCase with blank provider should return validation failure`() = runTest {
        val result = oAuthLoginUseCase("", "valid_token")

        assertTrue(result.isFailure)
        assertIs<ValidationException>(result.exceptionOrNull())
        assertEquals("Provider cannot be empty", result.exceptionOrNull()?.message)

        coVerify(exactly = 0) {
            authRepository.oauthLogin(any(), any())
        }
    }

    @Test
    fun `oAuthLoginUseCase with blank token should return validation failure`() = runTest {
        val result = oAuthLoginUseCase("google", "")

        assertTrue(result.isFailure)
        assertIs<ValidationException>(result.exceptionOrNull())
        assertEquals("Token cannot be empty", result.exceptionOrNull()?.message)

        coVerify(exactly = 0) {
            authRepository.oauthLogin(any(), any())
        }
    }
}