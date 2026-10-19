[![Maven Central](https://img.shields.io/maven-central/v/io.github.lucas-cordeiro.ymir/core.svg)](https://central.sonatype.com/artifact/io.github.lucas-cordeiro.ymir/core)
[![Kotlin](https://img.shields.io/badge/kotlin-2.4.20-blue.svg?logo=kotlin)](http://kotlinlang.org)
![badge-android](http://img.shields.io/badge/platform-android-6EDB8D.svg?style=flat)
![badge-ios](http://img.shields.io/badge/platform-ios-CDCDCD.svg?style=flat)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

# Ymir: Custom ViewModel Library for Kotlin Multiplatform

Welcome to **Ymir**! This library provides a flexible and efficient way to manage UI state and actions in your Android and iOS applications using a custom implementation of ViewModel. The library is designed to simplify the handling of UI state changes and actions while leveraging the power of Kotlin's coroutines and Flow APIs.

## Table of Contents

- [Features](#features)
- [Getting Started](#getting-started)
- [Migrating from 1.x](#migrating-from-1x)
- [Usage](#usage)
  - [Implementing a ViewModel](#implementing-a-viewmodel)
  - [Using with Compose](#using-with-compose)
- [Testing](#testing)
- [Demo](#demo)
- [Contributions and Feedback](#contributions-and-feedback)

## Features

- **State Management**: Easily manage UI state and actions.
- **Kotlin Multiplatform**: Share your ViewModels between Android and iOS (`iosArm64` and `iosSimulatorArm64`).
- **Compose Integration**: Seamlessly integrate with Jetpack Compose and Compose Multiplatform.
- **Coroutine Support**: Built-in support for Kotlin coroutines for asynchronous operations.

## Getting Started

In a Kotlin Multiplatform module, add the dependency to `commonMain`:

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("io.github.lucas-cordeiro.ymir:core:[last-version]")
        }
    }
}
```

In an Android-only module, add it to `dependencies`:

```kotlin
dependencies {
    implementation("io.github.lucas-cordeiro.ymir:core:[last-version]")
}
```

Replace [last-version] with the latest version available on [Maven Central](https://central.sonatype.com/artifact/io.github.lucas-cordeiro.ymir/core).

Requirements:

- Kotlin 2.4.20 or newer.
- Android `minSdk` 23.
- The library is built with the JetBrains lifecycle 2.10.0 and Compose Multiplatform 1.11.1.

## Migrating from 1.x

- The package did not change, so your imports keep working.
- Android `minSdk` went from 21 to 23.
- `ObserveActions` reads `LocalLifecycleOwner` from `androidx.lifecycle.compose`. Activities and fragments with Compose already provide it.

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

Create your ViewModel by extending the custom ViewModel class provided by the library. It works in `commonMain`:

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
    sendAction { HomeUiAction.ShowToast("Hello, Android!") }
}
```

**Note**: Both `setState` and `sendAction` are `suspend` functions.

### Using with Compose

In your Compose UI, on Android or in Compose Multiplatform, you can easily observe state and handle actions using the provided utility functions. Here's how:

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
This library includes support for testing ViewModel implementations by providing a utility class, `ViewModelObserver`, that simplifies the management of `UiState` and `UiAction` during tests. The `test` artifact is available for Android.

To use the testing utilities provided by the library, add the following dependency to the `testImplementation` section of your `build.gradle.kts`:

```kotlin
dependencies {
    testImplementation("io.github.lucas-cordeiro.ymir:test:[last-version]")
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

In common tests, you can collect `state` and `action` with [Turbine](https://github.com/cashapp/turbine), as the tests in `core/src/commonTest` do.

## Demo

For a demonstration of how to implement and use this library, check out the `MainActivity` in the `app` module of this repository.
Additionally, the `app` module includes a unit test example for the ViewModel, demonstrating how to test `UiState` and `UiAction` using the `ViewModelObserver`.

## Contributions and Feedback

As the sole developer of this library, I am open to suggestions, improvements, and feedback to make it even better! If you have any ideas, encounter a bug, or would like to share your thoughts, feel free to open an issue.

Your contributions and feedback are highly appreciated as they help enhance the quality and functionality of this library.
