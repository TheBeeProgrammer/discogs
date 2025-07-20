package com.clara.clarachallenge.ui.viewmodel

import androidx.paging.PagingData
import com.clara.clarachallenge.ui.model.search.SearchState
import com.clara.clarachallenge.ui.viewmodel.fakes.FakeSearchArtistUseCase
import com.clara.domain.model.Artist
import com.clara.domain.usecase.model.UseCaseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchArtistViewModelTest {

    companion object {
        private const val DEBOUNCE_TIME = 301L
    }

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var viewModel: SearchArtistViewModel
    private lateinit var fakeUseCase: FakeSearchArtistUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeUseCase = FakeSearchArtistUseCase()
        viewModel = SearchArtistViewModel(fakeUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be Idle`() = testScope.runTest {
        assertEquals(SearchState.Idle, viewModel.state.value)
    }

    @Test
    fun `onPagingError updates state to Empty`() = testScope.runTest {
        viewModel.onPagingError("Test error")
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(SearchState.Empty, viewModel.state.value)
    }

    @Test
    fun `onSearchQueryChange triggers Idle state`() = testScope.runTest {
        // Arrange
        val pagingFlow = flowOf(PagingData.empty<Artist>())
        fakeUseCase.result = UseCaseResult.Success(pagingFlow)

        // Act
        val collectJob = launch {
            viewModel.pagedArtists.collect { }
        }

        viewModel.onSearchQueryChange("beatles")
        testDispatcher.scheduler.advanceTimeBy(DEBOUNCE_TIME)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals("beatles", fakeUseCase.receivedQuery)

        collectJob.cancel()
    }


    @Test
    fun `onSearchQueryChange triggers failure state`() = testScope.runTest {
        // Arrange
        fakeUseCase.result = UseCaseResult.Failure(UseCaseResult.Reason.NoInternet)

        val collectJob = launch {
            viewModel.pagedArtists.collect {}
        }

        // Act
        viewModel.onSearchQueryChange("fail")
        testDispatcher.scheduler.advanceTimeBy(DEBOUNCE_TIME)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assert(viewModel.state.value is SearchState.Error)

        collectJob.cancel()
    }
}
