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
 * This state class is intentionally left minimal because the screen uses Paging 3,
 * which provides its own mechanism for managing loading, error, and empty states via
 * the `LoadState` APIs exposed through `LazyPagingItems`.
 *
 * As a result, we avoid duplicating UI state and rely directly on Paging's `loadState.refresh`,
 * `loadState.append`, and `itemCount` to drive the screen behavior.
 *
 * For more information, see the
 * [official Paging 3 documentation](https://developer.android.com/topic/libraries/architecture/paging/v3-overview).
 *
 * This sealed class remains in place in case additional view-level state is needed
 * in the future (e.g., transient flags or UI-level state unrelated to paging).
 */
sealed class ReleaseListState : ViewState {
    object Idle : ReleaseListState()
}

