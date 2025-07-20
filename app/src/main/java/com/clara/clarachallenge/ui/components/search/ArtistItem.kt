package com.clara.clarachallenge.ui.components.search

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.clara.clarachallenge.R
import com.clara.clarachallenge.ui.components.utils.CachedImage
import com.clara.clarachallenge.ui.components.utils.Divider
import com.clara.clarachallenge.ui.components.utils.debouncedClickable
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .debouncedClickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CachedImage(
            imageUrl = artist.imageUrl,
            contentDescription = artist.name,
            modifier = Modifier.size(48.dp),
            placeholder = painterResource(R.drawable.ic_loading),
            error = painterResource(R.drawable.ic_error)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = artist.name,
            style = MaterialTheme.typography.bodyLarge
        )
    }

    Divider(color = Color.LightGray, paddingStart = 16.dp, paddingEnd = 16.dp)
}
