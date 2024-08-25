package io.github.lucascordeiro.ymir.core.action

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.lucascordeiro.ymir.core.action.UiAction
import io.github.lucascordeiro.ymir.core.state.UiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

abstract class ViewModel<Action: UiAction> : ViewModel() {
    private val viewModelAction = Action<Action>()

    val action = viewModelAction.action

    protected suspend fun sendAction(action: () -> Action){
        viewModelAction.sendAction(action)
    }
}
