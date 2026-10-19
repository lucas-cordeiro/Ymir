package io.github.lucascordeiro.ymir.core.state

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class StateViewModelTest {

    private data class NameState(val name: String = "") : UiState

    private class NameViewModel : ViewModel<NameState>(NameState()) {
        suspend fun rename(name: String) = setState { it.copy(name = name) }
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
    fun setStateEmitsTheNewState() = runTest {
        val viewModel = NameViewModel()

        viewModel.state.test {
            assertEquals(NameState(), awaitItem())
            viewModel.rename("Ymir")
            assertEquals(NameState("Ymir"), awaitItem())
        }
    }
}
