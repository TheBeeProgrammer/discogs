package com.clara.clarachallenge.ui.components.screens.release

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.clara.clarachallenge.R
import com.clara.clarachallenge.ui.components.release.ReleaseListContent
import com.clara.clarachallenge.ui.model.release.ReleaseListAction
import com.clara.clarachallenge.ui.model.release.ReleaseListEvent
import com.clara.clarachallenge.ui.viewmodel.ArtistReleasesViewModel

@Composable
fun ReleaseListScreen(
    artistId: Int,
    navController: NavHostController,
    onBack: () -> Boolean,
) {
    val viewModel: ArtistReleasesViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val releases = viewModel.pagedReleases.collectAsLazyPagingItems()
    val context = LocalContext.current
    val errorMessage = stringResource(R.string.releases_not_found)

    LaunchedEffect(Unit) {
        viewModel.sendAction(ReleaseListAction.LoadReleases(artistId))
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ReleaseListEvent.ShowError -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    ReleaseListContent(
        releases = releases,
        releaseListState = state,
        onRetry = { releases.retry() },
        onNotFoundReleases = { viewModel.onPagingError(errorMessage) }
    )
}

