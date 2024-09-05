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
import module.CourseData

@Composable
fun CourseScene(onNavigate: (String) -> Unit, role: String) {
    var selectedMenuItem by remember { mutableStateOf("") }
    val courseModule = remember { CourseModule() }
    var selectedCourse by remember { mutableStateOf<CourseData?>(null) }

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
            if(role=="admin")
            {
                TextButton(onClick = {
                    selectedMenuItem = "增加课程"
                }) {
                    Text("增加课程", color = Color.Black) // 设置字体颜色为黑色
                }
                TextButton(onClick = {
                    selectedMenuItem = "修改课程"
                    selectedCourse = CourseData(
                        courseName = "示例课程",
                        courseId = "12345",
                        credit = "3",
                        capacity = "50",
                        grade = "大二",
                        major = "计算机科学",
                        semester = "第一学期",
                        property = "选修",
                        time = "",
                        location = "",
                        timeAndLocationCards = listOf(),
                        teacher = "张老师",
                        teacherId = "67890"
                    )
                }) {
                    Text("修改课程", color = Color.Black) // 设置字体颜色为黑色
                }

            }
            if(role=="teacher")
            {
                TextButton(onClick = {
                    selectedMenuItem = "查看课程"
                    courseModule.viewMyclass()
                }) {
                    Text("查看课程", color = Color.Black) // 设置字体颜色为黑色
                }
            }
        }
        Box(modifier = Modifier.weight(0.7f).fillMaxHeight().padding(16.dp)) {
            Crossfade(targetState = selectedMenuItem) { menuItem ->
                when (menuItem) {
                    "选课" -> SelectCourseSubscene(courseModule)
                    "查看我的课表" -> ViewMyCoursesSubscene()
                    "增加课程" -> AddCourseSubscene()
                    "修改课程" -> selectedCourse?.let { course ->
                        ModifyCourseSubscene(
                            course = course,
                            onSelectCourse = { selectedCourse, onSuccess -> /* Handle course selection */ },
                            onUnselectCourse = { selectedCourse, onSuccess -> /* Handle course unselection */ }
                        )
                    }
                    "查看课程" -> ViewTeacherCourseSubscene()
                    else -> Text("请选择一个菜单项")
                }
            }
        }
    }
}