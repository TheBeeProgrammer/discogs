package com.clara.data.remote.interceptor

import com.clara.data.remote.ApiConstants
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

/**
 * An [Interceptor] that automatically adds Discogs-specific authentication parameters
 * to each outgoing HTTP request.
 *
 * This includes:
 * - Adding the `token` query parameter using the provided API token.
 * - Adding the `User-Agent` header required by the Discogs API.
 *
 * @constructor Creates an instance of [DiscogsAuthInterceptor] which is typically injected
 * via Hilt or another DI framework.
 *
 * @see <a href="https://www.discogs.com/developers/#page:authentication">Discogs Authentication</a>
 */
class DiscogsAuthInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val requestWithAuth = originalRequest.withDiscogsToken(ApiConstants.TOKEN)
            .withUserAgent(ApiConstants.USER_AGENT)

        return chain.proceed(requestWithAuth)
    }
}

/**
 * Adds the Discogs API token as a query parameter to the request URL.
 *
 * @param token The token to include.
 * @return A new [Request] with the `token` query parameter added.
 */
fun Request.withDiscogsToken(token: String): Request {
    return newBuilder().header("Authorization", "Discogs token=$token").build()
}

/**
 * Adds the `User-Agent` header to comply with Discogs API requirements.
 *
 * @param userAgent The user agent string to include.
 * @return A new [Request] with the `User-Agent` header.
 */
fun Request.withUserAgent(userAgent: String): Request {
    return newBuilder().header("User-Agent", userAgent).build()
}



