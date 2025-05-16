package com.realio.app.feature.authentication.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.realio.app.core.exception.ApiException
import com.realio.app.core.exception.ValidationException
import com.realio.app.feature.authentication.domain.entity.User
import com.realio.app.feature.authentication.domain.usecases.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState.asStateFlow()

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading

            registerUseCase(name, email, password)
                .onSuccess { user ->
                    _registerState.value = RegisterState.Success(user)
                }
                .onFailure { error ->
                    val errorMessage = when (error) {
                        is ValidationException -> error.message
                        is ApiException -> "Server error: ${error.message}"
                        else -> "An unexpected error occurred: ${error.message}"
                    }
                    _registerState.value = RegisterState.Error(errorMessage ?: "Unknown error")
                }
        }
    }

    fun resetState() {
        _registerState.value = RegisterState.Idle
    }
}

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    data class Success(val user: User) : RegisterState()
    data class Error(val message: String) : RegisterState()
}