package com.clara.clarachallenge.ui.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.clara.clarachallenge.ui.model.release.ReleaseListAction
import com.clara.clarachallenge.ui.model.release.ReleaseListEvent
import com.clara.clarachallenge.ui.model.release.ReleaseListState
import com.clara.clarachallenge.ui.viewmodel.base.BaseViewModel
import com.clara.domain.model.Releases
import com.clara.domain.usecase.artist.ArtistReleasesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing the state and logic for displaying an artist's releases.
 *
 * This ViewModel handles loading releases for a specific artist using the [ArtistReleasesUseCase].
 * It exposes a [StateFlow] of [PagingData] for releases ([pagedReleases]) to be observed by the UI.
 * The UI state, managed by the [BaseViewModel], transitions through [ReleaseListState.Loading],
 * [ReleaseListState.Success], or [ReleaseListState.Error] based on the data loading process.
 *
 * It processes [ReleaseListAction] to trigger actions like loading releases.
 * One-time UI events can be communicated via [ReleaseListEvent], though this is not
 * actively used in the current implementation.
 *
 * @param artistReleasesUseCase The use case responsible for fetching artist releases.
 */
@HiltViewModel
class ArtistReleasesViewModel @Inject constructor(
    private val artistReleasesUseCase: @JvmSuppressWildcards ExecutableUseCase<Int, UseCaseResult<Flow<PagingData<Album>>>>
) : BaseViewModel<ReleaseListAction, ReleaseListState, ReleaseListEvent>(ReleaseListState.Idle) {

    private val _pagedReleases = MutableStateFlow<PagingData<Releases>>(PagingData.empty())
    val pagedReleases: StateFlow<PagingData<Releases>> get() = _pagedReleases


    override fun handleAction(action: ReleaseListAction) {
        when (action) {
            is ReleaseListAction.LoadReleases -> loadReleases(action.artistId)
        }
    }

    /**
     * Loads releases for a given artist.
     *
     * This function is launched in the `viewModelScope`.
     * It calls the `artistReleasesUseCase` to fetch a [Flow] of [PagingData] for the
     * provided `artistId`.
     * The fetched data is then cached in the `viewModelScope` to survive configuration changes.
     * Finally, it collects the `PagingData` and updates the `_pagedReleases` StateFlow,
     * which in turn notifies the UI of the new data.
     *
     * Note: Error handling and UI state updates (Loading, Success, Error) are expected
     * to be managed by the Paging library itself or within the `collect` block if specific
     * side-effects beyond data emission are needed. In this current implementation,
     * the focus is on populating `_pagedReleases`.
     *
     * @param artistId The ID of the artist for whom to load releases.
     */
    private fun loadReleases(artistId: Int) {
        viewModelScope.launch {
            artistReleasesUseCase(artistId)
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                    _pagedReleases.value = pagingData
                }
        }
    }
}
