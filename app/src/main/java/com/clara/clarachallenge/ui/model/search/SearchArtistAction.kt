package com.clara.clarachallenge.ui.model.search

import com.clara.clarachallenge.ui.viewmodel.base.ViewAction
import com.clara.clarachallenge.ui.viewmodel.base.ViewEvent
import com.clara.clarachallenge.ui.viewmodel.base.ViewState
import com.clara.domain.model.Artist
import com.clara.domain.model.Pagination

sealed interface SearchArtistAction : ViewAction {
    data class Search(val query: String, val pagination: Pagination) : SearchArtistAction
}

data class SearchArtistState(
    val isLoading: Boolean = false,
    val artists: List<Artist> = emptyList(),
    val pagination: Pagination? = null,
    val errorMessage: String? = null
) : ViewState

sealed interface SearchArtistEvent : ViewEvent {
    data class ShowError(val message: String) : SearchArtistEvent
}
