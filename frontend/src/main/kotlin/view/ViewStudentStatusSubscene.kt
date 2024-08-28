package view

import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import view.component.pageTitle
import module.StudentStatusModule

@Composable
fun ViewStudentStatusSubscene(studentStatusModule: StudentStatusModule) {
    val name by studentStatusModule::name
    val gender by studentStatusModule::gender
    val ethnicity by studentStatusModule::race
    val origin by studentStatusModule::nativePlace
    val studentId by studentStatusModule::studentId
    val major by studentStatusModule::major
    val college by studentStatusModule::academy

    Column(modifier = Modifier.padding(start = 16.dp)) {
        pageTitle(heading = "个人学籍信息", caption = "查看个人学籍信息")

        // 第一行
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            OutlinedTextField(
                value = name,
                onValueChange = { studentStatusModule.name = it },
                label = { Text("姓名") },
                readOnly = true,
                modifier = Modifier.weight(1f).padding(end = 16.dp)
            )
            OutlinedTextField(
                value = gender,
                onValueChange = { studentStatusModule.gender = it },
                label = { Text("性别") },
                readOnly = true,
                modifier = Modifier.weight(1f).padding(end = 16.dp)
            )
            OutlinedTextField(
                value = ethnicity,
                onValueChange = { studentStatusModule.race = it },
                label = { Text("名族") },
                readOnly = true,
                modifier = Modifier.weight(1f)
            )
        }

        // 第二行
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            OutlinedTextField(
                value = origin,
                onValueChange = { studentStatusModule.nativePlace = it },
                label = { Text("籍贯") },
                readOnly = true,
                modifier = Modifier.weight(1f).padding(end = 16.dp)
            )
            OutlinedTextField(
                value = studentId,
                onValueChange = { studentStatusModule.studentId = it },
                label = { Text("一卡通") },
                readOnly = true,
                modifier = Modifier.weight(1f)
            )
        }

        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            OutlinedTextField(
                value = major,
                onValueChange = { studentStatusModule.major = it },
                label = { Text("专业") },
                readOnly = true,
                modifier = Modifier.weight(1f).padding(end = 16.dp)
            )
            OutlinedTextField(
                value = college,
                onValueChange = { studentStatusModule.academy = it },
                label = { Text("学院") },
                readOnly = true,
                modifier = Modifier.weight(1f)
            )
        }
    }
}