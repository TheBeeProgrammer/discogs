package com.clara.clarachallenge.ui.components.screens.album

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.clara.clarachallenge.ui.components.albums.AlbumListContent
import com.clara.clarachallenge.ui.model.album.AlbumListAction
import com.clara.clarachallenge.ui.model.album.AlbumListEvent
import com.clara.clarachallenge.ui.viewmodel.ArtistReleasesViewModel

@Composable
fun AlbumListScreen(
    artistId: Int,
    navController: NavHostController,
    onBack: () -> Boolean,
) {
    val viewModel: ArtistReleasesViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val albums = viewModel.pagedAlbums.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        viewModel.sendAction(AlbumListAction.LoadAlbums(artistId))
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is AlbumListEvent.ShowError -> {
                    Log.e("AlbumListScreen", event.message)
                }
            }
        }
    }

    AlbumListContent(
        albums = albums,
        albumListState = state,
        onRetry = { albums.retry() },
        onNotFoundAlbums = { viewModel.onPagingError("Albums not found") }
    )
}

