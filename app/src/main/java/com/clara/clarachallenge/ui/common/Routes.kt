package com.clara.clarachallenge.ui.common

sealed class Screen(val route: String) {
    object Search : Screen("search")
    object ArtistDetail : Screen("artistDetail/{artistId}") {
        fun createRoute(artistId: String) = "artistDetail/$artistId"
    }

    object Releases : Screen("releases/{artistId}") {
        fun createRoute(artistId: String) = "releases/$artistId"
    }
}
