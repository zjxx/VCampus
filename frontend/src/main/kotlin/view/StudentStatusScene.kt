// 修改 StudentStatusScene.kt
package view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.shadow
import androidx.compose.animation.Crossfade

@Composable
fun StudentStatusScene(onNavigate: (String) -> Unit, role: String) {
    var selectedMenuItem by remember { mutableStateOf("") }

    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.3f) // 设置宽度占父容器的30%
                .background(Color.LightGray) // 设置背景颜色为浅灰色
                .shadow(4.dp) // 添加阴影效果
        ) {
            if (role == "student") {
                TextButton(onClick = { selectedMenuItem = "查看学籍" }) {
                    Text("查看学籍", color = Color.Black) // 设置字体颜色为黑色
                }
            } else if (role == "admin") {
                TextButton(onClick = { selectedMenuItem = "增加学籍" }) {
                    Text("增加学籍", color = Color.Black) // 设置字体颜色为黑色
                }
                TextButton(onClick = { selectedMenuItem = "删除学籍" }) {
                    Text("删除学籍", color = Color.Black) // 设置字体颜色为黑色
                }
                TextButton(onClick = { selectedMenuItem = "修改学籍" }) {
                    Text("修改学籍", color = Color.Black) // 设置字体颜色为黑色
                }
                TextButton(onClick = { selectedMenuItem = "查找学籍" }) {
                    Text("查找学籍", color = Color.Black) // 设置字体颜色为黑色
                }
            }
        }
        Box(modifier = Modifier.weight(0.7f).fillMaxHeight().padding(16.dp)) {
            Crossfade(targetState = selectedMenuItem) { menuItem ->
                when (menuItem) {
                    "查看学籍" -> ViewStudentStatusSubscene()
                    "增加学籍" -> AddStudentStatusSubscene()
                    "删除学籍" -> DeleteStudentStatusSubscene()
                    "修改学籍" -> ModifyStudentStatusSubscene()
                    "查找学籍" -> SearchStudentStatusSubscene()
                    else -> Text("请选择一个菜单项")
                }
            }
        }
    }
}

@Composable
fun ViewStudentStatusSubscene() {
    Text("查看学籍子场景")
}

@Composable
fun AddStudentStatusSubscene() {
    Text("增加学籍子场景")
}

@Composable
fun DeleteStudentStatusSubscene() {
    Text("删除学籍子场景")
}

@Composable
fun ModifyStudentStatusSubscene() {
    Text("修改学籍子场景")
}