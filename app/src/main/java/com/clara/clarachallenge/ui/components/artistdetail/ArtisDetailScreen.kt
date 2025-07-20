package com.clara.clarachallenge.ui.components.artistdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.clara.clarachallenge.ui.components.utils.CachedImage
import com.clara.clarachallenge.ui.components.utils.CircularLoadingView
import com.clara.clarachallenge.ui.components.utils.Divider
import com.clara.clarachallenge.ui.components.utils.ErrorView
import com.clara.clarachallenge.ui.model.artistdetail.ArtistDetailState
import com.clara.domain.model.ArtistDetail

/**
 * Composable function that displays the content of the artist detail screen.
 *
 * @param artistDetailState The current state of the artist detail screen.
 * @param onRetry A callback function to be invoked when the user wants to retry fetching the artist details.
 */
@Composable
fun ArtistDetailContent(artistDetailState: ArtistDetailState, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when (artistDetailState) {
            is ArtistDetailState.Loading -> CircularLoadingView()
            is ArtistDetailState.Error -> ErrorView(message = artistDetailState.message) { onRetry() }
            is ArtistDetailState.Success -> ArtistDetailsView(artist = artistDetailState.artist)
        }
    }
}

/**
 * Displays the detailed information of an artist, including their image, name, profile, and members.
 *
 * @param artist The [ArtistDetail] object containing the artist's information.
 */
@Composable
fun ArtistDetailsView(artist: ArtistDetail) {
    Column {
        artist.imageUrl?.let {
            CachedImage(
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.CenterHorizontally),
                imageUrl = it,
                contentDescription = "Image of ${artist.name}"
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Divider()
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = artist.name,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (!artist.profile.isNullOrBlank()) {
                Text(
                    text = artist.profile!!,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (artist.members.isNotEmpty()) {
                Text(
                    text = "Members:",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                artist.members.forEach { member ->
                    val active = if (member.active) "Active" else "Inactive"
                    Column {
                        Text(
                            text = "• ${member.name} $active",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
