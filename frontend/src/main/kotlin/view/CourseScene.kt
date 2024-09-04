// src/main/kotlin/view/CourseScene.kt
package view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.animation.Crossfade
import androidx.compose.ui.draw.shadow
import module.CourseModule


@Composable
fun CourseScene(onNavigate: (String) -> Unit, role: String) {
    var selectedMenuItem by remember { mutableStateOf("") }
    val courseModule = remember { CourseModule() }

    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.2f) // 设置宽度占父容器的20%
                .background(Color.LightGray) // 设置背景颜色为浅灰色
                .shadow(4.dp, spotColor = Color.Gray, ambientColor = Color.Gray, clip = false) // 添加左右阴影效果
        ) {
            Text(
                text = "课程管理",
                color = Color.Black,
                modifier = Modifier.padding(16.dp)
            )
            Divider(color = Color.Gray, thickness = 1.dp) // 添加分隔栏
            if (role == "student") {
                TextButton(onClick = {
                    selectedMenuItem = "选课"
                    courseModule.listCourse()
                }) {
                    Text("选课", color = Color.Black) // 设置字体颜色为黑色
                }
                TextButton(onClick = {
                    selectedMenuItem = "查看我的课表"
                }) {
                    Text("查看我的课表", color = Color.Black) // 设置字体颜色为黑色
                }
            }
        }
        Box(modifier = Modifier.weight(0.7f).fillMaxHeight().padding(16.dp)) {
            Crossfade(targetState = selectedMenuItem) { menuItem ->
                when (menuItem) {
                    "选课" -> SelectCourseSubscene()
                    "查看我的课表" -> ViewMyCoursesSubscene()
                    else -> Text("请选择一个菜单项")
                }
            }
        }
    }
}

