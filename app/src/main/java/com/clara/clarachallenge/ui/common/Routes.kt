package com.clara.clarachallenge.ui.common

sealed class Screen(val route: String) {
    object Search : Screen("search")
    object ArtistDetail : Screen("artistDetail/{artistId}") {
        fun createRoute(artistId: Int) = "artistDetail/$artistId"
    }

    object Releases : Screen("releases/{artistId}") {
        fun createRoute(artistId: Int) = "releases/$artistId"
    }
}
