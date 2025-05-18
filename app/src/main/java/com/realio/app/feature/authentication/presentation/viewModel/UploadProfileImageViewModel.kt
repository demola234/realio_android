package com.realio.app.feature.authentication.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.realio.app.core.exception.ApiException
import com.realio.app.core.exception.ValidationException
import com.realio.app.feature.authentication.data.model.response.UploadImageResponse
import com.realio.app.feature.authentication.domain.usecases.UploadProfileImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UploadProfileImageViewModel @Inject constructor(
    private val uploadProfileImageUseCase: UploadProfileImageUseCase
) : ViewModel() {
    private val _uploadProfileState = MutableStateFlow<UploadImageState>(UploadImageState.Idle)
    val uploadProfileState: StateFlow<UploadImageState> = _uploadProfileState.asStateFlow()

     fun uploadProfileImage(image: File) {
        viewModelScope.launch {
            _uploadProfileState.value = UploadImageState.Loading
            uploadProfileImageUseCase(image)
                .onSuccess { uploadResponse ->
                    _uploadProfileState.value = UploadImageState.Success(uploadResponse)
                }
                .onFailure { error ->
                    val errorMessage = when (error) {
                        is ValidationException -> error.message
                        is ApiException -> "Server error: ${error.message}"
                        else -> "An unexpected error occurred: ${error.message}"
                    }
                    _uploadProfileState.value = UploadImageState.Error(
                        message = errorMessage ?: "An unexpected error occurred"
                    )
                }
        }
    }

}

sealed class UploadImageState {
    object Idle : UploadImageState()
    object Loading : UploadImageState()
    data class Success(val uploadResponse: UploadImageResponse) : UploadImageState()
    data class Error(val message: String) : UploadImageState()
}