package com.realio.app.feature.authentication.domain.usecases
import com.realio.app.feature.authentication.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class LogoutUseCaseTest {
    private lateinit var authRepository: AuthRepository
    private lateinit var logoutUseCase: LogoutUseCase

    @Before
    fun setUp() {
        authRepository = mockk()
        logoutUseCase = LogoutUseCase(authRepository)
    }

    @Test
    fun `logoutUseCase should delegate to the repository`() = runTest {
        coEvery { authRepository.logout() } returns Result.success(true)

        val result = logoutUseCase()
        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { authRepository.logout() }
    }

    @Test
    fun `logoutUseCase should propagate error from the repository`() = runTest {
        val exception = Exception("Repository error")
        coEvery { authRepository.logout() } returns Result.failure(exception)

        val result = logoutUseCase()
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

}