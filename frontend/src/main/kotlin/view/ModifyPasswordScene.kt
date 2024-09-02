// ModifyPasswordScene.kt
package view

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import module.StudentStatusModule
import view.component.GlobalDialog
import view.component.pageTitle

@Composable
fun ModifyPasswordScene() {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    val studentStatusModule = remember { StudentStatusModule() }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally // 水平居中
    ) {
        // 标题不居中
        Box(modifier = Modifier.fillMaxWidth()) {
            pageTitle(heading = "修改密码", caption = "修改你的密码")
        }

        Spacer(modifier = Modifier.height(24.dp)) // 增加标题和输入框之间的间距

        OutlinedTextField(
            value = oldPassword,
            onValueChange = { oldPassword = it },
            label = { Text("旧密码") },
            modifier = Modifier.fillMaxWidth(0.8f).padding(bottom = 16.dp) // 减少文本框的宽度
        )

        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("新密码") },
            modifier = Modifier.fillMaxWidth(0.8f).padding(bottom = 16.dp) // 减少文本框的宽度
        )

        Button(onClick = {
            studentStatusModule.modifyPassword(oldPassword, newPassword)
        }, modifier = Modifier.align(Alignment.End)) {
            Text("确认修改")
        }
    }

    GlobalDialog() // 添加全局对话框
}