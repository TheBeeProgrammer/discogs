package com.clara.clarachallenge.ui.components.screens.artistdetail

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.clara.clarachallenge.ui.components.artistdetail.ArtistDetailContent
import com.clara.clarachallenge.ui.model.artistdetail.ArtistDetailAction
import com.clara.clarachallenge.ui.model.artistdetail.ArtistDetailEvent
import com.clara.clarachallenge.ui.theme.ClarachallengeTheme
import com.clara.clarachallenge.ui.viewmodel.ArtistDetailViewModel

/**
 * Composable function that displays the artist detail screen.
 *
 * This screen shows detailed information about a specific artist, including their albums.
 * It observes the state from [ArtistDetailViewModel] to update the UI and handles
 * events such as error messages.
 *
 * @param artistId The ID of the artist to display details for.
 * @param onBack A lambda function to be invoked when the user navigates back.
 */
@Composable
fun ArtistDetailScreen(
    artistId: Int,
    onBack: () -> Unit
) {
    ClarachallengeTheme {
        val viewModel: ArtistDetailViewModel = hiltViewModel()
        val state by viewModel.state.collectAsState()
        val context = LocalContext.current

        LaunchedEffect(Unit) {
            viewModel.sendAction(ArtistDetailAction.LoadArtist(artistId))
        }

        LaunchedEffect(Unit) {
            viewModel.events.collect { event ->
                when (event) {
                    is ArtistDetailEvent.ShowError -> {
                        Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        ArtistDetailContent(state) {
            viewModel.sendAction(ArtistDetailAction.LoadArtist(artistId))
        }
    }
}
