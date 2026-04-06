package com.realio.app.core.utils.extensions

import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import androidx.lifecycle.LifecycleOwner
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.junit.Test

class FlowExtensionsTest {
    @Test
    fun `mapSuccess() should return success result with transformed value`() = runTest {
        val result = Result.success(10).mapSuccess { it * 2 }

        assertTrue(result.isSuccess)
        assertEquals(20, result.getOrThrow())
    }

    @Test
    fun `mapSuccess() should return failure result if original result is failure`() = runTest {
        val result = Result.failure<Int>(Exception("Original failure")).mapSuccess { it * 2 }

        assertFalse(result.isSuccess)
        assertTrue(result.isFailure)
        assertEquals("Original failure", result.exceptionOrNull()?.message)
    }


    @Test
    fun `collectAsEffect() should collect values from state flow`() = runTest {
        val stateFlow = MutableStateFlow(0)
        val lifecycleOwner = mockk<LifecycleOwner>()

        val collectJob = launch {
            stateFlow.collectAsEffect(lifecycleOwner) { value ->
                assertEquals(0, value)
            }
        }

        stateFlow.value = 1

        collectJob.cancel()
    }

    @Test
    fun `collectAsEffect() should handle lifecycle state changes`() = runTest {
        val stateFlow = MutableStateFlow(0)
        val lifecycleOwner = mockk<LifecycleOwner>()

        val collectJob = launch {
            stateFlow.collectAsEffect(lifecycleOwner) { value ->
                assertEquals(0, value)
            }
        }

        stateFlow.value = 1

        collectJob.cancel()
    }
}