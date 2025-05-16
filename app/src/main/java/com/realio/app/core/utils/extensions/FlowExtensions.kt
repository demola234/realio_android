package com.realio.app.core.utils.extensions

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

inline fun <T, R> Result<T>.mapSuccess(transform: (T) -> R): Result<R> {
    return when {
        isSuccess -> Result.success(transform(getOrThrow()))
        else -> Result.failure(exceptionOrNull()!!)
    }
}

// StateFlow collectors for ViewModels
fun <T> StateFlow<T>.collectAsEffect(
    lifecycleOwner: LifecycleOwner,
    block: suspend (T) -> Unit
) {
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            collect { value ->
                block(value)
            }
        }
    }
}
