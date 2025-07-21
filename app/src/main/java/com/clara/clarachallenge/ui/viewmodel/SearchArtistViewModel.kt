package com.clara.clarachallenge.ui.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.clara.clarachallenge.ui.model.search.SearchArtistAction
import com.clara.clarachallenge.ui.model.search.SearchArtistEvent
import com.clara.clarachallenge.ui.model.search.SearchState
import com.clara.clarachallenge.ui.viewmodel.SearchArtistViewModel.Companion.DEBOUNCE_PERIOD
import com.clara.clarachallenge.ui.viewmodel.base.BaseViewModel
import com.clara.domain.model.Artist
import com.clara.domain.usecase.base.ExecutableUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

/**
 * [SearchArtistViewModel] is responsible for managing the state and logic for searching artists.
 * It consumes [SearchArtistAction]s and updates the UI state via [SearchState] and [SearchArtistEvent].
 *
 * This ViewModel observes changes in the search query and reacts by providing a debounced, distinct,
 * and cancellable flow of [PagingData] for [Artist]s, fetched via the provided use case.
 *
 * @property searchArtistsUseCase A use case that returns a [Flow] of paginated artist data
 * based on a search query string.
 */
@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class SearchArtistViewModel @Inject constructor(
    private val searchArtistsUseCase: @JvmSuppressWildcards ExecutableUseCase<String, Flow<PagingData<Artist>>>
) : BaseViewModel<SearchArtistAction, SearchState, SearchArtistEvent>(
    defaultState = SearchState.Idle
) {

    companion object {
        private const val DEBOUNCE_PERIOD = 300L // 300 milliseconds
    }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()


    /**
     * A [Flow] of [PagingData] representing the paginated list of artists.
     *
     * This flow is derived from the [searchQuery] StateFlow. It incorporates several operators
     * to optimize search behavior:
     * - **debounce**: It waits for a specified period ([DEBOUNCE_PERIOD]) after the last
     *   character is typed before emitting the query. This prevents excessive API calls
     *   while the user is typing.
     * - **distinctUntilChanged**: It only emits a new query if it's different from the
     *   previous one, further reducing redundant searches.
     * - **flatMapLatest**: This operator is crucial for handling rapidly changing queries.
     *   When a new query is emitted, it cancels any ongoing work (API call) from the previous
     *   query and starts a new one with the latest query. It then flattens the resulting
     *   Flow<PagingData<Artist>> from `handleQuery` into the main flow.
     *
     * The actual fetching and state management logic is delegated to the [handleQuery] function.
     */
    val pagedArtists: Flow<PagingData<Artist>> = searchQuery
        .debounce(DEBOUNCE_PERIOD)
        .distinctUntilChanged()
        .flatMapLatest { query -> handleQuery(query) }

    /**
     * Handles the search query by either returning an empty flow if the query is blank or
     * performing a search and returning a flow of paged artist data if the query is not blank.
     *
     * @param query The search query string.
     * @return A flow of [PagingData] containing [Artist] objects.
     */
    private suspend fun handleQuery(query: String): Flow<PagingData<Artist>> {
        return if (query.isBlank()) {
            handleBlankQuery()
        } else {
            val result = searchArtistsUseCase(query)
            handleSearchSuccess(result)
        }
    }

    /**
     * Handles the case when the search query is blank.
     * It updates the state to Idle and returns an empty flow of PagingData.
     *
     * @return An empty flow of PagingData<Artist>.
     */
    private fun handleBlankQuery(): Flow<PagingData<Artist>> {
        updateState { SearchState.Idle }
        return emptyFlow()
    }

    /**
     * Handles a successful search result.
     * It caches the flow of paginated artist data within the [viewModelScope].
     * Note: This function previously updated the state to [SearchState.Success].
     * This responsibility has been moved to the collector of [pagedArtists]
     * to ensure state updates reflect the latest emitted data.
     *
     * @param result The successful result from the use case, containing a flow of [PagingData].
     * @return A [Flow] of [PagingData] containing [Artist] objects, cached in [viewModelScope].
     */
    private fun handleSearchSuccess(result: Flow<PagingData<Artist>>): Flow<PagingData<Artist>> {
        return result.cachedIn(viewModelScope)
    }

    override fun handleAction(action: SearchArtistAction) {
        when (action) {
            is SearchArtistAction.Search -> {
                onSearchQueryChange(action.query)
            }
        }
    }

    /**
     * Updates the search query.
     * This function is called when the user types in the search bar.
     *
     * @param query The new search query.
     */
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }
}
