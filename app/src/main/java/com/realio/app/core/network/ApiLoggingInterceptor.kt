package com.realio.app.core.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Custom logging interceptor for API calls
 */
class ApiLoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val startTime = System.nanoTime()
        Timber.d("API Request: ${request.method} ${request.url}")
        Timber.d("API Headers: ${request.headers}")
        request.body?.let { body ->
            Timber.d("API Request Body: ${body.toString()}")
        }

        val response = chain.proceed(request)

        val endTime = System.nanoTime()
        val duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime)

        val responseBody = response.peekBody(Long.MAX_VALUE)
        Timber.d("API Response: ${response.code} (${duration}ms)")
        Timber.d("API Response Body: ${responseBody.string()}")

        return response
    }
}
