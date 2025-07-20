package com.clara.clarachallenge.ui.model.album

import com.clara.clarachallenge.ui.viewmodel.base.ViewAction
import com.clara.clarachallenge.ui.viewmodel.base.ViewEvent
import com.clara.clarachallenge.ui.viewmodel.base.ViewState
import com.clara.domain.model.Album

/**
 * Represents the actions that can be performed on the album list screen.
 * These actions are typically triggered by user interactions.
 */
sealed interface AlbumListAction : ViewAction {
    data class LoadAlbums(val artistId: Int) : AlbumListAction
}

/**
 * Represents events that can be emitted by the album list feature.
 * These events are typically used to communicate one-time actions or results
 * from the ViewModel to the View (e.g., showing an error message).
 */
sealed interface AlbumListEvent : ViewEvent {
    data class ShowError(val message: String) : AlbumListEvent
}

/**
 * Represents the different states of the album list screen.
 *
 * - [Idle]: The initial state, where no data has been loaded yet.
 * - [Loading]: Indicates that albums are currently being fetched or processed.
 * - [Success]: Signifies that albums were successfully loaded and are available.
 * - [Empty]: Indicates that no albums were found for the specified artist.
 * - [Error]: Represents a state where an error occurred during the process of loading albums.
 *   Contains an error [message] detailing the issue.
 */
sealed class AlbumListState : ViewState {
    object Idle : AlbumListState()
    object Loading : AlbumListState()
    object Success : AlbumListState()
    object Empty : AlbumListState()
    data class Error(val message: String) : AlbumListState()
}
