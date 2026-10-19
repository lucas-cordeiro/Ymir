package io.github.lucascordeiro.ymir.core.action

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ActionViewModelTest {

    private data class Navigate(val route: String) : UiAction

    private class NavigationViewModel : ViewModel<Navigate>() {
        suspend fun open(route: String) = sendAction { Navigate(route) }
    }

    @Test
    fun sendActionEmitsTheAction() = runTest {
        val viewModel = NavigationViewModel()

        viewModel.action.test {
            viewModel.open("home")
            assertEquals(Navigate("home"), awaitItem())
        }
    }
}
