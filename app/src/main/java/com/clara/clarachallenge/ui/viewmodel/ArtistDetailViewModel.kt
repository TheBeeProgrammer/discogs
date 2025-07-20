package com.clara.clarachallenge.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.clara.clarachallenge.ui.common.toUiMessage
import com.clara.clarachallenge.ui.model.artistdetail.ArtistDetailAction
import com.clara.clarachallenge.ui.model.artistdetail.ArtistDetailEvent
import com.clara.clarachallenge.ui.model.artistdetail.ArtistDetailState
import com.clara.clarachallenge.ui.viewmodel.base.BaseViewModel
import com.clara.domain.model.ArtistDetail
import com.clara.domain.usecase.artist.ArtistDetailUseCase
import com.clara.domain.usecase.base.ExecutableUseCase
import com.clara.domain.usecase.base.UseCaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing the state and logic of the artist detail screen.
 *
 * This ViewModel interacts with the [ArtistDetailUseCase] to fetch artist details and updates
 * the UI state accordingly. It follows the MVI (Model-View-Intent) pattern, where actions
 * trigger state updates and events are sent to the UI for side effects.
 *
 * @param useCase The use case for fetching artist details.
 */
@HiltViewModel
class ArtistDetailViewModel @Inject constructor(
    private val useCase: @JvmSuppressWildcards ExecutableUseCase<Int, UseCaseResult<Flow<ArtistDetail>>>
) : BaseViewModel<ArtistDetailAction, ArtistDetailState, ArtistDetailEvent>(
    defaultState = ArtistDetailState.Loading
) {

    override fun handleAction(action: ArtistDetailAction) {
        when (action) {
            is ArtistDetailAction.LoadArtist -> loadArtist(action.artistId)
        }
    }

    /**
     * Loads the artist details for the given [artistId].
     *
     * This function launches a coroutine in the [viewModelScope] to fetch the artist details
     * from the [repository].
     *
     * - If the operation is successful, it updates the state to [ArtistDetailState.Success]
     *   with the fetched artist data.
     * - If the operation fails, it updates the state to [ArtistDetailState.Error] with an
     *   appropriate UI message and sends an [ArtistDetailEvent.ShowError] event with the
     *   same message.
     *
     * @param artistId The ID of the artist to load.
     */
    private fun loadArtist(artistId: String) {
        viewModelScope.launch {
            when (val result = useCase(artistId.toInt())) {
                is UseCaseResult.Success -> {
                    result.data.collect { artistDetail ->
                        updateState { ArtistDetailState.Success(artistDetail) }
                    }
                }

                is UseCaseResult.Failure -> {
                    updateState { ArtistDetailState.Error(result.reason.toUiMessage()) }
                    sendEvent(ArtistDetailEvent.ShowError(result.reason.toUiMessage()))
                }
            }
        }
    }
}
