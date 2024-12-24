package com.example.goldendays
import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GoldenDaysApp : Application() {
    override fun onCreate() {
        super.onCreate()

    }
}