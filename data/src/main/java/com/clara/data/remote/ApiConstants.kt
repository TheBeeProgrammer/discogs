package com.clara.data.remote

import com.clara.data.BuildConfig

object ApiConstants {
    internal const val USER_AGENT =
        "DiscogsChallengeDemo/0.1 +https://github.com/TheBeeProgrammer/discogs"
    internal const val BASE_URL = "https://api.discogs.com/"
    internal const val DEFAULT_PAGE = 1
    internal const val PER_PAGE = 30
    internal const val TOKEN = BuildConfig.DISCOGS_TOKEN
}