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
import androidx.compose.foundation.clickable
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*


@Composable
fun StudentStatusScene(onNavigate: (String) -> Unit, role: String) {
    var selectedMenuItem by remember { mutableStateOf("") }
    val studentStatusModule = remember { StudentStatusModule() }
    var searchResults by remember { mutableStateOf(listOf<StudentStatusModule>())}
    var isCollapsed by remember { mutableStateOf(true) }

    Row(modifier = Modifier.fillMaxSize()) {
        if (isCollapsed) {
Box(
    modifier = Modifier
        .fillMaxHeight()
        .weight(0.03f)
        .background(Color.LightGray)
        .shadow(4.dp, spotColor = Color.Gray, ambientColor = Color.Gray, clip = false),
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
            modifier = Modifier.clickable { isCollapsed = false }
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (role == "student") {
            Icon(imageVector = Icons.Default.Person, contentDescription = "查看个人信息")
            Spacer(modifier = Modifier.height(16.dp))
            Icon(imageVector = Icons.Default.Lock, contentDescription = "修改密码")
        } else if (role == "admin") {
            Icon(imageVector = Icons.Default.Add, contentDescription = "增加学籍")
            Spacer(modifier = Modifier.height(16.dp))
            Icon(imageVector = Icons.Default.Edit, contentDescription = "修改学籍")
        }
    }
}
        } else {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.2f)
                    .background(Color.LightGray)
                    .shadow(4.dp, spotColor = Color.Gray, ambientColor = Color.Gray, clip = false)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Collapse",
                        modifier = Modifier.clickable { isCollapsed = true }
                    )
                }
                Text(
                    text = "学籍信息",
                    color = Color.Black,
                    modifier = Modifier.padding(16.dp)
                )
                Divider(color = Color.Gray, thickness = 1.dp)
                if (role == "student") {
                    TextButton(onClick = {
                        selectedMenuItem = "查看个人信息"
                        studentStatusModule.searchStudentStatus()
                    }) {
                        Text("查看个人信息", color = Color.Black)
                    }
                    TextButton(onClick = {
                        selectedMenuItem = "修��密码"
                    }) {
                        Text("修改密码", color = Color.Black)
                    }
                } else if (role == "admin") {
                    TextButton(onClick = { selectedMenuItem = "增加学籍" }) {
                        Text("增加学籍", color = Color.Black)
                    }
                    TextButton(onClick = { selectedMenuItem = "修改学籍"
                    studentStatusModule.onclickModifyStudentStatus{ results ->
                        searchResults = results
                    }})
                    {
                        Text("修改学籍", color = Color.Black)
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .weight(0.7f)
                .fillMaxHeight()
                .padding(16.dp)
        ) {
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