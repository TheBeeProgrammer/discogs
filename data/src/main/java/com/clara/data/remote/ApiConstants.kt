package com.clara.data.remote

import com.clara.data.BuildConfig

object ApiConstants {
    internal const val USER_AGENT =
        "DiscogsChallengeDemo/0.1 +https://github.com/TheBeeProgrammer/discogs"
    internal const val BASE_URL = "https://api.discogs.com/"
    internal const val DEFAULT_PAGE = 1
    internal const val PER_PAGE = 30
    internal const val TOKEN = BuildConfig.DISCOGS_TOKEN
    internal const val UNAUTHORIZED_CODE = 401
    internal const val FORBIDDEN_CODE = 403
    internal const val NOT_FOUND_CODE = 404
}

object PagingSourceConstants {
    internal const val MINIMAL_PAGE_NUMBER = 0
    internal const val FIRST_PAGE_NUMBER = 1
}