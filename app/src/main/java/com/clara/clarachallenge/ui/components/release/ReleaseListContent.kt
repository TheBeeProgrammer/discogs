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
import com.clara.clarachallenge.ui.components.utils.ErrorView
import com.clara.clarachallenge.ui.components.utils.LinearLoadingView
import com.clara.clarachallenge.ui.components.utils.mapErrorToMessage
import com.clara.domain.model.Releases

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
                message = mapErrorToMessage(refreshState.error),
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

@Composable
fun EmptyView() {
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