package io.github.lucascordeiro.ymir.core.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class State<State : UiState>(initialState: State) {
    private val _state: MutableStateFlow<State> = MutableStateFlow(initialState)
    val state: StateFlow<State> = _state

    suspend fun setState(state: (State) -> State){
        _state.emit(state(_state.value))
    }
}