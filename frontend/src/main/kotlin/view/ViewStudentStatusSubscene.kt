package view

import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import view.component.pageTitle

@Composable
fun ViewStudentStatusSubscene() {
    Column(modifier = Modifier.padding(start = 16.dp)) { // 添加左边距
        pageTitle(heading = "个人学籍信息", caption = "查看个人学籍信息")

        // 第一行
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            OutlinedTextField(
                value = "姓名",
                onValueChange = {},
                label = { Text("姓名") },
                readOnly = true,
                modifier = Modifier.weight(1f).padding(end = 16.dp)
            )
            OutlinedTextField(
                value = "性别",
                onValueChange = {},
                label = { Text("性别") },
                readOnly = true,
                modifier = Modifier.weight(1f).padding(end = 16.dp)
            )
            OutlinedTextField(
                value = "民族",
                onValueChange = {},
                label = { Text("民族") },
                readOnly = true,
                modifier = Modifier.weight(1f)
            )
        }

        // 第二行
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            OutlinedTextField(
                value = "籍贯",
                onValueChange = {},
                label = { Text("籍贯") },
                readOnly = true,
                modifier = Modifier.weight(1f).padding(end = 16.dp)
            )
            OutlinedTextField(
                value = "学号",
                onValueChange = {},
                label = { Text("学号") },
                readOnly = true,
                modifier = Modifier.weight(1f)
            )
        }

        // 第三行
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            OutlinedTextField(
                value = "专业",
                onValueChange = {},
                label = { Text("专业") },
                readOnly = true,
                modifier = Modifier.weight(1f).padding(end = 16.dp)
            )
            OutlinedTextField(
                value = "学院",
                onValueChange = {},
                label = { Text("学院") },
                readOnly = true,
                modifier = Modifier.weight(1f)
            )
        }
    }
}