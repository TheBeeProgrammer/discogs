package com.clara.clarachallenge.ui.components.search

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.clara.clarachallenge.ui.components.utils.ErrorView
import com.clara.clarachallenge.ui.components.utils.LinearLoadingView
import com.clara.clarachallenge.ui.model.search.SearchState
import com.clara.domain.model.Artist

/**
 * Main screen for artist search functionality.
 *
 * @param modifier Modifier for customizing layout appearance.
 * @param artists Paged list of artists to display.
 * @param searchState Current state of the search operation.
 * @param onSearchQueryChange Callback invoked when search text changes.
 * @param onArtistClick Callback invoked when an artist is selected.
 * @param onRetry Callback invoked when retrying after an error.
 * @param onNotFoundArtist Callback invoked when no artists are found.
 */
@Composable
fun ArtistSearchContent(
    modifier: Modifier = Modifier,
    artists: LazyPagingItems<Artist>,
    searchState: SearchState,
    onSearchQueryChange: (String) -> Unit,
    onArtistClick: (Artist) -> Unit,
    onRetry: () -> Unit,
    onNotFoundArtist: () -> Unit,
) {
    var query by remember { mutableStateOf("") }

    Column(modifier = modifier.padding(16.dp)) {
        SearchBar(query, onQueryChange = {
            query = it
            onSearchQueryChange(it)
        })

        Spacer(modifier = Modifier.height(8.dp))

        when (searchState) {
            SearchState.Idle -> IdleView()
            is SearchState.Error -> ErrorView(message = searchState.message, onRetry = onRetry)
            is SearchState.Empty -> EmptyResultsView()
            is SearchState.Success -> ArtistListContent(
                artists = artists,
                onArtistClick = onArtistClick,
                onRetry = onRetry,
                onNotFoundArtist = onNotFoundArtist
            )
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
        label = { Text("Search Artist") },
        singleLine = true
    )
}

/**
 * Displays the list of artists with pagination handling.
 *
 * @param artists Paged list of artists to display.
 * @param onArtistClick Callback invoked when an artist is selected.
 * @param onRetry Callback invoked when retrying after an error.
 * @param onNotFoundArtist Callback invoked when no artists are found.
 */
@Composable
private fun ArtistListContent(
    artists: LazyPagingItems<Artist>,
    onArtistClick: (Artist) -> Unit,
    onRetry: () -> Unit,
    onNotFoundArtist: () -> Unit
) {
    val refreshState = artists.loadState.refresh

    when (refreshState) {
        is LoadState.Error -> {
            ErrorView(
                message = refreshState.error.localizedMessage ?: "Unknown error",
                onRetry = onRetry
            )
        }

        is LoadState.Loading -> LinearLoadingView()
        else -> {
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

                handlePagingLoadState(artists, onRetry, onNotFoundArtist)
            }
        }
    }
}

/**
 * Handles the pagination load states for the artist list.
 *
 * @param artists Paged list of artists.
 * @param onRetry Callback invoked when retrying after an error.
 * @param onNotFoundArtist Callback invoked when no artists are found.
 */
private fun LazyListScope.handlePagingLoadState(
    artists: LazyPagingItems<Artist>,
    onRetry: () -> Unit,
    onNotFoundArtist: () -> Unit
) {
    when (artists.loadState.append) {
        is LoadState.NotLoading -> {
            if (artists.itemCount == 0) {
                onNotFoundArtist()
            }
        }

        is LoadState.Loading -> item { LinearLoadingView() }
        is LoadState.Error -> item {
            ErrorView(
                message = "Failed to load more artists",
                onRetry = onRetry
            )
        }
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
            contentDescription = "Search",
            modifier = Modifier.size(64.dp)
        )
        Text("Search for your favorite artists")
    }
}