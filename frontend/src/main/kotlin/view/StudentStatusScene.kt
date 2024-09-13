// 修改 StudentStatusScene.kt
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
import module.StudentStatusModule


@Composable
fun StudentStatusScene(onNavigate: (String) -> Unit, role: String) {
    var firstscene = "查看个人信息"
    if(UserSession.role == "admin") {
        firstscene = "增加学籍"
    }
    var selectedMenuItem by remember { mutableStateOf(firstscene) }
    val studentStatusModule = remember { StudentStatusModule() }
    var searchResults by remember { mutableStateOf(listOf<StudentStatusModule>())}
    var isCollapsed by remember { mutableStateOf(true) }

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
            modifier = Modifier.clickable {
                isCollapsed = false
            },
            tint = ColorPack.backgroundColor1[choose.value].value,
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (role == "student") {
            Icon(imageVector = Icons.Default.Person, contentDescription = "查看个人信息", tint = ColorPack.backgroundColor1[choose.value].value)
            Spacer(modifier = Modifier.height(16.dp))
            Icon(imageVector = Icons.Default.Lock, contentDescription = "修改密码", tint = ColorPack.backgroundColor1[choose.value].value)
        } else if (role == "admin") {
            Icon(imageVector = Icons.Default.Add, contentDescription = "增加学籍", tint = ColorPack.backgroundColor1[choose.value].value)
            Spacer(modifier = Modifier.height(16.dp))
            Icon(imageVector = Icons.Default.Edit, contentDescription = "修改学籍", tint = ColorPack.backgroundColor1[choose.value].value)
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
        //.shadow(4.dp, spotColor = Color.Gray, ambientColor = Color.Gray, clip = false)
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
            modifier = Modifier.clickable {
                isCollapsed = true
            },
            tint = ColorPack.backgroundColor1[choose.value].value,
        )
    }
    Text(
        text = "学籍信息",
        color = ColorPack.backgroundColor1[choose.value].value,
        modifier = Modifier.padding(16.dp)
    )
    Divider(color = Color.Gray, thickness = 1.dp)
    if (role == "student") {
        TextButton(onClick = {
            selectedMenuItem = "查看个人信息"
            studentStatusModule.searchStudentStatus()
        }) {
            Icon(imageVector = Icons.Default.Person, contentDescription = "查看个人信息", tint = ColorPack.backgroundColor1[choose.value].value)
            Spacer(modifier = Modifier.width(8.dp))
            Text("查看个人信息", fontSize = 16.sp, color = ColorPack.backgroundColor1[choose.value].value)
        }
        TextButton(onClick = {
            selectedMenuItem = "修改密码"
        }) {
            Icon(imageVector = Icons.Default.Lock, contentDescription = "修改密码", tint = ColorPack.backgroundColor1[choose.value].value)
            Spacer(modifier = Modifier.width(8.dp))
            Text("修改密码", fontSize = 16.sp, color = ColorPack.backgroundColor1[choose.value].value)
        }
    } else if (role == "admin") {
        TextButton(onClick = { selectedMenuItem = "增加学籍" }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "增加学籍", tint = ColorPack.backgroundColor1[choose.value].value)
            Spacer(modifier = Modifier.width(8.dp))
            Text("增加学籍", fontSize = 16.sp, color = ColorPack.backgroundColor1[choose.value].value)
        }
        TextButton(onClick = { selectedMenuItem = "修改学籍"
            studentStatusModule.onclickModifyStudentStatus { results ->
                searchResults = results
            }
        }) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = "修改学籍", tint = ColorPack.backgroundColor1[choose.value].value)
            Spacer(modifier = Modifier.width(8.dp))
            Text("修改学籍", fontSize = 16.sp, color = ColorPack.backgroundColor1[choose.value].value)
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