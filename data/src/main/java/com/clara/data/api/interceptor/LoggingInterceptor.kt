package com.clara.data.api.interceptor

import android.util.Log
import com.clara.data.BuildConfig
import com.clara.logger.Logger
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject

/**
 * A custom [HttpLoggingInterceptor.Logger] implementation that logs HTTP request
 * and response details using Android's [Log] utility.
 *
 * This logger is useful during development and debugging to inspect network traffic.
 *
 * @constructor Creates an instance of [LoggingInterceptor], typically injected using a DI framework.
 */
class LoggingInterceptor @Inject constructor() : HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        Logger.i(message = message)
    }
}