package com.realio.app.feature.authentication.domain.usecases

import com.realio.app.core.exception.ValidationException
import com.realio.app.feature.authentication.domain.entity.User
import com.realio.app.feature.authentication.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import kotlin.test.assertIs
import org.junit.Before
import org.junit.Test

class LoginUseCaseTest {
    private lateinit var authRepository: AuthRepository
    private lateinit var loginUseCase: LoginUseCase

    private val mockUser = User(
        id = "user123", name = "John Doe", email = "johndoe@example.com", isVerified = true
    )

    @Before
    fun setUp() {
        authRepository = mockk()
        loginUseCase = LoginUseCase(authRepository)
    }

    @Test
    fun `loginUseCase with valid email and password should return success`() = runTest {
        coEvery { authRepository.login(any(), any()) } returns Result.success(mockUser)
        val result = loginUseCase("johndoe@example.com", "ValidPass123!")

        assertTrue(result.isSuccess)
        assertEquals(mockUser, result.getOrNull())
        coVerify(exactly = 1) {
            authRepository.login("johndoe@example.com", "ValidPass123!")
        }
    }

    @Test
    fun `loginUseCase with blank email should return validation failure`() = runTest {
        val result = loginUseCase("", "ValidPass123!")

        assertTrue(result.isFailure)
        assertIs<ValidationException>(result.exceptionOrNull())
        assertEquals("Email cannot be empty", result.exceptionOrNull()?.message)

        coVerify(exactly = 0) {
            authRepository.login(any(), any())
        }
    }

    @Test
    fun `loginUseCase with invalid email format should return validation failure`() = runTest {
        val result = loginUseCase("notanemail", "ValidPass123!")

        assertTrue(result.isFailure)
        assertIs<ValidationException>(result.exceptionOrNull())
        assertEquals("Invalid email format", result.exceptionOrNull()?.message)

        coVerify(exactly = 0) {
            authRepository.login(any(), any())
        }
    }

    @Test
    fun `loginUseCase with blank password should return validation failure`() = runTest {
        val result = loginUseCase("johndoe@example.com", "")

        assertTrue(result.isFailure)
        assertIs<ValidationException>(result.exceptionOrNull())
        assertEquals("Password cannot be empty", result.exceptionOrNull()?.message)

        coVerify(exactly = 0) {
            authRepository.login(any(), any())
        }
    }

    @Test
    fun `loginUseCase should propagate error from the repository`() = runTest {
        val exception = Exception("Repository error")
        coEvery { authRepository.login(any(), any()) } returns Result.failure(exception)

        val result = loginUseCase("johndoe@example.com", "ValidPass123!")

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())

        coVerify(exactly = 1) {
            authRepository.login("johndoe@example.com", "ValidPass123!")
        }
    }
}