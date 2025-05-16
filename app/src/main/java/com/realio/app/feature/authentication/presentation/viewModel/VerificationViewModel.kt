package com.realio.app.feature.authentication.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.realio.app.core.exception.ApiException
import com.realio.app.core.exception.ValidationException
import com.realio.app.feature.authentication.domain.entity.User
import com.realio.app.feature.authentication.domain.usecases.ResendOtpUseCase
import com.realio.app.feature.authentication.domain.usecases.VerifyOtpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerificationViewModel @Inject constructor(
    private val verifyOtpUseCase: VerifyOtpUseCase,
    private val resendOtpUseCase: ResendOtpUseCase
) : ViewModel() {

    private val _verificationState = MutableStateFlow<VerificationState>(VerificationState.Idle)
    val verificationState: StateFlow<VerificationState> = _verificationState.asStateFlow()

    private val _resendState = MutableStateFlow<ResendState>(ResendState.Idle)
    val resendState: StateFlow<ResendState> = _resendState.asStateFlow()

    fun verifyOtp(email: String, otp: String) {
        viewModelScope.launch {
            _verificationState.value = VerificationState.Loading

            verifyOtpUseCase(email, otp)
                .onSuccess { user ->
                    _verificationState.value = VerificationState.Success(user)
                }
                .onFailure { error ->
                    val errorMessage = when (error) {
                        is ValidationException -> error.message
                        is ApiException -> "Server error: ${error.message}"
                        else -> "An unexpected error occurred: ${error.message}"
                    }
                    _verificationState.value = VerificationState.Error(errorMessage ?: "Unknown error")
                }
        }
    }

    fun resendOtp(email: String) {
        viewModelScope.launch {
            _resendState.value = ResendState.Loading

            resendOtpUseCase(email)
                .onSuccess {
                    _resendState.value = ResendState.Success
                }
                .onFailure { error ->
                    val errorMessage = when (error) {
                        is ValidationException -> error.message
                        is ApiException -> "Server error: ${error.message}"
                        else -> "An unexpected error occurred: ${error.message}"
                    }
                    _resendState.value = ResendState.Error(errorMessage ?: "Unknown error")
                }
        }
    }

    fun resetVerificationState() {
        _verificationState.value = VerificationState.Idle
    }

    fun resetResendState() {
        _resendState.value = ResendState.Idle
    }
}

sealed class VerificationState {
    object Idle : VerificationState()
    object Loading : VerificationState()
    data class Success(val user: User) : VerificationState()
    data class Error(val message: String) : VerificationState()
}

sealed class ResendState {
    object Idle : ResendState()
    object Loading : ResendState()
    object Success : ResendState()
    data class Error(val message: String) : ResendState()
}