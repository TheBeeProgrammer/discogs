package com.clara.logger

/**
 * A [Logging] implementation that does nothing.
 *
 * This can be useful in situations where logging is not desired or needed,
 * such as in release builds or unit tests where logging output might be noisy.
 */
class NoOpLogger : Logging {
    override fun d(t: Throwable?, message: String) {}
    override fun i(t: Throwable?, message: String) {}
    override fun e(t: Throwable?, message: String) {}
    override fun wtf(t: Throwable?, message: String) {}
}
