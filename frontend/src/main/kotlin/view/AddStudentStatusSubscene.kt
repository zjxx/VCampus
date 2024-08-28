package view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import view.component.pageTitle
import module.StudentStatusModule

@Composable
fun AddStudentStatusSubscene() {
    val studentStatusModule = remember { StudentStatusModule() }
    var name by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var race by remember { mutableStateOf("") }
    var nativePlace by remember { mutableStateOf("") }
    var studentId by remember { mutableStateOf("") }
    var major by remember { mutableStateOf("") }
    var academy by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(start = 16.dp)) {
        pageTitle(heading = "增加学籍信息", caption = "填写学籍信息")

        // 第一行
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("姓名") },
                modifier = Modifier.weight(1f).padding(end = 16.dp)
            )
            OutlinedTextField(
                value = gender,
                onValueChange = { gender = it },
                label = { Text("性别") },
                modifier = Modifier.weight(1f).padding(end = 16.dp)
            )
            OutlinedTextField(
                value = race,
                onValueChange = { race = it },
                label = { Text("名族") },
                modifier = Modifier.weight(1f)
            )
        }

        // 第二行
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            OutlinedTextField(
                value = nativePlace,
                onValueChange = { nativePlace = it },
                label = { Text("籍贯") },
                modifier = Modifier.weight(1f).padding(end = 16.dp)
            )
            OutlinedTextField(
                value = studentId,
                onValueChange = { studentId = it },
                label = { Text("一卡通") },
                modifier = Modifier.weight(1f)
            )
        }

        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            OutlinedTextField(
                value = major,
                onValueChange = { major = it },
                label = { Text("专业") },
                modifier = Modifier.weight(1f).padding(end = 16.dp)
            )
            OutlinedTextField(
                value = academy,
                onValueChange = { academy = it },
                label = { Text("学院") },
                modifier = Modifier.weight(1f)
            )
        }

        // 确认添加按钮
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomEnd) {
            Button(onClick = {
                studentStatusModule.name = name
                studentStatusModule.gender = gender
                studentStatusModule.race = race
                studentStatusModule.nativePlace = nativePlace
                studentStatusModule.studentId = studentId
                studentStatusModule.major = major
                studentStatusModule.academy = academy
                studentStatusModule.addStudentStatus()
            }) {
                Text("确认添加")
            }
        }
    }
}