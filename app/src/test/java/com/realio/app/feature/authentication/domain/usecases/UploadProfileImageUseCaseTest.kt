package com.realio.app.feature.authentication.domain.usecases

import com.realio.app.feature.authentication.domain.repository.AuthRepository
import com.realio.app.core.exception.ValidationException
import com.realio.app.feature.authentication.data.model.response.UploadImageResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import kotlin.test.assertIs
import org.junit.Before
import org.junit.Test
import java.io.File

class UploadProfileImageUseCaseTest {
    private lateinit var authRepository: AuthRepository
    private lateinit var uploadProfileImageUseCase: UploadProfileImageUseCase


    private var image = File("path/to/image.jpg")

    private var updateImageRes = UploadImageResponse(message = "Image Uploaded successfully",userID =  "user123",imageUrl = "https://example.com/image.jpg")

    @Before
    fun setUp() {
        authRepository = mockk()
        uploadProfileImageUseCase = UploadProfileImageUseCase(authRepository)
    }

    @Test
    fun `uploadProfileImageUseCase with valid image should return success`() = runTest {
        coEvery { authRepository.uploadProfileImage(any()) } returns Result.success(updateImageRes)


        val result = uploadProfileImageUseCase(image)

        assertTrue(result.isSuccess)

        coVerify(exactly = 1) {
            authRepository.uploadProfileImage(any())
        }
    }

    @Test
    fun `uploadProfileImageUseCase with blank image should return validation failure`() = runTest {
        val blankFile = mockk<File>()
        every { blankFile.path } returns ""

        val result = uploadProfileImageUseCase(image =
            blankFile)

        assertTrue(result.isFailure)
        assertIs<ValidationException>(result.exceptionOrNull())

        assertEquals("Image path cannot be empty", result.exceptionOrNull()?.message)


        coVerify(exactly = 0) {
            authRepository.uploadProfileImage(any())
        }
    }
}