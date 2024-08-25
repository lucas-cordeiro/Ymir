package io.github.lucascordeiro.ymir.test

import io.github.lucascordeiro.ymir.core.action.UiAction
import io.github.lucascordeiro.ymir.core.state.UiState
import io.github.lucascordeiro.ymir.core.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@ExperimentalCoroutinesApi
class ViewModelObserver<S: UiState, A: UiAction, V: ViewModel<S, A>>(
    private val viewModel: V,
    private val testDispatcher: CoroutineDispatcher
) {
    val state = mutableListOf<S>()
    val action = mutableListOf<A>()

    private lateinit var collectJobState: Job
    private lateinit var collectJobAction: Job

    fun start() {
        CoroutineScope(testDispatcher).run {
            collectJobState = launch(UnconfinedTestDispatcher()) { viewModel.state.toList(state) }
            collectJobAction = launch(UnconfinedTestDispatcher()) { viewModel.action.toList(action) }
        }
    }

    fun stop(){
        collectJobState.cancel()
        collectJobAction.cancel()
    }
}

@ExperimentalCoroutinesApi
class ViewModelStateObserver<S: UiState, V: io.github.lucascordeiro.ymir.core.state.ViewModel<S>>(
    private val viewModel: V,
    private val testDispatcher: CoroutineDispatcher
) {
    val state = mutableListOf<S>()

    private lateinit var collectJobState: Job

    fun start() {
        CoroutineScope(testDispatcher).run {
            collectJobState = launch(UnconfinedTestDispatcher()) { viewModel.state.toList(state) }
        }
    }

    fun stop(){
        collectJobState.cancel()
    }
}

@ExperimentalCoroutinesApi
class ViewModelActionObserver< A: UiAction, V: io.github.lucascordeiro.ymir.core.action.ViewModel<A>>(
    private val viewModel: V,
    private val testDispatcher: CoroutineDispatcher
) {
    val action = mutableListOf<A>()

    private lateinit var collectJobAction: Job

    fun start() {
        CoroutineScope(testDispatcher).run {
            collectJobAction = launch(UnconfinedTestDispatcher()) { viewModel.action.toList(action) }
        }
    }

    fun stop(){
        collectJobAction.cancel()
    }
}

