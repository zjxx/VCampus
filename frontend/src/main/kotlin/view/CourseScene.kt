// src/main/kotlin/view/CourseScene.kt
package view

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.ColorPack
import data.ColorPack.choose
import data.UserSession
import module.CourseModule

@Composable
fun CourseScene(onNavigate: (String) -> Unit, role: String) {
    var first = "选课"
    if(UserSession.role == "admin") {
        first = "增加课程"
    } else if(UserSession.role == "teacher") {
        first = "查看课程"
    }
    var selectedMenuItem by remember { mutableStateOf(first) }
    val courseModule = remember { CourseModule() }
    var isCollapsed by remember { mutableStateOf(false) }
    var classes by remember { mutableStateOf(emptyList<module.Class>()) }
    var videoclasses by remember { mutableStateOf(emptyList<module.videoClass>()) }


    Row(modifier = Modifier.fillMaxSize()) {
        if (isCollapsed) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.03f)
                    .background(ColorPack.sideColor2[choose.value].value)
                    .drawBehind {
                        drawLine(
                            color = ColorPack.mainColor2[choose.value].value,
                            start = Offset(size.width, 0f),
                            end = Offset(size.width, size.height),
                            strokeWidth = 4.dp.toPx()
                        )
                    },
                    //.shadow(4.dp, spotColor = Color.Gray, ambientColor = Color.Gray, clip = false),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Expand",
                        modifier = Modifier.clickable { isCollapsed = false },
                        tint = ColorPack.backgroundColor1[choose.value].value,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    if (role == "student") {
                        Icon(imageVector = Icons.Default.Person, contentDescription = "选课", tint = ColorPack.backgroundColor1[choose.value].value)
                        Spacer(modifier = Modifier.height(16.dp))
                        Icon(imageVector = Icons.Default.Schedule, contentDescription = "查看我的课表", tint = ColorPack.backgroundColor1[choose.value].value)
                        Spacer(modifier = Modifier.height(16.dp))
                        Icon(imageVector = Icons.Default.PlaylistAddCheck, contentDescription = "查看我的成绩", tint = ColorPack.backgroundColor1[choose.value].value)
                        Spacer(modifier = Modifier.height(16.dp))
                        Icon(imageVector = Icons.Default.Videocam, contentDescription = "云课堂", tint = ColorPack.backgroundColor1[choose.value].value)
                    } else if (role == "admin") {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "增加课程", tint = ColorPack.backgroundColor1[choose.value].value)
                        Spacer(modifier = Modifier.height(16.dp))
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "修改课程", tint = ColorPack.backgroundColor1[choose.value].value)
                        Spacer(modifier = Modifier.height(16.dp))
                        Icon(imageVector = Icons.Default.Calculate, contentDescription = "成绩管理", tint = ColorPack.backgroundColor1[choose.value].value)
                    } else if (role == "teacher") {
                        Icon(imageVector = Icons.Default.ViewList, contentDescription = "查看课程", tint = ColorPack.backgroundColor1[choose.value].value)
                        Spacer(modifier = Modifier.height(16.dp))
                        Icon(imageVector = Icons.Default.Radio, contentDescription = "录课", tint = ColorPack.backgroundColor1[choose.value].value)
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.2f)
                    .background(ColorPack.sideColor2[choose.value].value)
                    .drawBehind {
                        drawLine(
                            color = ColorPack.mainColor2[choose.value].value,
                            start = Offset(0f, 0f),
                            end = Offset(0f, size.height),
                            strokeWidth = 8.dp.toPx()
                        )
                    }
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Collapse",
                        modifier = Modifier.clickable { isCollapsed = true },
                        tint = ColorPack.backgroundColor1[choose.value].value,
                    )
                }
                Text(
                    text = "课程管理",
                    color = ColorPack.backgroundColor1[choose.value].value,
                    modifier = Modifier.padding(16.dp)
                )
                Divider(color = Color.Gray, thickness = 1.dp)
                if (role == "student") {
                    TextButton(onClick = {
                        selectedMenuItem = "选课"
                        courseModule.listCourse()
                    }) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = "选课", tint = ColorPack.backgroundColor1[choose.value].value)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("选课", fontSize = 16.sp, color = ColorPack.backgroundColor1[choose.value].value)
                    }
                    TextButton(onClick = {
                        selectedMenuItem = "查看我的课表"
                        courseModule.classTable()
                    }) {
                        Icon(imageVector = Icons.Default.Schedule, contentDescription = "查看我的课表", tint = ColorPack.backgroundColor1[choose.value].value)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("查看我的课表", fontSize = 16.sp, color = ColorPack.backgroundColor1[choose.value].value)
                    }
                    TextButton(onClick = {
                        selectedMenuItem = "查看我的成绩"
                        courseModule.viewScore()
                    }) {
                        Icon(imageVector = Icons.Default.PlaylistAddCheck, contentDescription = "查看我的课表", tint = ColorPack.backgroundColor1[choose.value].value)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("查看我的成绩", fontSize = 16.sp, color = ColorPack.backgroundColor1[choose.value].value)
                    }
                    TextButton(onClick = {
                        selectedMenuItem = "云课堂"
                        courseModule.studentViewRecording { receivedClasses ->
                            videoclasses = receivedClasses
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Videocam, contentDescription = "云课堂", tint = ColorPack.backgroundColor1[choose.value].value)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("云课堂", fontSize = 16.sp, color = ColorPack.backgroundColor1[choose.value].value)
                    }
                }
                if (role == "admin") {
                    TextButton(onClick = {
                        selectedMenuItem = "增加课程"
                    }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "增加课程", tint = ColorPack.backgroundColor1[choose.value].value)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("增加课程", fontSize = 16.sp, color = ColorPack.backgroundColor1[choose.value].value)
                    }
                    TextButton(onClick = {
                        selectedMenuItem = "修改课程"
                        courseModule.ShowAllCourse()
                    }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "修改课程", tint = ColorPack.backgroundColor1[choose.value].value)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("修改课程", fontSize = 16.sp, color = ColorPack.backgroundColor1[choose.value].value)
                    }
                    TextButton(onClick = {
                        selectedMenuItem = "成绩管理"
                        courseModule.ConfirmGrade { receivedClasses ->
                            classes = receivedClasses
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Calculate, contentDescription = "成绩管理", tint = ColorPack.backgroundColor1[choose.value].value)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("成绩管理", fontSize = 16.sp, color = ColorPack.backgroundColor1[choose.value].value)
                    }
                }
                if (role == "teacher") {
                    TextButton(onClick = {
                        selectedMenuItem = "查看课程"
                        courseModule.viewMyclass { receivedClasses ->
                            classes = receivedClasses
                        }
                    }) {
                        Icon(imageVector = Icons.Default.ViewList, contentDescription = "查看课程", tint = ColorPack.backgroundColor1[choose.value].value)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("查看课程", fontSize = 16.sp, color = ColorPack.backgroundColor1[choose.value].value)
                    }
                    TextButton(onClick = {
                        selectedMenuItem = "录课"
                        courseModule.recordMyclass { receivedClasses ->
                            videoclasses = receivedClasses
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Radio, contentDescription = "查看课程", tint = ColorPack.backgroundColor1[choose.value].value)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("录课", fontSize = 16.sp, color = ColorPack.backgroundColor1[choose.value].value)
                    }
                }
            }
        }

        Box(modifier = Modifier.weight(0.7f).fillMaxHeight().padding(16.dp)) {
            Crossfade(targetState = selectedMenuItem) { menuItem ->
                when (menuItem) {
                    "选课" -> SelectCourseSubscene(courseModule)
                    "查看我的课表" -> ViewMyCoursesSubscene(courseModule)
                    "查看我的成绩" -> MygradeSubscene(courseModule)
                    "增加课程" -> AddCourseSubscene()
                    "修改课程" -> ModifyCourseSubscene(courseModule)
                    "成绩管理" -> ConfirmGrade(classes)
                    "查看课程" -> ViewTeacherCourseSubscene(classes)
                    "录课" -> RecordTeacherCourseSubscene(videoclasses)
                    "云课堂" -> StudentCourseSubscene(videoclasses)
                    "打分" -> RecordSubscene()
                    else -> Text("请选择一个菜单项")
                }
            }
        }
    }
}