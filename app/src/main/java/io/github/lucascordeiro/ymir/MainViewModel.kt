package io.github.lucascordeiro.ymir

import androidx.lifecycle.viewModelScope
import io.github.lucascordeiro.ymir.core.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel<MainUiState, MainUiAction>(MainUiState()) {
    fun clickedLoadButton() {
        viewModelScope.launch {
            setState { state -> state.copy(isLoading = true) }

            withContext(ioDispatcher) { delay(2000L) }

            sendAction { MainUiAction.ShowToast("Hello, Android!") }
            setState { state -> state.copy(isLoading = false) }
        }
    }
}