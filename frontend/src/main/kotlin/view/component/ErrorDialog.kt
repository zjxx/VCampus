// 新建 ErrorDialog.kt
package view.component

import androidx.compose.material.*
import androidx.compose.runtime.Composable

@Composable
fun ErrorDialog(showDialog: Boolean, dialogMessage: String, onDismiss: () -> Unit) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("登录失败") },
            text = { Text(dialogMessage) },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("确定")
                }
            }
        )
    }
}