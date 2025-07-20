package com.clara.data.api

import com.clara.data.BuildConfig

object ApiConstants {
    internal const val USER_AGENT =
        "DiscogsChallengeDemo/0.1 +https://github.com/TheBeeProgrammer/discogs"
    internal const val BASE_URL = "https://api.discogs.com/"
    internal const val DEFAULT_PAGE = 1
    internal const val PER_PAGE = 30
    internal const val TOKEN = BuildConfig.DISCOGS_TOKEN
    internal const val INTERNAL_SERVER_ERROR = 500
}

object PagingSourceConstants {
    internal const val MINIMAL_PAGE_NUMBER = 0
    internal const val FIRST_PAGE_NUMBER = 1
}