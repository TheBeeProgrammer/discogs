package com.clara.clarachallenge.ui.model.release

import com.clara.clarachallenge.ui.viewmodel.base.ViewAction
import com.clara.clarachallenge.ui.viewmodel.base.ViewEvent
import com.clara.clarachallenge.ui.viewmodel.base.ViewState

/**
 * Represents the actions that can be performed on the release list screen.
 * These actions are typically triggered by user interactions.
 */
sealed interface ReleaseListAction : ViewAction {
    data class LoadReleases(val artistId: Int) : ReleaseListAction
}

/**
 * Represents events that can be emitted by the release list feature.
 * These events are typically used to communicate one-time actions or results
 * from the ViewModel to the View (e.g., showing an error message).
 */
sealed interface ReleaseListEvent : ViewEvent {
    data class ShowError(val message: String) : ReleaseListEvent
}

/**
 * Represents the different states of the release list screen.
 *
 * - [Idle]: The initial state, where no data has been loaded yet.
 * - [Loading]: Indicates that releases are currently being fetched or processed.
 * - [Success]: Signifies that releases were successfully loaded and are available.
 * - [Empty]: Indicates that no releases were found for the specified criteria.
 * - [Error]: Represents a state where an error occurred during the process of loading releases.
 *   Contains an error [message] detailing the issue.
 */
sealed class ReleaseListState : ViewState {
    object Loading : ReleaseListState()
}
