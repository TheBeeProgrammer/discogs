package com.clara.clarachallenge.ui.components.release

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.clara.clarachallenge.R
import com.clara.clarachallenge.ui.components.utils.CircularLoadingView
import com.clara.clarachallenge.ui.components.utils.ErrorView
import com.clara.clarachallenge.ui.components.utils.LinearLoadingView
import com.clara.clarachallenge.ui.model.release.ReleaseListState
import com.clara.domain.model.Releases

@Composable
fun ReleaseListContent(
    releases: LazyPagingItems<Releases>,
    releaseListState: ReleaseListState,
    onRetry: () -> Unit,
    onNotFoundReleases: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.releases_label),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
        )

        when (releaseListState) {
            is ReleaseListState.Error -> ErrorView(
                message = releaseListState.message,
                onRetry = onRetry
            )

            is ReleaseListState.Success -> {
                val refreshState = releases.loadState.refresh
                when (refreshState) {
                    is LoadState.Error -> ErrorView(
                        message = refreshState.error.localizedMessage
                            ?: stringResource(R.string.unknown_error),
                        onRetry = onRetry
                    )

                    is LoadState.Loading -> LinearLoadingView()
                    else -> LazyColumn(
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
                            onNotFoundReleases = onNotFoundReleases,
                        )
                    }
                }
            }

            else -> {}
        }
    }
}


private fun LazyListScope.handleReleasePagingLoadState(
    releases: LazyPagingItems<Releases>,
    onRetry: () -> Unit,
    onNotFoundReleases: () -> Unit
) {
    when (releases.loadState.append) {
        is LoadState.NotLoading -> {
            if (releases.itemCount == 0) onNotFoundReleases()
        }

        is LoadState.Loading -> item { LinearLoadingView() }
        is LoadState.Error -> item {
            ErrorView(message = stringResource(R.string.load_more_releases_error)) {
                onRetry()
            }
        }
    }
}
