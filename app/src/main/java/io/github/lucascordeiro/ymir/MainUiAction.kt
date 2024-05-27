package io.github.lucascordeiro.ymir

import io.github.lucascordeiro.ymir.core.action.UiAction

sealed class MainUiAction : UiAction {
    data class ShowToast(val message: String) : MainUiAction()
}