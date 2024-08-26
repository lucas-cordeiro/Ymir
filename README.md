[![Maven Central](https://img.shields.io/maven-central/v/io.github.lucas-cordeiro.ymir/core.svg)](https://search.maven.org/artifact/io.github.lucas-cordeiro.ymir/core)
[![Kotlin](https://img.shields.io/badge/kotlin-1.9.23-blue.svg?logo=kotlin)](http://kotlinlang.org)
![badge-android](http://img.shields.io/badge/platform-android-6EDB8D.svg?style=flat)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

# Ymir: Custom ViewModel Library for Android

Welcome to **Ymir**! This library provides a flexible and efficient way to manage UI state and actions in your Android applications using a custom implementation of ViewModel. The library is designed to simplify the handling of UI state changes and actions while leveraging the power of Kotlin's coroutines and Flow APIs.

## Table of Contents

- [Features](#features)
- [Getting Started](#getting-started)
- [Usage](#usage)
  - [Implementing a ViewModel](#implementing-a-viewmodel)
  - [Using with Jetpack Compose](#using-with-jetpack-compose)
- [Testing](#testing)
- [Demo](#demo)
- [Contributions and Feedback](#contributions-and-feedback)

## Features

- **State Management**: Easily manage UI state and actions.
- **Jetpack Compose Integration**: Seamlessly integrate with Jetpack Compose.
- **Coroutine Support**: Built-in support for Kotlin coroutines for asynchronous operations.

## Getting Started

To get started with this library, add the following dependency to your `build.gradle`:

```groovy
dependencies {
    implementation 'io.github.lucas-cordeiro.ymir:core:[last-version]'
}
```
Replace [last-version] with the latest version available on [Maven Central](https://central.sonatype.com/artifact/io.github.lucas-cordeiro.ymir/core).

## Usage
### Implementing a ViewModel

Create your `UiState` and `UiAction` by extending the interfaces provided by the library:

```kotlin
import io.github.lucascordeiro.ymir.core.state.UiState
import io.github.lucascordeiro.ymir.core.action.UiAction

data class HomeUiState(
    val isLoading: Boolean = false
) : UiState

sealed class HomeUiAction : UiAction {
    data class ShowToast(val message: String) : HomeUiAction()
}
```

Create your ViewModel by extending the custom ViewModel class provided by the library:

```kotlin

import io.github.lucascordeiro.ymir.core.viewmodel.ViewModel

class HomeViewModel() : ViewModel<HomeUiState, HomeUiAction>(HomeUiState()) {
    // ViewModel implementation
}
```

To update the `UiState`, you need to call `setState`:
```kotlin
viewModelScope.launch {
    setState { state -> state.copy(isLoading = true) }
}
```

To send an `UiAction` need to call `sendAction`:
```kotlin
viewModelScope.launch {
    sendAction { MainUiAction.ShowToast("Hello, Android!") }
}
```

**Note**: Both `setState` and `sendAction` are `suspend` functions.

### Using with Jetpack Compose

In your Jetpack Compose UI, you can easily observe state and handle actions using the provided utility functions. Here's how:

```kotlin

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveActions(
        viewModel = viewModel,
        handleAction = ::handleAction
    )

    // UI Code using state
}

fun handleAction(action: HomeUiAction) {
    when (action) {
        is HomeUiAction.ShowToast -> {
            // Show toast message
        }
    }
}
```

## Testing
This library includes support for testing ViewModel implementations by providing a utility class, `ViewModelObserver`, that simplifies the management of `UiState` and `UiAction` during tests.

To use the testing utilities provided by the library, add the following dependency to the `testImplementation` section of your `build.gradle`:

```groovy
dependencies {
    testImplementation 'io.github.lucas-cordeiro.ymir:test:[last-version]'
}
```
Here's an example of how to write tests using the `ViewModelObserver`:

```kotlin
@get:Rule
val mainDispatcherRule = MainDispatcherRule() // Not included in library

private lateinit var viewModel: HomeViewModel
private val observer by lazy {
    ViewModelObserver(
        viewModel = viewModel,
        testDispatcher = mainDispatcherRule.testDispatcher
    )
}

@Before
fun setup() {
    viewModel = HomeViewModel()
}

@Test
fun someTest() = runTest {
    observer.start()

    val state = observer.state
    val action = observer.action

    // Simulate user interactions, such as button clicks

    advanceUntilIdle()

    // Add asserts to verify the expected outcomes

    observer.stop()
}
```

## Demo

For a demonstration of how to implement and use this library, check out the `HomeActivity` in the `app` module of this repository.
Additionally, the `app` module includes a unit test example for the ViewModel, demonstrating how to test `UiState` and `UiAction` using the `ViewModelObserver`.

## Contributions and Feedback

As the sole developer of this library, I am open to suggestions, improvements, and feedback to make it even better! If you have any ideas, encounter a bug, or would like to share your thoughts, feel free to open an issue.

Your contributions and feedback are highly appreciated as they help enhance the quality and functionality of this library.
