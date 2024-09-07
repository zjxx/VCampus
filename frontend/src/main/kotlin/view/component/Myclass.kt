package view.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment

data class Student(val name: String, val cardNumber: String)

@Composable
fun classCard(
    courseName: String,
    classTime: String,
    classroom: String,
    studentCount: Int,
    students: List<Student>,
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
            .clickable { expanded = !expanded },
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = courseName, fontWeight = FontWeight.Bold)
            Text(text = "时间: $classTime")
            Text(text = "教室: $classroom")
            Text(text = "人数: $studentCount")
            AnimatedVisibility(visible = expanded) {
                Column {
                    students.forEach { student ->
                        studentCard(student)
                    }
                }
            }
        }
    }
}

@Composable
fun studentCard(student: Student) {
    var expanded by remember { mutableStateOf(false) }
    var regularGrade by remember { mutableStateOf("85") }
    var midtermGrade by remember { mutableStateOf("88") }
    var finalGrade by remember { mutableStateOf("90") }
    var overallGrade by remember { mutableStateOf("89") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = student.name, fontWeight = FontWeight.Bold)
                    Text(text = "一卡通号: ${student.cardNumber}")
                }
                Button(onClick = { expanded = !expanded }) {
                    Text(text = if (expanded) "收起" else "打分")
                }
            }
            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .padding(top = 16.dp)
                ) {
                    Row {
                        OutlinedTextField(
                            value = regularGrade,
                            onValueChange = { regularGrade = it },
                            label = { Text("平时分") },
                            modifier = Modifier.weight(1f).padding(end = 8.dp)
                        )
                        OutlinedTextField(
                            value = midtermGrade,
                            onValueChange = { midtermGrade = it },
                            label = { Text("期中成绩") },
                            modifier = Modifier.weight(1f).padding(end = 8.dp)
                        )
                        OutlinedTextField(
                            value = finalGrade,
                            onValueChange = { finalGrade = it },
                            label = { Text("期末成绩") },
                            modifier = Modifier.weight(1f).padding(end = 8.dp)
                        )
                        OutlinedTextField(
                            value = overallGrade,
                            onValueChange = { overallGrade = it },
                            label = { Text("综合成绩") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = { /* Handle submit action */ },
                            modifier = Modifier.width(150.dp)
                        ) {
                            Text(text = "提交")
                        }
                    }
                }
            }
        }
    }
}