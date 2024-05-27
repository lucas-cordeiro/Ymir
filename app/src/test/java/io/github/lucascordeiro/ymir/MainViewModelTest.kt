package io.github.lucascordeiro.ymir

import io.github.lucascordeiro.ymir.test.ViewModelObserver
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: MainViewModel
    private val observer by lazy {
        ViewModelObserver(
            viewModel = viewModel,
            testDispatcher = mainDispatcherRule.testDispatcher
        )
    }

    @Before
    fun setup() {
        viewModel = MainViewModel(
            ioDispatcher = mainDispatcherRule.testDispatcher
        )
    }

    @Test
    fun shouldSetStateWhenClickedButton() = runTest() {
        observer.start()

        val initialState = MainUiState()
        val loadingState = MainUiState(isLoading = true)
        val state = observer.state

        viewModel.clickedLoadButton()

        advanceUntilIdle()

        assertEquals(initialState, state[0])
        assertEquals(loadingState, state[1])
        assertEquals(initialState, state[2])

        observer.stop()
    }

    @Test
    fun shouldSendToastActionWhenClickedButton() = runTest {
        observer.start()

        val action = observer.action
        val toastMessage = "Hello, Android!"

        viewModel.clickedLoadButton()

        advanceUntilIdle()

        val toastAction = action[0] as MainUiAction.ShowToast
        assertEquals(toastMessage, toastAction.message)

        observer.stop()
    }
}