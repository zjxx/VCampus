package view.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import module.StudentStatusModule
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete

@Composable
fun CombinedStudentStatusCard(studentStatusModule: StudentStatusModule) {
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("确认删除") },
            text = { Text("你确定要删除这个学生信息吗？") },
            confirmButton = {
                Button(onClick = {
                    studentStatusModule.deleteStudentStatus()
                    showDialog = false
                }) {
                    Text("确认")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("取消")
                }
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "学生姓名: ${studentStatusModule.name}")
                    Text(text = "一卡通: ${studentStatusModule.studentId}")
                }
                if (!expanded) {
                    IconButton(onClick = { showDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "删除")
                    }
                }
            }

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

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Button(onClick = { studentStatusModule.modifyStudentStatus() }) {
                            Text("确认修改")
                        }
                    }
                }
            }
        }
    }
}