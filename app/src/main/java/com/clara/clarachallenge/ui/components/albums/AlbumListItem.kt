package com.clara.clarachallenge.ui.components.albums

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.clara.clarachallenge.R
import com.clara.clarachallenge.ui.components.utils.CachedImage
import com.clara.domain.model.Album

/**
 * Composable function that displays a single album item in a list.
 *
 * This function takes an [Album] object as input and renders its information,
 * including the album cover, title, and release year.
 *
 * @param album The [Album] object to display.
 */
@Composable
fun AlbumListItem(album: Album) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        CachedImage(
            imageUrl = album.imageUrl,
            contentDescription = album.title,
            modifier = Modifier.size(48.dp),
            placeholder = painterResource(R.drawable.ic_loading),
            error = painterResource(R.drawable.ic_error)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.align(Alignment.CenterVertically)) {
            Text(
                text = album.title.ifBlank { "Unknown" },
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = album.releaseYear.ifBlank { "Unknown" },
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
