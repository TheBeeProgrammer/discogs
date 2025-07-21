package com.clara.clarachallenge.ui.components.screens.release

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.clara.clarachallenge.ui.components.release.ReleaseListContent
import com.clara.clarachallenge.ui.state.ReleaseListAction
import com.clara.clarachallenge.ui.state.ReleaseListEvent
import com.clara.clarachallenge.ui.viewmodel.ArtistReleasesViewModel

@Composable
fun ReleaseListScreen(
    artistId: Int,
    navController: NavHostController,
    onBack: () -> Boolean,
) {
    val viewModel: ArtistReleasesViewModel = hiltViewModel()
    val releases = viewModel.pagedReleases.collectAsLazyPagingItems()
    val context = LocalContext.current

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
        onRetry = { releases.retry() }
    )
}

