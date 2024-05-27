package io.github.lucascordeiro.ymir

import android.widget.Toast
import androidx.activity.ComponentActivity

internal fun ComponentActivity.handleAction(action: MainUiAction) {
    when (action) {
        is MainUiAction.ShowToast -> showToast(action.message)
    }
}

private fun ComponentActivity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}