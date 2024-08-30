package view.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import module.StudentStatusModule

@Composable
fun CombinedStudentStatusCard(studentStatusModule: StudentStatusModule) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "学生姓名: ${studentStatusModule.name}")
            Text(text = "一卡通: ${studentStatusModule.studentId}")

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(start = 16.dp)) {
                    // 第一行
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                        OutlinedTextField(
                            value = studentStatusModule.name,
                            onValueChange = { studentStatusModule.name = it },
                            label = { Text("姓名") },
                            readOnly = true,
                            modifier = Modifier.weight(1f).padding(end = 16.dp)
                        )
                        OutlinedTextField(
                            value = studentStatusModule.gender,
                            onValueChange = { studentStatusModule.gender = it },
                            label = { Text("性别") },
                            readOnly = true,
                            modifier = Modifier.weight(1f).padding(end = 16.dp)
                        )
                        OutlinedTextField(
                            value = studentStatusModule.race,
                            onValueChange = { studentStatusModule.race = it },
                            label = { Text("名族") },
                            readOnly = true,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // 第二行
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                        OutlinedTextField(
                            value = studentStatusModule.nativePlace,
                            onValueChange = { studentStatusModule.nativePlace = it },
                            label = { Text("籍贯") },
                            readOnly = true,
                            modifier = Modifier.weight(1f).padding(end = 16.dp)
                        )
                        OutlinedTextField(
                            value = studentStatusModule.studentId,
                            onValueChange = { studentStatusModule.studentId = it },
                            label = { Text("一卡通") },
                            readOnly = true,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                        OutlinedTextField(
                            value = studentStatusModule.major,
                            onValueChange = { studentStatusModule.major = it },
                            label = { Text("专业") },
                            readOnly = true,
                            modifier = Modifier.weight(1f).padding(end = 16.dp)
                        )
                        OutlinedTextField(
                            value = studentStatusModule.academy,
                            onValueChange = { studentStatusModule.academy = it },
                            label = { Text("学院") },
                            readOnly = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}