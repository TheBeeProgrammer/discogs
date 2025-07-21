package com.clara.clarachallenge.ui.components.release

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
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
import com.clara.domain.model.Releases

/**
 * Displays a list of releases with pagination and loading/error states.
 *
 * @param releases The [LazyPagingItems] containing the releases to display.
 * @param onRetry Callback invoked when the user requests to retry loading data after an error.
 */
@Composable
fun ReleaseListContent(
    releases: LazyPagingItems<Releases>,
    onRetry: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.releases_label),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
        )

        val refreshState = releases.loadState.refresh
        when (refreshState) {
            is LoadState.Error -> ErrorView(
                throwable = refreshState.error,
                onRetry = onRetry
            )

            is LoadState.Loading -> LinearLoadingView()
            else -> if (releases.itemCount == 0) {
                EmptyView()
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    items(releases.itemCount) { index ->
                        releases[index]?.let { album ->
                            ReleaseListItem(releases = album)
                        }
                    }

                    handleReleasePagingLoadState(
                        releases = releases,
                        onRetry = onRetry,
                    )
                }
            }
        }
    }
}


/**
 * Composable function that displays a view indicating that there are no releases to show.
 * It shows a centered text message.
 */
@Composable
private fun EmptyView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.empty_releases_message),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

/**
 * Handles the load state for release paging.
 *
 * This function is an extension of [LazyListScope] and is responsible for displaying a loading
 * indicator or an error message based on the current load state of the `releases` [LazyPagingItems].
 *
 * - If the `append` load state is [LoadState.Loading], a [LinearLoadingView] is displayed as an item.
 * - If the `append` load state is [LoadState.Error], an [ErrorView] is displayed as an item,
 *   allowing the user to retry the operation via the [onRetry] callback.
 * - For any other load state, nothing is displayed.
 *
 * @param releases The [LazyPagingItems] representing the paginated list of releases.
 * @param onRetry A lambda function to be invoked when the user wishes to retry loading more releases
 *                after an error.
 */
private fun LazyListScope.handleReleasePagingLoadState(
    releases: LazyPagingItems<Releases>,
    onRetry: () -> Unit,
) {
    when (releases.loadState.append) {
        is LoadState.Loading -> item { LinearLoadingView() }
        is LoadState.Error -> item {
            ErrorView(message = stringResource(R.string.load_more_releases_error)) {
                onRetry()
            }
        }

        else -> {}
    }
}