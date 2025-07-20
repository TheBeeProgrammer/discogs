package com.clara.clarachallenge.ui.viewmodel.base

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

sealed class TestAction : ViewAction {
    object Increment : TestAction()
    object Decrement : TestAction()
    object ShowMessage : TestAction()
}

data class TestState(val count: Int) : ViewState

sealed class TestEvent : ViewEvent {
    data class MessageShown(val text: String) : TestEvent()
}

@OptIn(ExperimentalCoroutinesApi::class)
class BaseViewModelTest {

    private lateinit var testViewModel: TestConcreteViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        testViewModel = TestConcreteViewModel()
    }

    @Test
    fun `initial state should be set correctly`() {
        assertEquals(TestState(count = 0), testViewModel.state.value)
    }

    @Test
    fun `state should increment correctly`() {
        testViewModel.sendAction(TestAction.Increment)
        assertEquals(TestState(count = 1), testViewModel.state.value)
    }

    @Test
    fun `state should decrement correctly`() {
        testViewModel.sendAction(TestAction.Decrement)
        assertEquals(TestState(count = -1), testViewModel.state.value)
    }

    @Test
    fun `should handle multiple actions sequentially`() {
        testViewModel.sendAction(TestAction.Increment)
        testViewModel.sendAction(TestAction.Increment)
        testViewModel.sendAction(TestAction.Decrement)

        assertEquals(TestState(count = 1), testViewModel.state.value)
    }

    private class TestConcreteViewModel : BaseViewModel<TestAction, TestState, TestEvent>(
        defaultState = TestState(count = 0)
    ) {
        override fun handleAction(action: TestAction) {
            when (action) {
                TestAction.Increment -> updateState { it.copy(count = it.count + 1) }
                TestAction.Decrement -> updateState { it.copy(count = it.count - 1) }
                TestAction.ShowMessage -> sendEvent(TestEvent.MessageShown("Message!"))
            }
        }
    }
}
