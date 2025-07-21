package com.clara.logger

import timber.log.Timber

/**
 * A [Timber.DebugTree] that also implements the [Logging] interface.
 * This allows for easy integration with Timber while still providing a common logging interface.
 *
 * The `createStackElementTag` method is overridden to provide a custom tag format that includes
 * the file name, line number, and method name of the calling code.
 */
class TimberLogging : Timber.DebugTree(), Logging {
    override fun createStackElementTag(element: StackTraceElement): String? {
        return "(${element.fileName}:${element.lineNumber}) on ${element.methodName}"
    }

    override fun d(t: Throwable?, message: String) {
        Timber.d(t, message)
    }

    override fun i(t: Throwable?, message: String) {
        Timber.i(t, message)
    }

    override fun e(t: Throwable?, message: String) {
        Timber.e(t, message)
    }

    override fun wtf(t: Throwable?, message: String) {
        Timber.wtf(t, message)
    }
}
