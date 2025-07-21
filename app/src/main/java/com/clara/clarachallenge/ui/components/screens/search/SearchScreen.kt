package com.clara.clarachallenge.ui.components.screens.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.clara.clarachallenge.ui.common.Screen
import com.clara.clarachallenge.ui.components.artist.ArtistSearchContent
import com.clara.clarachallenge.ui.state.SearchArtistAction
import com.clara.clarachallenge.ui.state.SearchArtistEvent
import com.clara.clarachallenge.ui.viewmodel.SearchArtistViewModel
import com.clara.logger.Logger

@Composable
fun SearchScreen(navController: NavHostController) {
    val viewModel: SearchArtistViewModel = hiltViewModel()
    val pagedArtists = viewModel.pagedArtists.collectAsLazyPagingItems()
    val searchState by viewModel.state.collectAsState()
    val query by viewModel.searchQuery.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is SearchArtistEvent.ShowError -> {
                    /** Since the error is already displayed on screen, we simulate sending
                    it to the logger.*/
                    Logger.e(message = event.message)
                }
            }
        }
    }

    ArtistSearchContent(
        artists = pagedArtists,
        searchState = searchState,
        query = query,
        onSearchQueryChange = { query ->
            viewModel.sendAction(SearchArtistAction.Search(query))
        },
        onArtistClick = { artist ->
            navController.navigate(Screen.ArtistDetail.createRoute(artist.id))
        },
        onRetry = {
            pagedArtists.retry()
        }
    )
}

