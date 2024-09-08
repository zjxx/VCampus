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
import java.util.*

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

    val greeting = getGreeting()
    val userId = UserSession.userId ?: "Unknown ID"
    val userName = UserSession.userName ?: "Unknown User"
    val role = UserSession.role ?: "Unknown Role"
    val roleTitle = when (role) {
        "student" -> "同学"
        "teacher" -> "老师"
        "admin" -> "管理员"
        else -> "用户"
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("$greeting,  $userName$roleTitle ")
        Text("一卡通号: $userId")
        // 添加更多内容
    }

}

fun getGreeting(): String {
    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when (currentHour) {
        in 0..5 -> "晚上好"
        in 6..11 -> "早上好"
        in 12..13 -> "中午好"
        in 14..17 -> "下午好"
        else -> "晚上好"
    }
}