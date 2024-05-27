package io.github.lucascordeiro.ymir.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.lucascordeiro.ymir.core.action.UiAction
import io.github.lucascordeiro.ymir.core.state.UiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

abstract class ViewModel<State: UiState, Action: UiAction>(
    initialState: State
) : ViewModel() {
    private val viewModelState = io.github.lucascordeiro.ymir.core.state.State(initialState)
    private val viewModelAction = io.github.lucascordeiro.ymir.core.action.Action<Action>()

    val state = viewModelState.state.stateIn(
        scope = viewModelScope,
        initialValue = initialState,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
    )
    val action = viewModelAction.action

    protected suspend fun setState(state: (State) -> State){
        viewModelState.setState(state)
    }

    protected suspend fun sendAction(action: () -> Action){
        viewModelAction.sendAction(action)
    }
}
