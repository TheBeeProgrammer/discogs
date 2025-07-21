package com.clara.clarachallenge.ui.components.artistdetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.clara.clarachallenge.R
import com.clara.clarachallenge.ui.components.utils.CachedImage
import com.clara.clarachallenge.ui.components.utils.CircularLoadingView
import com.clara.clarachallenge.ui.components.utils.Divider
import com.clara.clarachallenge.ui.components.utils.ErrorView
import com.clara.clarachallenge.ui.components.utils.debouncedClickable
import com.clara.clarachallenge.ui.model.artistdetail.ArtistDetailState
import com.clara.domain.model.ArtistDetail
import com.clara.domain.model.ArtistMembers

/**
 * Composable function that displays the content of the artist detail screen.
 * It handles different states of the artist detail data (loading, error, success)
 * and displays the appropriate UI for each state.
 *
 * @param artistDetailState The current state of the artist detail data.
 * @param onRetry A callback function to be invoked when the user wants to retry fetching the data in case of an error.
 * @param viewReleasesClick A callback function to be invoked when the user clicks on the "View Releases" button.
 */
@Composable
fun ArtistDetailContent(
    artistDetailState: ArtistDetailState,
    onRetry: () -> Unit,
    viewReleasesClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when (artistDetailState) {
            is ArtistDetailState.Loading -> CircularLoadingView()
            is ArtistDetailState.Error -> ErrorView(
                message = artistDetailState.message,
                onRetry = onRetry
            )

            is ArtistDetailState.Success -> ArtistDetailsView(
                artist = artistDetailState.artist,
                onViewReleasesClick = viewReleasesClick
            )
        }
    }
}

/**
 * Displays the detailed information of an artist.
 *
 * @param artist The [ArtistDetail] object containing the artist's information.
 * @param onViewReleasesClick A lambda function to be invoked when the "View Releases" button is clicked.
 */
@Composable
fun ArtistDetailsView(
    artist: ArtistDetail,
    onViewReleasesClick: () -> Unit
) {
    Column {
        ArtistImage(imageUrl = artist.imageUrl, name = artist.name)
        ArtistName(name = artist.name)
        ViewReleasesButton(onClick = onViewReleasesClick)
        Divider()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            artist.profile?.takeIf { it.isNotBlank() }?.let {
                ArtistProfile(profile = it)
            }

            if (artist.members.isNotEmpty()) {
                ArtistMembers(members = artist.members)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

/**
 * Composable function to display the artist's image.
 *
 * @param imageUrl The URL of the artist's image. Can be null if no image is available.
 * @param name The name of the artist, used for content description.
 */
@Composable
private fun ArtistImage(imageUrl: String?, name: String) {
    imageUrl?.let {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            CachedImage(
                imageUrl = it,
                contentDescription = stringResource(
                    R.string.artist_image_content_description,
                    android.R.attr.name
                ),
                modifier = Modifier
                    .size(180.dp)
                    .align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

/**
 * Composable function to display the name of the artist.
 *
 * @param name The name of the artist.
 */
@Composable
private fun ArtistName(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}

/**
 * Composable function that displays a button to view releases.
 *
 * @param onClick Callback function to be executed when the button is clicked.
 */
@Composable
private fun ViewReleasesButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
            .debouncedClickable { onClick() }
            .padding(top = 8.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            painter = painterResource(id = R.drawable.ic_album),
            contentDescription = stringResource(
                id = R.string.release_content_description
            ),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.releases_label),
            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary)
        )
    }
}

/**
 * Displays the artist's profile information.
 *
 * @param profile The artist's profile text.
 */
@Composable
private fun ArtistProfile(profile: String) {
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = stringResource(R.string.artist_profile_title),
        style = MaterialTheme.typography.titleMedium
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        text = profile,
        style = MaterialTheme.typography.bodyMedium
    )
}

/**
 * Displays a list of artist members.
 *
 * @param members The list of [ArtistMembers] to display.
 */
@Composable
private fun ArtistMembers(members: List<ArtistMembers>) {
    Spacer(modifier = Modifier.height(24.dp))
    Text(
        text = stringResource(R.string.artist_members_title),
        style = MaterialTheme.typography.titleMedium
    )
    Spacer(modifier = Modifier.height(8.dp))
    members.forEach { member ->
        val status =
            if (member.active) stringResource(R.string.artist_member_active) else stringResource(R.string.artist_member_inactive)
        Text(
            text = "• ${member.name} $status",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 2.dp)
        )
    }
}
