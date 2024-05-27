package io.github.lucascordeiro.ymir

import io.github.lucascordeiro.ymir.core.state.UiState

data class MainUiState(
    val isLoading: Boolean = false
) : UiState
