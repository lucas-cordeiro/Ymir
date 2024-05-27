package io.github.lucascordeiro.ymir

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.lucascordeiro.ymir.ui.theme.YmirTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YmirTheme {
                MainScreen()
            }
        }
    }

    @Composable
    private fun MainScreen(
        viewModel: MainViewModel = viewModel()
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            val state by viewModel.state.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                viewModel.action.collect(::handleAction)
            }

            Greeting(
                name = "Android",
                isLoading = state.isLoading,
                onClickButton = viewModel::clickedLoadButton,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }

    @Composable
    fun Greeting(
        name: String,
        isLoading: Boolean,
        onClickButton: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Text(
                text = "Hello $name!"
            )

            AnimatedVisibility(visible = isLoading) {
                CircularProgressIndicator()
            }

            Button(onClick = onClickButton) {
                Text(text = "Click me!")
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        YmirTheme {
            Greeting(
                name = "Android",
                isLoading = false,
                onClickButton = {}
            )
        }
    }
}