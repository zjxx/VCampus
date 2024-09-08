// src/main/kotlin/view/HomeScene.kt
package view

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.UserSession
import module.LoginModule
import view.component.EmailDialog
import view.component.DialogManager

@Composable
fun HomeScene() {
    var showEmailDialog by remember { mutableStateOf(UserSession.status == "noemail") }
    val loginModule = LoginModule(onLoginSuccess = {})

    if (showEmailDialog) {
        EmailDialog(
            onDismiss = { showEmailDialog = false },
            onConfirm = { email ->
                loginModule.updateEmail(UserSession.userId ?: "", email, {
                    UserSession.status = "success"
                    showEmailDialog = false
                }, { errorMessage ->
                    DialogManager.showDialog(errorMessage)
                })
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Welcome to the Home Scene")
        // 添加更多内容
    }

}