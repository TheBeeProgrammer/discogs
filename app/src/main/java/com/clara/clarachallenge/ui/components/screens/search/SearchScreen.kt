package com.clara.clarachallenge.ui.components.screens.search

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.clara.clarachallenge.ui.common.Screen
import com.clara.clarachallenge.ui.components.artist.ArtistSearchContent
import com.clara.clarachallenge.ui.state.SearchArtistAction
import com.clara.clarachallenge.ui.state.SearchArtistEvent
import com.clara.clarachallenge.ui.viewmodel.SearchArtistViewModel

@Composable
fun SearchScreen(navController: NavHostController) {
    val viewModel: SearchArtistViewModel = hiltViewModel()
    val pagedArtists = viewModel.pagedArtists.collectAsLazyPagingItems()
    val searchState by viewModel.state.collectAsState()
    val query by viewModel.searchQuery.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is SearchArtistEvent.ShowError -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT)
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

