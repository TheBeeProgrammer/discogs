package com.clara.clarachallenge.ui.components.albums

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.clara.clarachallenge.ui.components.utils.CircularLoadingView
import com.clara.clarachallenge.ui.components.utils.ErrorView
import com.clara.clarachallenge.ui.components.utils.LinearLoadingView
import com.clara.clarachallenge.ui.model.album.AlbumListState
import com.clara.domain.model.Album

@Composable
fun AlbumListContent(
    albums: LazyPagingItems<Album>,
    albumListState: AlbumListState,
    onRetry: () -> Unit,
    onNotFoundAlbums: () -> Unit
) {
    when (albumListState) {
        is AlbumListState.Idle -> IdleView()
        is AlbumListState.Error -> ErrorView(message = albumListState.message) {
            onRetry()
        }

        is AlbumListState.Success -> {
            val refreshState = albums.loadState.refresh

            when (refreshState) {
                is LoadState.Error -> ErrorView(
                    message =
                        refreshState.error.localizedMessage ?: "Unknown error"
                ) {
                    onRetry()
                }

                is LoadState.Loading -> LinearLoadingView()
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        items(albums.itemCount) { index ->
                            albums[index]?.let { album ->
                                AlbumListItem(album = album)
                            }
                        }

                        handleAlbumPagingLoadState(albums, onRetry, onNotFoundAlbums)
                    }
                }
            }
        }

        else -> {}
    }
}

private fun LazyListScope.handleAlbumPagingLoadState(
    albums: LazyPagingItems<Album>,
    onRetry: () -> Unit,
    onNotFoundAlbums: () -> Unit
) {
    when (albums.loadState.append) {
        is LoadState.NotLoading -> {
            if (albums.itemCount == 0) onNotFoundAlbums()
        }

        is LoadState.Loading -> item { LinearLoadingView() }
        is LoadState.Error -> item {
            ErrorView(message = "Failed to load more albums") {
                onRetry()
            }
        }
    }
}

@Composable
private fun IdleView() {
    CircularLoadingView()
}
