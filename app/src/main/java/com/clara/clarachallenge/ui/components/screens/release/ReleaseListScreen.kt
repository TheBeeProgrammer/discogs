package com.clara.clarachallenge.ui.components.screens.release

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.clara.clarachallenge.ui.components.release.ReleaseListContent
import com.clara.clarachallenge.ui.state.ReleaseListAction
import com.clara.clarachallenge.ui.state.ReleaseListEvent
import com.clara.clarachallenge.ui.viewmodel.ArtistReleasesViewModel
import com.clara.logger.Logger

@Composable
fun ReleaseListScreen(
    artistId: Int
) {
    val viewModel: ArtistReleasesViewModel = hiltViewModel()
    val releases = viewModel.pagedReleases.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        viewModel.sendAction(ReleaseListAction.LoadReleases(artistId))
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ReleaseListEvent.ShowError -> {
                    /** Since the error is already displayed on screen, we simulate sending
                    it to the logger.*/
                    Logger.e(message = event.message)
                }
            }
        }
    }

    ReleaseListContent(
        releases = releases,
        onRetry = { releases.retry() }
    )
}
