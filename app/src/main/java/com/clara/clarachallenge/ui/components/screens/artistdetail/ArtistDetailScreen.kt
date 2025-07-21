package com.clara.clarachallenge.ui.components.screens.artistdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.clara.clarachallenge.ui.common.Screen
import com.clara.clarachallenge.ui.components.artist.ArtistDetailContent
import com.clara.clarachallenge.ui.state.ArtistDetailAction
import com.clara.clarachallenge.ui.state.ArtistDetailEvent
import com.clara.clarachallenge.ui.theme.ClarachallengeTheme
import com.clara.clarachallenge.ui.viewmodel.ArtistDetailViewModel
import com.clara.logger.Logger

/**
 * Composable function that displays the artist detail screen.
 *
 * This screen shows detailed information about a specific artist, including their releases.
 * It observes the state from [ArtistDetailViewmodel] to update the UI and handles
 * events such as error messages.
 *
 * @param artistId The ID of the artist to display details for.
 * @param onBack A lambda function to be invoked when the user navigates back.
 */
@Composable
fun ArtistDetailScreen(
    artistId: String,
    onBack: () -> Unit,
    navController: NavHostController
) {
    ClarachallengeTheme {
        val viewModel: ArtistDetailViewModel = hiltViewModel()
        val state by viewModel.state.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.sendAction(ArtistDetailAction.LoadArtist(artistId))
        }

        LaunchedEffect(Unit) {
            viewModel.events.collect { event ->
                when (event) {
                    is ArtistDetailEvent.ShowError -> {
                        /** Since the error is already displayed on screen, we simulate sending
                        it to the logger.*/
                        Logger.e(message = "Error loading artist detail: ${event.message}")
                    }
                }
            }
        }

        ArtistDetailContent(
            artistDetailState = state,
            onRetry = { viewModel.sendAction(ArtistDetailAction.LoadArtist(artistId)) },
            viewReleasesClick = {
                navController.navigate(Screen.Releases.createRoute(artistId))
            })
    }
}
