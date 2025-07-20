package com.clara.clarachallenge.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.clara.clarachallenge.ui.common.toUiMessage
import com.clara.clarachallenge.ui.model.artistdetail.ArtistDetailAction
import com.clara.clarachallenge.ui.model.artistdetail.ArtistDetailEvent
import com.clara.clarachallenge.ui.model.artistdetail.ArtistDetailState
import com.clara.clarachallenge.ui.viewmodel.base.BaseViewModel
import com.clara.domain.repositories.ArtistDetailRepository
import com.clara.domain.usecase.model.UseCaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistDetailViewModel @Inject constructor(
    private val repository: ArtistDetailRepository
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
    private fun loadArtist(artistId: Int) {
        viewModelScope.launch {
            when (val result = repository.getArtistDetail(artistId)) {
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
