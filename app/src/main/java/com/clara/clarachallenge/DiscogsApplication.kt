package com.clara.clarachallenge

import android.app.Application
import com.clara.data.BuildConfig
import com.clara.logger.Logger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DiscogsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Logger.isDebug = BuildConfig.DEBUG
        Logger.init()
    }
}
