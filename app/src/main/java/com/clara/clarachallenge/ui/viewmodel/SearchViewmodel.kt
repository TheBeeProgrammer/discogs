package com.clara.clarachallenge.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.clara.clarachallenge.ui.model.search.SearchArtistAction
import com.clara.clarachallenge.ui.model.search.SearchArtistEvent
import com.clara.clarachallenge.ui.model.search.SearchArtistState
import com.clara.clarachallenge.ui.viewmodel.base.BaseViewModel
import com.clara.domain.model.Pagination
import com.clara.domain.usecase.SearchArtistUseCase
import com.clara.domain.usecase.model.SearchArtistParams
import com.clara.domain.usecase.model.UseCaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchArtistViewModel @Inject constructor(
    private val searchArtistUseCase: SearchArtistUseCase
) : BaseViewModel<SearchArtistAction, SearchArtistState, SearchArtistEvent>(
    defaultState = SearchArtistState()
) {

    override fun handleAction(action: SearchArtistAction) {
        when (action) {
            is SearchArtistAction.Search -> searchArtist(action.query, action.pagination)
        }
    }

    private fun searchArtist(query: String, pagination: Pagination) {
        updateState { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            searchArtistUseCase(SearchArtistParams(query, pagination)).collect { result ->
                when (result) {
                    is UseCaseResult.Success -> {
                        updateState {
                            it.copy(
                                isLoading = false,
                                artists = result.data.data,
                                pagination = Pagination(
                                    page = result.data.currentPage,
                                    pages = result.data.totalPages
                                ),
                                errorMessage = null
                            )
                        }
                    }

                    is UseCaseResult.Failure -> {
                        val message = when (val reason = result.reason) {
                            is UseCaseResult.Reason.NotFound -> "No artists found"
                            is UseCaseResult.Reason.Unauthorized -> "You are not authorized"
                            is UseCaseResult.Reason.NoInternet -> "No internet connection"
                            is UseCaseResult.Reason.Timeout -> "Request timed out"
                            is UseCaseResult.Reason.Unknown -> reason.message
                        }

                        updateState {
                            it.copy(
                                isLoading = false,
                                errorMessage = message
                            )
                        }

                        sendEvent(SearchArtistEvent.ShowError(message))
                    }
                }
            }
        }
    }


    init {
        sendAction(SearchArtistAction.Search("Radiohead", Pagination(1, 30)))
    }
}

