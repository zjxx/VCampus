// src/main/kotlin/view/component/DialogManager.kt
package view.component

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.Composable

object DialogManager {
    var dialogMessage = mutableStateOf<String?>(null)

    fun showDialog(message: String) {
        dialogMessage.value = message
    }

    fun dismissDialog() {
        dialogMessage.value = null
    }
}