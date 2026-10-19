package io.github.lucascordeiro.ymir.core.viewmodel

import app.cash.turbine.test
import io.github.lucascordeiro.ymir.core.action.UiAction
import io.github.lucascordeiro.ymir.core.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelTest {

    private data class CounterState(val count: Int = 0) : UiState

    private sealed interface CounterAction : UiAction {
        data object ShowDone : CounterAction
        data class ShowError(val message: String) : CounterAction
    }

    private class CounterViewModel : ViewModel<CounterState, CounterAction>(CounterState()) {
        suspend fun increment() = setState { it.copy(count = it.count + 1) }
        suspend fun done() = sendAction { CounterAction.ShowDone }
        suspend fun fail(message: String) = sendAction { CounterAction.ShowError(message) }
    }

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun startsWithTheInitialState() {
        assertEquals(CounterState(), CounterViewModel().state.value)
    }

    @Test
    fun setStateEmitsTheNewState() = runTest {
        val viewModel = CounterViewModel()

        viewModel.state.test {
            assertEquals(CounterState(0), awaitItem())
            viewModel.increment()
            assertEquals(CounterState(1), awaitItem())
            viewModel.increment()
            assertEquals(CounterState(2), awaitItem())
        }
    }

    @Test
    fun sendActionDeliversActionsInOrder() = runTest {
        val viewModel = CounterViewModel()

        viewModel.action.test {
            viewModel.done()
            viewModel.fail("timeout")
            assertEquals(CounterAction.ShowDone, awaitItem())
            assertEquals(CounterAction.ShowError("timeout"), awaitItem())
        }
    }

    @Test
    fun actionsSentBeforeCollectingAreKept() = runTest {
        val viewModel = CounterViewModel()
        viewModel.done()

        viewModel.action.test {
            assertEquals(CounterAction.ShowDone, awaitItem())
        }
    }

    @Test
    fun eachActionReachesASingleCollector() = runTest {
        val viewModel = CounterViewModel()
        val received = mutableListOf<CounterAction>()
        val collectors = UnconfinedTestDispatcher(testScheduler)
        val first = launch(collectors) { viewModel.action.collect { received += it } }
        val second = launch(collectors) { viewModel.action.collect { received += it } }

        viewModel.done()
        viewModel.fail("timeout")

        assertEquals(listOf(CounterAction.ShowDone, CounterAction.ShowError("timeout")), received)
        first.cancel()
        second.cancel()
    }
}
