// src/main/kotlin/view/CourseScene.kt
package view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
                .weight(0.2f)
                .background(Color.LightGray)
                .shadow(4.dp, spotColor = Color.Gray, ambientColor = Color.Gray, clip = false)
        ) {
            Text(
                text = "课程管理",
                color = Color.Black,
                modifier = Modifier.padding(16.dp)
            )
            Divider(color = Color.Gray, thickness = 1.dp)
            if (role == "student") {
                TextButton(onClick = {
                    selectedMenuItem = "选课"
                    courseModule.listCourse()
                }) {
                    Text("选课", color = Color.Black)
                }
                TextButton(onClick = {
                    selectedMenuItem = "查看我的课表"
                    courseModule.classTable()
                }) {
                    Text("查看我的课表", color = Color.Black)
                }
            }
            if (role == "admin") {
                TextButton(onClick = {
                    selectedMenuItem = "增加课程"
                }) {
                    Text("增加课程", color = Color.Black)
                }
                TextButton(onClick = {
                    selectedMenuItem = "修改课程"
                    courseModule.ShowAllCourse()
                }) {
                    Text("修改课程", color = Color.Black)
                }
            }
            if (role == "teacher") {
                TextButton(onClick = {
                    selectedMenuItem = "查看课程"
                    courseModule.viewMyclass()
                }) {
                    Text("查看课程", color = Color.Black)
                }
            }
        }
        Box(modifier = Modifier.weight(0.7f).fillMaxHeight().padding(16.dp)) {
            Crossfade(targetState = selectedMenuItem) { menuItem ->
                when (menuItem) {
                    "选课" -> SelectCourseSubscene(courseModule)
                    "查看我的课表" -> ViewMyCoursesSubscene(courseModule)
                    "增加课程" -> AddCourseSubscene()
                    "修改课程" -> ModifyCourseSubscene(courseModule)
                    "查看课程" -> ViewTeacherCourseSubscene()
                    else -> Text("请选择一个菜单项")
                }
            }
        }
    }
}