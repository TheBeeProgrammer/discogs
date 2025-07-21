package com.clara.clarachallenge.ui.viewmodel

import androidx.paging.PagingData
import com.clara.clarachallenge.ui.viewmodel.fakes.FakeSearchArtistUseCase
import com.clara.domain.model.Artist
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
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
    fun `empty query should return empty flow`() = testScope.runTest {
        // Arrange
        val collectedData = mutableListOf<PagingData<Artist>>()
        val collectJob = launch {
            viewModel.pagedArtists.collect { collectedData.add(it) }
        }

        // Act
        viewModel.onSearchQueryChange("")
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertTrue(collectedData.isEmpty())

        collectJob.cancel()
    }

    @Test
    fun `changing query cancels previous search`() = testScope.runTest {
        // Arrange
        val slowFlow = flow<PagingData<Artist>> {
            delay(1000) // Simulate slow network
            emit(PagingData.empty())
        }
        fakeUseCase.searchResult = slowFlow

        val collectedData = mutableListOf<PagingData<Artist>>()
        val collectJob = launch {
            viewModel.pagedArtists.collect { collectedData.add(it) }
        }

        // Act - Start first search
        viewModel.onSearchQueryChange("first")
        testDispatcher.scheduler.advanceTimeBy(DEBOUNCE_TIME)

        // Change query before first search completes
        viewModel.onSearchQueryChange("second")
        testDispatcher.scheduler.advanceTimeBy(DEBOUNCE_TIME)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(2, fakeUseCase.callCount)
        assertEquals("second", fakeUseCase.lastReceivedQuery)
        // Should only have data from second search (first was cancelled)
        assertEquals(1, collectedData.size)

        collectJob.cancel()
    }

    @Test
    fun `identical queries don't trigger new search`() = testScope.runTest {
        // Arrange
        fakeUseCase.searchResult = flowOf(PagingData.empty())

        val collectJob = launch {
            viewModel.pagedArtists.collect {}
        }

        // Act
        viewModel.onSearchQueryChange("same")
        testDispatcher.scheduler.advanceTimeBy(DEBOUNCE_TIME)

        viewModel.onSearchQueryChange("same")
        testDispatcher.scheduler.advanceTimeBy(DEBOUNCE_TIME)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(1, fakeUseCase.callCount) // Only one search

        collectJob.cancel()
    }
}
