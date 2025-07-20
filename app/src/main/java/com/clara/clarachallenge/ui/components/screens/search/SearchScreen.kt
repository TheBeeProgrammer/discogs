package com.clara.clarachallenge.ui.components.screens.search

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.clara.clarachallenge.ui.components.search.ArtistSearchContent
import com.clara.clarachallenge.ui.model.search.SearchArtistAction
import com.clara.clarachallenge.ui.model.search.SearchArtistEvent
import com.clara.clarachallenge.ui.viewmodel.SearchArtistViewModel

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
                    Log.d("SearchScreen", event.message)
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
            navController.navigate("artistDetail/${artist.id}")
        },
        onRetry = {
            pagedArtists.retry()
        },
        onNotFoundArtist = {
            viewModel.onPagingError("Artists not found")
        }
    )
}

