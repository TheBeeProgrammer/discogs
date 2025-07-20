package com.clara.clarachallenge.ui.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.clara.clarachallenge.ui.model.search.SearchArtistAction
import com.clara.clarachallenge.ui.model.search.SearchArtistEvent
import com.clara.clarachallenge.ui.model.search.SearchState
import com.clara.clarachallenge.ui.viewmodel.base.BaseViewModel
import com.clara.domain.model.Artist
import com.clara.domain.usecase.SearchArtistUseCase
import com.clara.domain.usecase.model.UseCaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class SearchArtistViewModel @Inject constructor(
    private val searchArtistsUseCase: SearchArtistUseCase
) : BaseViewModel<SearchArtistAction, SearchState, SearchArtistEvent>(
    defaultState = SearchState.Idle
) {

    companion object {
        private const val DEBOUNCE_PERIOD = 300L // 300 milliseconds
    }

    private val searchQuery = MutableStateFlow("")

    /**
     * A [Flow] of [PagingData] representing the paginated list of artists.
     * It observes changes in [searchQuery], applies a debounce period,
     * ensures distinct queries, and then fetches artists based on the query.
     * - If the query is blank, it updates the state to [SearchState.Idle] and emits an empty flow.
     * - If the query is not blank:
     *   - It calls [searchArtistsUseCase] to fetch artists.
     *   - On success, it updates the state to [SearchState.Success] and emits the cached [PagingData].
     *   - On failure, it updates the state to an appropriate [SearchState.Error] based on the [UseCaseResult.Reason]
     *     and emits an empty flow.
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
            handleSearchQuery(query)
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
     * Handles the search query by calling the searchArtistsUseCase and processing the result.
     *
     * @param query The search query string.
     * @return A Flow of PagingData containing the search results (artists) or an empty flow in case of failure.
     */
    private suspend fun handleSearchQuery(query: String): Flow<PagingData<Artist>> {
        return when (val result = searchArtistsUseCase(query)) {
            is UseCaseResult.Success -> handleSearchSuccess(result)
            is UseCaseResult.Failure -> handleSearchFailure(result)
        }
    }

    /**
     * Handles a successful search result.
     * Updates the state to [SearchState.Success] and returns the flow of paginated artist data,
     * caching it within the [viewModelScope].
     *
     * @param result The successful result from the use case, containing a flow of [PagingData].
     * @return A [Flow] of [PagingData] containing [Artist] objects, cached in [viewModelScope].
     */
    private fun handleSearchSuccess(result: UseCaseResult.Success<Flow<PagingData<Artist>>>): Flow<PagingData<Artist>> {
        updateState { SearchState.Success }
        return result.data.cachedIn(viewModelScope)
    }

    /**
     * Handles the failure of a search operation.
     * It maps the [UseCaseResult.Reason] to a specific [SearchState.Error] and updates the UI state.
     *
     * @param result The [UseCaseResult.Failure] object containing the reason for the failure.
     * @return An empty [Flow] of [PagingData] as the search failed.
     */
    private fun handleSearchFailure(result: UseCaseResult.Failure): Flow<PagingData<Artist>> {
        val errorState = when (result.reason) {
            UseCaseResult.Reason.Unauthorized -> SearchState.Error("No Authorization")
            UseCaseResult.Reason.NoInternet -> SearchState.Error("No internet connection")
            UseCaseResult.Reason.Timeout -> SearchState.Error("Timeout")
            else -> SearchState.Error("Unknown Error: ${result.reason}")
        }
        updateState { errorState }
        return emptyFlow()
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
        searchQuery.value = query
    }

    /**
     * Handles errors that occur during the paging process.
     * It updates the state to [SearchState.Empty] and sends an event to show the error message.
     *
     * @param error The error message string to be displayed.
     */
    fun onPagingError(error: String) {
        updateState { SearchState.Empty }
        sendEvent(SearchArtistEvent.ShowError(error))
    }
}
