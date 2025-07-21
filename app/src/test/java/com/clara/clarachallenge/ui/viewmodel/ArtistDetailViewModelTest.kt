package com.clara.clarachallenge.ui.viewmodel

import com.clara.clarachallenge.R
import com.clara.clarachallenge.ui.common.toUiMessage
import com.clara.clarachallenge.ui.state.ArtistDetailAction
import com.clara.clarachallenge.ui.state.ArtistDetailEvent
import com.clara.clarachallenge.ui.state.ArtistDetailState
import com.clara.clarachallenge.ui.viewmodel.fakes.FakeArtistDetailUseCase
import com.clara.clarachallenge.ui.viewmodel.fakes.FakeTextResourceProvider
import com.clara.domain.model.ArtistDetail
import com.clara.domain.usecase.base.UseCaseResult
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ArtistDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var viewModel: ArtistDetailViewModel
    private lateinit var fakeUseCase: FakeArtistDetailUseCase
    private lateinit var fakeTextResourceProvider: FakeTextResourceProvider

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeUseCase = FakeArtistDetailUseCase()
        fakeTextResourceProvider = FakeTextResourceProvider().apply {
            resources = listOf(
                R.string.error_no_internet to "No internet connection. Please try again",
                R.string.error_timeout to "Taking too long. Try again",
                R.string.error_not_found to "Artist not found",
                R.string.error_unknown to "Something went wrong"
            )
        }
        viewModel = ArtistDetailViewModel(fakeTextResourceProvider, fakeUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be Loading`() = testScope.runTest {
        assertEquals(ArtistDetailState.Loading, viewModel.state.value)
    }

    @Test
    fun `when sending LoadArtist with success then updates to Success`() = testScope.runTest {
        // Arrange
        val artist = ArtistDetail("1", "Artist Name", "image.jpg", emptyList())
        fakeUseCase.result = UseCaseResult.Success(flowOf(artist))

        // Act
        viewModel.sendAction(ArtistDetailAction.LoadArtist("123"))
        advanceUntilIdle()

        // Assert
        val state = viewModel.state.value
        assertTrue(state is ArtistDetailState.Success)
        assertEquals(artist, (state as ArtistDetailState.Success).artist)
    }

    @Test
    fun `when sending LoadArtist then use case is called with correct id`() = testScope.runTest {
        // Arrange
        val artist = ArtistDetail("1", "Artist Name", "image.jpg", emptyList())
        fakeUseCase.result = UseCaseResult.Success(flowOf(artist))

        // Act
        viewModel.sendAction(ArtistDetailAction.LoadArtist("456"))
        advanceUntilIdle()

        // Assert
        assertEquals("456", fakeUseCase.receivedId.toString())
    }

    @Test
    fun `when LoadArtist fails then updates to Error and sends event`() = testScope.runTest {
        // Arrange
        val reason = UseCaseResult.Failure(UseCaseResult.Reason.NoInternet)
        fakeUseCase.result = UseCaseResult.Failure(reason.reason)

        val events = mutableListOf<ArtistDetailEvent>()
        val job = launch {
            viewModel.events.collect {
                events.add(it)
            }
        }

        // Act
        viewModel.sendAction(ArtistDetailAction.LoadArtist("999"))
        advanceUntilIdle()

        // Assert
        val state = viewModel.state.value
        assertTrue(state is ArtistDetailState.Error)
        assertTrue(
            events.contains(
                ArtistDetailEvent.ShowError(
                    reason.reason.toUiMessage(
                        fakeTextResourceProvider
                    )
                )
            )
        )

        job.cancel()
    }

    @Test
    fun `when LoadArtist succeeds, no event is emitted`() = testScope.runTest {
        val artist = ArtistDetail("1", "Artist", "img.jpg", emptyList())
        fakeUseCase.result = UseCaseResult.Success(flowOf(artist))

        val events = mutableListOf<ArtistDetailEvent>()
        val job = launch { viewModel.events.collect { events.add(it) } }

        viewModel.sendAction(ArtistDetailAction.LoadArtist("1"))
        advanceUntilIdle()

        assertTrue(events.isEmpty())
        job.cancel()
    }

}
