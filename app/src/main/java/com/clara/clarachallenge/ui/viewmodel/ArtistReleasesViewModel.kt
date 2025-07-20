package com.clara.clarachallenge.ui.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.clara.clarachallenge.ui.common.toUiMessage
import com.clara.clarachallenge.ui.model.release.ReleaseListAction
import com.clara.clarachallenge.ui.model.release.ReleaseListEvent
import com.clara.clarachallenge.ui.model.release.ReleaseListState
import com.clara.clarachallenge.ui.model.search.SearchState
import com.clara.clarachallenge.ui.viewmodel.base.BaseViewModel
import com.clara.domain.model.Releases
import com.clara.domain.usecase.ArtistReleasesUseCase
import com.clara.domain.usecase.model.UseCaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing the state and logic for displaying an artist's releases.
 *
 * This ViewModel handles loading releases for a specific artist using the [ArtistReleasesUseCase].
 * It exposes a [Flow] of [PagingData] for releases ([pagedReleases]) to be observed by the UI.
 * The UI state is managed by the [BaseViewModel] and can be [ReleaseListState.Loading],
 * [ReleaseListState.Success], or [ReleaseListState.Error].
 *
 * It takes [ReleaseListAction] as input to trigger actions like loading releases.
 * It can emit [ReleaseListEventListEvent] to the UI for one-time events (currently not used in this specific implementation).
 *
 * @param getReleasesUseCase The use case responsible for fetching artist releases.
 */
@HiltViewModel
class ArtistReleasesViewModel @Inject constructor(
    private val artistReleasesUseCase: ArtistReleasesUseCase
) : BaseViewModel<ReleaseListAction, ReleaseListState, ReleaseListEvent>(ReleaseListState.Loading) {

    private var _pagedReleases: Flow<PagingData<Releases>> = emptyFlow()
    val pagedReleases: Flow<PagingData<Releases>> get() = _pagedReleases

    override fun handleAction(action: ReleaseListAction) {
        when (action) {
            is ReleaseListAction.LoadReleases -> loadReleases(action.artistId)
        }
    }

    /**
     * Loads releases for a given artist.
     *
     * This function is launched in the viewModelScope and updates the UI state accordingly.
     * It first sets the state to Loading.
     * Then, it calls the `getReleasesUseCase` to fetch releases for the provided `artistId`.
     * - If the use case returns a `Success` result, the `_pagedReleases` flow is updated with the
     *   fetched data (cached in the viewModelScope) and the UI state is set to `Success`.
     * - If the use case returns a `Failure` result, the UI state is set to `Error` with a
     *   user-friendly message derived from the failure reason.
     *
     * @param artistId The ID of the artist for whom to load releases.
     */
    private fun loadReleases(artistId: Int) {
        viewModelScope.launch {
            updateState { ReleaseListState.Loading }

            when (val result = artistReleasesUseCase(artistId)) {
                is UseCaseResult.Success -> {
                    _pagedReleases = result.data.cachedIn(viewModelScope)
                    updateState { ReleaseListState.Success }
                }

                is UseCaseResult.Failure -> {
                    updateState {
                        ReleaseListState.Error(message = result.reason.toUiMessage())
                    }
                }
            }
        }
    }

    /**
     * Handles errors that occur during the paging process.
     * It updates the state to [SearchState.Empty] and sends an event to show the error message.
     *
     * @param error The error message string to be displayed.
     */
    fun onPagingError(error: String) {
        updateState { ReleaseListState.Empty }
        sendEvent(ReleaseListEvent.ShowError(error))
    }
}
