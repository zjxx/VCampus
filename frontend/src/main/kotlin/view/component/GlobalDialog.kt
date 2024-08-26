// src/main/kotlin/view/component/GlobalDialog.kt
package view.component

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun GlobalDialog() {
    val message = DialogManager.dialogMessage.value

    if (message != null) {
        AlertDialog(
            onDismissRequest = { DialogManager.dismissDialog() },
            title = { Text("提示") },
            text = { Text(message) },
            confirmButton = {
                TextButton(onClick = { DialogManager.dismissDialog() }) {
                    Text("确定")
                }
            }
        )
    }
}