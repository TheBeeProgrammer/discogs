package com.clara.clarachallenge.ui.components.screens.search

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.clara.clarachallenge.ui.components.search.ArtistSearchContent
import com.clara.clarachallenge.ui.model.search.SearchArtistAction
import com.clara.clarachallenge.ui.model.search.SearchArtistEvent
import com.clara.clarachallenge.ui.theme.ClarachallengeTheme
import com.clara.clarachallenge.ui.viewmodel.SearchArtistViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClarachallengeTheme {
                Scaffold(modifier = Modifier.Companion.fillMaxSize()) { innerPadding ->

                    val viewModel: SearchArtistViewModel = hiltViewModel()
                    val pagedArtists = viewModel.pagedArtists.collectAsLazyPagingItems()
                    val searchState by viewModel.state.collectAsState()

                    LaunchedEffect(Unit) {
                        viewModel.events.collect { event ->
                            when (event) {
                                is SearchArtistEvent.ShowError -> {
                                    Log.d(SearchScreen::class.java.simpleName, event.message)
                                }
                            }
                        }
                    }

                    ArtistSearchContent(
                        modifier = Modifier.Companion.padding(innerPadding),
                        artists = pagedArtists,
                        searchState = searchState,
                        onSearchQueryChange = { query ->
                            viewModel.sendAction(SearchArtistAction.Search(query))
                        },
                        onArtistClick = { artist ->
                        },
                        onRetry = {
                            pagedArtists.retry()
                        },
                        onNotFoundArtist = {
                            viewModel.onPagingError("Artists not found")
                        }
                    )
                }
            }
        }
    }
}