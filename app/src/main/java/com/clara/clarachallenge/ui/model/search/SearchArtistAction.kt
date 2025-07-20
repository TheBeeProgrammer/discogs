package com.clara.clarachallenge.ui.model.search

import com.clara.clarachallenge.ui.viewmodel.base.ViewAction
import com.clara.clarachallenge.ui.viewmodel.base.ViewEvent
import com.clara.clarachallenge.ui.viewmodel.base.ViewState

/**
 * Represents the actions that can be performed on the search artist screen.
 * These actions are typically triggered by user interactions.
 */
sealed interface SearchArtistAction : ViewAction {
    data class Search(val query: String) : SearchArtistAction
}

/**
 * Represents events that can be emitted by the search artist feature.
 * These events are typically used to communicate one-time actions or results
 * from the ViewModel to the View (e.g., showing an error message).
 */
sealed interface SearchArtistEvent : ViewEvent {
    data class ShowError(val message: String) : SearchArtistEvent
}

/**
 * Represents the different states of the search functionality.
 *
 * - [Idle]: The initial state, no search has been performed.
 * - [Success]: The search was successful and results are available.
 * - [Empty]: The search was successful but no results were found.
 * - [Error]: An error occurred during the search.
 */
sealed class SearchState : ViewState {
    object Idle : SearchState()
    object Success : SearchState()
    object Empty : SearchState()
    data class Error(val message: String) : SearchState()
}
