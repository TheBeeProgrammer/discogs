package com.clara.logger

/**
 * Interface for logging messages.
 */
interface Logging {
    fun d(t: Throwable? = null, message: String)
    fun i(t: Throwable? = null, message: String)
    fun e(t: Throwable? = null, message: String)
    fun wtf(t: Throwable? = null, message: String)
}
