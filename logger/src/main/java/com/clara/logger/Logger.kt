package com.clara.logger

import timber.log.Timber

/**
 * A simple logger that uses Timber for debug builds and a no-op logger for release builds.
 *
 * To use, call `Logger.init()` in your Application class. Then, use the static methods to log
 * messages:
 *
 * ```
 * Logger.d("Debug message")
 * Logger.i("Info message")
 * Logger.e(Exception("Error!"), "Error message")
 * ```
 */
object Logger {
    var isDebug = false
    private val logger: Logging by lazy {
        if (isDebug) {
            Timber.plant(TimberLogging())
            TimberLogging()
        } else {
            NoOpLogger()
        }
    }

    fun init() {
        // Intentionally left blank – lazy block handles setup
    }

    fun d(message: String, t: Throwable? = null) = logger.d(t, message)
    fun i(message: String, t: Throwable? = null) = logger.i(t, message)
    fun e(t: Throwable? = null, message: String) = logger.e(t, message)
    fun wtf(t: Throwable? = null, message: String) = logger.wtf(t, message)
}
