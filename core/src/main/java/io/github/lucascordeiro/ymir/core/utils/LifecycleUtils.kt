package io.github.lucascordeiro.ymir.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import io.github.lucascordeiro.ymir.core.action.UiAction
import io.github.lucascordeiro.ymir.core.state.UiState
import io.github.lucascordeiro.ymir.core.viewmodel.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

object LifecycleUtils {
    @Composable
    fun <S: UiState, A: UiAction, T: ViewModel<S, A>>ObserveActions(
        viewModel: T,
        handleAction: (A) -> Unit
    ) {
        val lifecycle = LocalLifecycleOwner.current.lifecycle

        LaunchedEffect(viewModel, lifecycle)  {
            lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                withContext(Dispatchers.Main.immediate) {
                    viewModel.action.collect(handleAction)
                }
            }
        }
    }
}