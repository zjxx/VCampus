package view.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import module.LoginModule

// src/main/kotlin/view/component/ForgotPasswordDialog.kt

// src/main/kotlin/view/component/ForgotPasswordDialog.kt

@Composable
fun ForgotPasswordDialog(onDismiss: () -> Unit, onLoginSuccess: () -> Unit, onLogout: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var verificationCode by remember { mutableStateOf("") }
    var showModifyPasswordDialog by remember { mutableStateOf(false) }
    val loginModule = LoginModule(onLoginSuccess,onLogout)

    if (showModifyPasswordDialog) {
        ModifyPasswordDialog(
            onDismiss = { showModifyPasswordDialog = false },
            onSuccess = {
                showModifyPasswordDialog = false
                onDismiss() // Dismiss the ForgotPasswordDialog
            },
            userId = cardNumber,
            onLoginSuccess={},
            onLogout={}
        )
    } else {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Column {
                    Text("找回密码", fontSize = 24.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            },
            text = {
                Column {
                    Text("请输入您的邮箱、一卡通号，我们将发送验证码到您的邮箱。", fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("邮箱") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = cardNumber,
                        onValueChange = { cardNumber = it },
                        label = { Text("一卡通号") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = verificationCode,
                            onValueChange = { verificationCode = it },
                            label = { Text("验证码") },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { if (email.contains("@")) {
                                loginModule.sendVerificationCode(email, cardNumber)
                            } else {
                                DialogManager.showDialog("邮箱输入错误，请重新输入")
                            } },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFF006400),
                                contentColor = Color.White
                            )
                        ) {
                            Text("发送验证码")
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (loginModule.verifyCode(verificationCode)) {
                            showModifyPasswordDialog = true
                        } else {
                            DialogManager.showDialog("验证码错误，请重新输入")
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
}