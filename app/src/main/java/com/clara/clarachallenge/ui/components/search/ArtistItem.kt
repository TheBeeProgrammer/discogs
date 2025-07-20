package com.clara.clarachallenge.ui.components.search

import androidx.compose.runtime.Composable
import com.clara.clarachallenge.ui.components.utils.ImageTextRowItem
import com.clara.domain.model.Artist

/**
 * Composable function that displays an artist item in a list.
 * It shows the artist's image and name.
 *
 * @param artist The [Artist] object to display.
 * @param onClick A lambda function to be executed when the item is clicked.
 */
@Composable
fun ArtistItem(
    artist: Artist,
    onClick: () -> Unit
) {
    ImageTextRowItem(
        imageUrl = artist.imageUrl ?: "",
        contentDescription = artist.name,
        title = artist.name,
        onClick = onClick
    )
}
