package com.realio.app
import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class RealioAndroidApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}