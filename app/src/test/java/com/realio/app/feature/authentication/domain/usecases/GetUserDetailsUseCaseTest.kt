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

class GetUserDetailsUseCaseTest {
    private lateinit var authRepository: AuthRepository
    private lateinit var getUserDetailsUseCase: GetUserDetailsUseCase

    private val mockUser = User(
        id = "user123", name = "John Doe", email = "johndoe@example.com", isVerified = true
    )

    @Before
    fun setUp() {
        authRepository = mockk()
        getUserDetailsUseCase = GetUserDetailsUseCase(authRepository)
    }

    @Test
    fun `getUserDetailsUseCase with valid userId should return success`() = runTest {
        coEvery { authRepository.getUserDetails(any()) } returns Result.success(mockUser)

        val result = getUserDetailsUseCase("user123")

        assertTrue(result.isSuccess)
        assertEquals(mockUser, result.getOrNull())


        coVerify(exactly = 1) {
            authRepository.getUserDetails("user123")
        }
    }

    @Test
    fun `getUserDetailsUseCase with blank userId should return validation failure`() = runTest {
        val result = getUserDetailsUseCase("")

        assertTrue(result.isFailure)
        assertIs<ValidationException>(result.exceptionOrNull())

        assertEquals("User ID cannot be empty", result.exceptionOrNull()?.message)


        coVerify(exactly = 0) {
            authRepository.getUserDetails(any())
        }
    }
}