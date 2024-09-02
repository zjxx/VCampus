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
import module.StudentStatusModule

@Composable
fun StudentStatusScene(onNavigate: (String) -> Unit, role: String) {
    var selectedMenuItem by remember { mutableStateOf("") }
    val studentStatusModule = remember { StudentStatusModule() }
    var searchResults by remember { mutableStateOf(listOf<StudentStatusModule>())}

    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.2f) // 设置宽度占父容器的20%
                .background(Color.LightGray) // 设置背景颜色为浅灰色
                .shadow(4.dp, spotColor = Color.Gray, ambientColor = Color.Gray, clip = false) // 添加左右阴影效果
        ) {
            Text(
                text = "学籍信息",
                color = Color.Black,
                modifier = Modifier.padding(16.dp)
            )
            Divider(color = Color.Gray, thickness = 1.dp) // 添加分隔栏
            if (role == "student") {
                TextButton(onClick = {
                    selectedMenuItem = "查看个人信息"
                    studentStatusModule.searchStudentStatus()
                }) {
                    Text("查看个人信息", color = Color.Black) // 设置字体颜色为黑色
                }
                TextButton(onClick = {
                    selectedMenuItem = "修改密码"
                }) {
                    Text("修改密码", color = Color.Black) // 设置字体颜色为黑色
                }
            } else if (role == "admin") {
                TextButton(onClick = { selectedMenuItem = "增加学籍" }) {
                    Text("增加学籍", color = Color.Black) // 设置字体颜色为黑色
                }
                TextButton(onClick = { selectedMenuItem = "修改学籍"
                studentStatusModule.onclickModifyStudentStatus{ results ->
                    searchResults = results
                }})
                {
                    Text("修改学籍", color = Color.Black) // 设置字体颜色为黑色
                }
            }
        }
        Box(modifier = Modifier.weight(0.7f).fillMaxHeight().padding(16.dp)) {
            Crossfade(targetState = selectedMenuItem) { menuItem ->
                when (menuItem) {
                    "查看个人信息" -> ViewStudentStatusSubscene(studentStatusModule)
                    "增加学籍" -> AddStudentStatusSubscene()
                    "修改学籍" -> ModifyStudentStatusSubscene()
                    "修改密码" -> ModifyPasswordScene()
                    else -> Text("请选择一个菜单项")
                }
            }
        }
    }
}