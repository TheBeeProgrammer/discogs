package com.clara.clarachallenge.ui.components.release

import androidx.compose.runtime.Composable
import com.clara.clarachallenge.ui.components.utils.ImageTextRowItem
import com.clara.domain.model.Releases

/**
 * Composable function that displays a single release item in a list.
 *
 * This function takes an [Releases] object as input and renders its information,
 * including the release cover, title, and release year.
 *
 * @param releases The [Releases] object to display.
 */
@Composable
fun ReleaseListItem(releases: Releases) {
    ImageTextRowItem(
        imageUrl = releases.imageUrl ?: "",
        contentDescription = releases.title,
        title = releases.title,
        subtitle = releases.releaseYear,
        imageSize = 56,
    )
}
