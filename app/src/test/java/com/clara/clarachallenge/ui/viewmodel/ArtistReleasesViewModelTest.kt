package com.clara.clarachallenge.ui.viewmodel

import androidx.paging.PagingData
import com.clara.clarachallenge.ui.model.release.ReleaseListAction
import com.clara.clarachallenge.ui.viewmodel.fakes.FakeArtistReleasesUseCase
import com.clara.domain.model.Releases
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ArtistReleasesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var viewModel: ArtistReleasesViewModel
    private lateinit var fakeUseCase: FakeArtistReleasesUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeUseCase = FakeArtistReleasesUseCase()
        viewModel = ArtistReleasesViewModel(fakeUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when starting, should emit empty paging data`() = testScope.runTest {
        // Arrange
        val emissions = mutableListOf<PagingData<Releases>>()
        val collectJob = launch {
            viewModel.pagedReleases.collect { emissions.add(it) }
        }

        // Assert
        assertEquals(0, emissions.size)
        assertTrue(emissions.isEmpty())

        collectJob.cancel()
    }
}
