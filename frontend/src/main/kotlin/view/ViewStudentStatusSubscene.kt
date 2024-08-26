package view

import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import view.component.pageTitle

@Composable
fun ViewStudentStatusSubscene() {
    val (name, setName) = remember { mutableStateOf("") }
    val (gender, setGender) = remember { mutableStateOf("") }
    val (ethnicity, setEthnicity) = remember { mutableStateOf("") }
    val (origin, setOrigin) = remember { mutableStateOf("") }
    val (studentId, setStudentId) = remember { mutableStateOf("") }
    val (major, setMajor) = remember { mutableStateOf("") }
    val (college, setCollege) = remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(start = 16.dp)) { // 添加左边距
        pageTitle(heading = "个人学籍信息", caption = "查看个人学籍信息")

        // 第一行
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            OutlinedTextField(
                value = name,
                onValueChange = setName,
                label = { Text("姓名") },
                readOnly = true,
                modifier = Modifier.weight(1f).padding(end = 16.dp)
            )
            OutlinedTextField(
                value = gender,
                onValueChange = setGender,
                label = { Text("性别") },
                readOnly = true,
                modifier = Modifier.weight(1f).padding(end = 16.dp)
            )
            OutlinedTextField(
                value = ethnicity,
                onValueChange = setEthnicity,
                label = { Text("名族") },
                readOnly = true,
                modifier = Modifier.weight(1f)
            )
        }

        // 第二行
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            OutlinedTextField(
                value = origin,
                onValueChange = setOrigin,
                label = { Text("籍贯") },
                readOnly = true,
                modifier = Modifier.weight(1f).padding(end = 16.dp)
            )
            OutlinedTextField(
                value = studentId,
                onValueChange = setStudentId,
                label = { Text("一卡通") },
                readOnly = true,
                modifier = Modifier.weight(1f)
            )
        }
        
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            OutlinedTextField(
                value = major,
                onValueChange = setMajor,
                label = { Text("专业") },
                readOnly = true,
                modifier = Modifier.weight(1f).padding(end = 16.dp)
            )
            OutlinedTextField(
                value = college,
                onValueChange = setCollege,
                label = { Text("学院") },
                readOnly = true,
                modifier = Modifier.weight(1f)
            )
        }
    }
}