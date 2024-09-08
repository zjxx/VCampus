package view.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import module.LoginModule

// src/main/kotlin/view/component/ModifyPasswordDialog.kt

@Composable
fun ModifyPasswordDialog(onDismiss: () -> Unit, onSuccess: () -> Unit, userId: String) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val loginModule = LoginModule {}

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("修改密码", fontSize = 24.sp)
        },
        text = {
            Column {
                Text("")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("新密码") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("确认新密码") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (newPassword == confirmPassword) {
                        loginModule.updatePassword(userId, newPassword, {
                            DialogManager.showDialog("密码修改成功")
                            onSuccess()
                        }, { errorMessage ->
                            DialogManager.showDialog(errorMessage)
                        })
                    } else {
                        DialogManager.showDialog("两次输入的密码不一致，请重新输入")
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF006400),
                    contentColor = Color.White
                )
            ) {
                Text("确认")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF006400),
                    contentColor = Color.White
                )
            ) {
                Text("取消")
            }
        }
    )
}