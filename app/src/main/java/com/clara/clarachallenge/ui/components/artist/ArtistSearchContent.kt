package com.clara.clarachallenge.ui.components.artist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.clara.clarachallenge.R
import com.clara.clarachallenge.ui.components.shared.ErrorView
import com.clara.clarachallenge.ui.components.shared.LinearLoadingView
import com.clara.clarachallenge.ui.state.SearchState
import com.clara.domain.model.Artist

/**
 * Main screen for artist search functionality.
 *
 * @param modifier Modifier for customizing layout appearance.
 * @param artists Paged list of artists to display.
 * @param searchState Current state of the search operation.
 * @param query Current search query text.
 * @param onSearchQueryChange Callback invoked when search text changes.
 * @param onArtistClick Callback invoked when an artist is selected.
 * @param onRetry Callback invoked when retrying after an error.
 */
@Composable
fun ArtistSearchContent(
    modifier: Modifier = Modifier,
    artists: LazyPagingItems<Artist>,
    searchState: SearchState,
    query: String,
    onSearchQueryChange: (String) -> Unit,
    onArtistClick: (Artist) -> Unit,
    onRetry: () -> Unit,
) {
    Column(modifier = modifier.padding(16.dp)) {
        SearchBar(
            query = query,
            onQueryChange = { newQuery ->
                onSearchQueryChange(newQuery)
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (query.isBlank() && searchState is SearchState.Idle) {
            IdleView()
        } else {
            ArtistListContent(artists, onArtistClick, onRetry)
        }
    }
}

/**
 * Search input field component.
 *
 * @param query Current search query text.
 * @param onQueryChange Callback invoked when the search text changes.
 * @param modifier Modifier for customizing layout appearance.
 */
@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.search_artist_hint)) },
        singleLine = true
    )
}

/**
 * Displays the list of artists with pagination handling.
 *
 * @param artists Paged list of artists to display.
 * @param onArtistClick Callback invoked when an artist is selected.
 * @param onRetry Callback invoked when retrying after an error.
 */
@Composable
private fun ArtistListContent(
    artists: LazyPagingItems<Artist>,
    onArtistClick: (Artist) -> Unit,
    onRetry: () -> Unit,
) {
    val refreshState = artists.loadState.refresh
    when (refreshState) {
        is LoadState.Error -> ErrorView(
            throwable = refreshState.error,
            onRetry = onRetry
        )

        is LoadState.Loading -> LinearLoadingView()
        else -> {
            if (artists.itemCount == 0) {
                EmptyResultsView()
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 8.dp)
                ) {
                    items(artists.itemCount) { index ->
                        artists[index]?.let { artist ->
                            ArtistItem(artist = artist, onClick = { onArtistClick(artist) })
                        }
                    }

                    handlePagingLoadState(artists, onRetry)
                }
            }
        }
    }
}

/**
 * Handles the pagination load states for the artist list.
 *
 * @param artists Paged list of artists.
 * @param onRetry Callback invoked when retrying after an error.
 */
private fun LazyListScope.handlePagingLoadState(
    artists: LazyPagingItems<Artist>,
    onRetry: () -> Unit,
) {
    when (artists.loadState.append) {
        is LoadState.Loading -> item { LinearLoadingView() }
        is LoadState.Error -> item {
            ErrorView(
                message = stringResource(R.string.load_more_artists_error),
                onRetry = onRetry
            )
        }

        else -> {}
    }
}

/**
 * Displays an empty state view when no results are found.
 */
@Composable
private fun EmptyResultsView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("No results found")
    }
}

/**
 * Displays the initial idle state with search prompt.
 */
@Composable
private fun IdleView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = stringResource(R.string.search_icon_content_description),
            modifier = Modifier.size(64.dp)
        )
        Text(stringResource(R.string.search_prompt))
    }
}