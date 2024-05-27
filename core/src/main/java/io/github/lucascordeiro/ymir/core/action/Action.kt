package io.github.lucascordeiro.ymir.core.action

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class Action<Action: UiAction> {

    private val _action = Channel<Action>(Channel.BUFFERED)
    val action = _action.receiveAsFlow()

    suspend fun sendAction(action: () -> Action) {
        _action.send(action())
    }
}