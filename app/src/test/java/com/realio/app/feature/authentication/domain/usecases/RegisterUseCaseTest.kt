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


class RegisterUseCaseTest {
    private lateinit var authRepository: AuthRepository
    private lateinit var registerUseCase: RegisterUseCase

    private var mockEmail = "testUser@gmail.com"
    private var mockPassword = "Password123#"
    private var mockName = "Test User"

    private val mockUser = User(
        id = "user123", name = "Test User", email = "testUser@gmail.com", isVerified = true
    )


    @Before
    fun setUp() {
        authRepository = mockk()
        registerUseCase = RegisterUseCase(authRepository)
    }

    @Test
    fun `registerUseCase with valid input should return success`() = runTest {
        coEvery { authRepository.register(any(), any(), any()) } returns Result.success(mockUser)
        val result = registerUseCase(mockName, mockEmail, mockPassword)

        assertTrue(result.isSuccess)
        assertEquals(mockUser, result.getOrNull())

        coVerify(exactly = 1) {
            authRepository.register(any(), any(), any())
        }
    }

    @Test
    fun `registerUseCase with blank name should return validation failure`() = runTest {
        val result = registerUseCase("", mockEmail, mockPassword)

        assertTrue(result.isFailure)
        assertIs<ValidationException>(result.exceptionOrNull())

        assertEquals("Name cannot be empty", result.exceptionOrNull()?.message)

        coVerify(exactly = 0) {
            authRepository.register(any(), any(), any())
        }
    }

    @Test
    fun `registerUseCase with blank email should return validation failure`() = runTest {
        val result = registerUseCase(mockName, "", mockPassword)

        assertTrue(result.isFailure)
        assertIs<ValidationException>(result.exceptionOrNull())

        assertEquals("Email cannot be empty", result.exceptionOrNull()?.message)

        coVerify(exactly = 0) {
            authRepository.register(any(), any(), any())
        }
    }

    @Test
    fun `registerUseCase with invalid email format should return validation failure`() = runTest {
        val result = registerUseCase(mockName, "invalidEmail", mockPassword)

        assertTrue(result.isFailure)
        assertIs<ValidationException>(result.exceptionOrNull())

        assertEquals("Invalid email format", result.exceptionOrNull()?.message)

        coVerify(exactly = 0) {
            authRepository.register(any(), any(), any())
        }
    }

    @Test
    fun `registerUseCase with blank password should return validation failure`() = runTest {
        val result = registerUseCase(mockName, mockEmail, "")

        assertTrue(result.isFailure)
        assertIs<ValidationException>(result.exceptionOrNull())


        assertEquals("Password cannot be empty", result.exceptionOrNull()?.message)

        coVerify(exactly = 0) {
            authRepository.register(any(), any(), any())
        }
    }

    @Test
    fun `registerUseCase with password less than 8 characters should return validation failure`() =
        runTest {
            val result = registerUseCase(mockName, mockEmail, "short")

            assertTrue(result.isFailure)
            assertIs<ValidationException>(result.exceptionOrNull())

            coVerify(exactly = 0) {
                authRepository.register(any(), any(), any())
            }
        }
}