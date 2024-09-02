// ModifyPasswordScene.kt
package view

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import module.StudentStatusModule

@Composable
fun ModifyPasswordScene() {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    val studentStatusModule = remember { StudentStatusModule() }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "修改密码", style = MaterialTheme.typography.h5, modifier = Modifier.padding(bottom = 16.dp))

        OutlinedTextField(
            value = oldPassword,
            onValueChange = { oldPassword = it },
            label = { Text("旧密码") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("新密码") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        Button(onClick = {
            studentStatusModule.modifyPassword(oldPassword, newPassword)
        }, modifier = Modifier.align(Alignment.End)) {
            Text("确认修改")
        }
    }
}