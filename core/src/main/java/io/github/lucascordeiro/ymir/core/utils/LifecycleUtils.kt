package io.github.lucascordeiro.ymir.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import io.github.lucascordeiro.ymir.core.action.UiAction
import io.github.lucascordeiro.ymir.core.state.UiState
import io.github.lucascordeiro.ymir.core.viewmodel.ViewModel

object LifecycleUtils {
    @Composable
    fun <S: UiState, A: UiAction, T: ViewModel<S, A>>ObserveActions(
        viewModel: T,
        handleAction: (A) -> Unit
    ) {
        LaunchedEffect(Unit) {
            viewModel.action.collect(handleAction)
        }
    }
}