package com.clara.clarachallenge.ui.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.clara.clarachallenge.ui.common.toUiMessage
import com.clara.clarachallenge.ui.model.album.AlbumListAction
import com.clara.clarachallenge.ui.model.album.AlbumListEvent
import com.clara.clarachallenge.ui.model.album.AlbumListState
import com.clara.clarachallenge.ui.model.search.SearchState
import com.clara.clarachallenge.ui.viewmodel.base.BaseViewModel
import com.clara.domain.model.Album
import com.clara.domain.usecase.ArtistReleasesUseCase
import com.clara.domain.usecase.base.ExecutableUseCase
import com.clara.domain.usecase.model.UseCaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing the state and logic for displaying an artist's releases.
 *
 * This ViewModel handles loading albums for a specific artist using the [ArtistReleasesUseCase].
 * It exposes a [Flow] of [PagingData] for albums ([pagedAlbums]) to be observed by the UI.
 * The UI state is managed by the [BaseViewModel] and can be [AlbumListState.Loading],
 * [AlbumListState.Success], or [AlbumListState.Error].
 *
 * It takes [AlbumListAction] as input to trigger actions like loading albums.
 * It can emit [AlbumListEvent] to the UI for one-time events (currently not used in this specific implementation).
 *
 * @param getAlbumsUseCase The use case responsible for fetching artist albums.
 */
@HiltViewModel
class ArtistReleasesViewModel @Inject constructor(
    private val getAlbumsUseCase: @JvmSuppressWildcards ExecutableUseCase<Int, UseCaseResult<Flow<PagingData<Album>>>>
) : BaseViewModel<AlbumListAction, AlbumListState, AlbumListEvent>(AlbumListState.Loading) {

    private var _pagedAlbums: Flow<PagingData<Album>> = emptyFlow()
    val pagedAlbums: Flow<PagingData<Album>> get() = _pagedAlbums

    override fun handleAction(action: AlbumListAction) {
        when (action) {
            is AlbumListAction.LoadAlbums -> loadAlbums(action.artistId)
        }
    }

    /**
     * Loads albums for a given artist.
     *
     * This function is launched in the viewModelScope and updates the UI state accordingly.
     * It first sets the state to Loading.
     * Then, it calls the `getAlbumsUseCase` to fetch albums for the provided `artistId`.
     * - If the use case returns a `Success` result, the `_pagedAlbums` flow is updated with the
     *   fetched data (cached in the viewModelScope) and the UI state is set to `Success`.
     * - If the use case returns a `Failure` result, the UI state is set to `Error` with a
     *   user-friendly message derived from the failure reason.
     *
     * @param artistId The ID of the artist for whom to load albums.
     */
    private fun loadAlbums(artistId: Int) {
        viewModelScope.launch {
            updateState { AlbumListState.Loading }

            when (val result = getAlbumsUseCase(artistId)) {
                is UseCaseResult.Success -> {
                    _pagedAlbums = result.data.cachedIn(viewModelScope)
                    updateState { AlbumListState.Success }
                }

                is UseCaseResult.Failure -> {
                    updateState {
                        AlbumListState.Error(message = result.reason.toUiMessage())
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
        updateState { AlbumListState.Empty }
        sendEvent(AlbumListEvent.ShowError(error))
    }
}
