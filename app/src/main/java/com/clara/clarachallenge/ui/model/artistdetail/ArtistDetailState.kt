package com.clara.clarachallenge.ui.model.artistdetail

import com.clara.clarachallenge.ui.viewmodel.base.ViewAction
import com.clara.clarachallenge.ui.viewmodel.base.ViewEvent
import com.clara.clarachallenge.ui.viewmodel.base.ViewState
import com.clara.domain.model.ArtistDetail
import kotlinx.coroutines.flow.Flow

/**
 * Represents the different states of the artist detail screen.
 * This sealed class encapsulates all possible UI states that the artist detail view can be in.
 *
 * - [Loading]: Indicates that the artist details are currently being fetched.
 * - [Success]: Indicates that the artist details have been successfully fetched and are available.
 *   - [artist]: A [Flow] emitting the [ArtistDetail] object.
 * - [Error]: Indicates that an error occurred while fetching the artist details.
 *   - [message]: A [String] containing the error message to be displayed.
 */
sealed class ArtistDetailState : ViewState {
    object Loading : ArtistDetailState()
    data class Success(val artist: ArtistDetail) : ArtistDetailState()
    data class Error(val message: String) : ArtistDetailState()
}

/**
 * Represents the events that can be emitted from the ArtistDetailViewModel.
 * These events are typically used to trigger one-time actions in the UI,
 * such as showing a toast or navigating to another screen.
 */
sealed interface ArtistDetailEvent : ViewEvent {
    data class ShowError(val message: String) : ArtistDetailEvent
}

/**
 * Represents the actions that can be performed on the Artist Detail screen.
 * These actions are typically triggered by user interactions or lifecycle events.
 */
sealed interface ArtistDetailAction : ViewAction {
    data class LoadArtist(val artistId: Int) : ArtistDetailAction
}
