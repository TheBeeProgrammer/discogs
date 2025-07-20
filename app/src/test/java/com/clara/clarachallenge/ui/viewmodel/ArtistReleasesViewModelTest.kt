package com.clara.clarachallenge.ui.viewmodel

import androidx.paging.PagingData
import com.clara.clarachallenge.ui.model.album.AlbumListAction
import com.clara.clarachallenge.ui.model.album.AlbumListState
import com.clara.clarachallenge.ui.viewmodel.fakes.FakeGetArtistAlbumsUseCase
import com.clara.domain.model.Album
import com.clara.domain.usecase.model.UseCaseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ArtistReleasesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var viewModel: ArtistReleasesViewModel
    private lateinit var fakeUseCase: FakeGetArtistAlbumsUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeUseCase = FakeGetArtistAlbumsUseCase()
        viewModel = ArtistReleasesViewModel(fakeUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be Loading`() = testScope.runTest {
        assertEquals(AlbumListState.Loading, viewModel.state.value)
    }

    @Test
    fun `onPagingError updates state to Empty`() = testScope.runTest {
        viewModel.onPagingError("Test error")
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(AlbumListState.Empty, viewModel.state.value)
    }

    @Test
    fun `sendAction LoadAlbums with success updates state to Success`() = testScope.runTest {
        val album = Album("id1", "Album title", "image_url")
        val pagingData = PagingData.from(listOf(album))
        fakeUseCase.result = UseCaseResult.Success(flowOf(pagingData))

        viewModel.sendAction(AlbumListAction.LoadAlbums(123))
        testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.state.value is AlbumListState.Success)
    }

    @Test
    fun `sendAction LoadAlbums with failure updates state to Error`() = testScope.runTest {
        fakeUseCase.result = UseCaseResult.Failure(UseCaseResult.Reason.NoInternet)

        viewModel.sendAction(AlbumListAction.LoadAlbums(123))
        testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.state.value is AlbumListState.Error)
    }
}
