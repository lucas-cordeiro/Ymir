package io.github.lucascordeiro.ymir.core.action

import androidx.lifecycle.ViewModel

abstract class ViewModel<Action: UiAction> : ViewModel() {
    private val viewModelAction = Action<Action>()

    val action = viewModelAction.action

    protected suspend fun sendAction(action: () -> Action){
        viewModelAction.sendAction(action)
    }
}
