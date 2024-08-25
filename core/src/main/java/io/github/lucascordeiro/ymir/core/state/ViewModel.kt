package io.github.lucascordeiro.ymir.core.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.lucascordeiro.ymir.core.action.UiAction
import io.github.lucascordeiro.ymir.core.state.UiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

abstract class ViewModel<State: UiState>(
    initialState: State
) : ViewModel() {
    private val viewModelState = State(initialState)

    val state = viewModelState.state.stateIn(
        scope = viewModelScope,
        initialValue = initialState,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
    )

    protected suspend fun setState(state: (State) -> State){
        viewModelState.setState(state)
    }

}
