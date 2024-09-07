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
import module.Student
import module.TimeAndLocationCardData
import module.StudentScore
import module.CourseModule
import kotlin.math.roundToInt


@Composable
fun classCard(
    courseName: String,
    courseId: String,
    timeAndLocationCards: List<TimeAndLocationCardData>,
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
            Text(text = "课程ID: $courseId", fontWeight = FontWeight.Bold)
            timeAndLocationCards.forEach { card ->
                Text(text = "时间: 周${card.dayOfWeek} ${card.startPeriod}节-${card.endPeriod}节")
                Text(text = "教室: ${card.location}")
            }
            Text(text = "人数: $studentCount")
            AnimatedVisibility(visible = expanded) {
                Column {
                    students.forEach { student ->
                        studentCard(student, courseId)
                    }
                }
            }
        }
    }
}

@Composable
fun studentCard(student: Student, courseId: String) {
    var expanded by remember { mutableStateOf(false) }
    var regularGrade by remember { mutableStateOf("") }
    var midtermGrade by remember { mutableStateOf("") }
    var finalGrade by remember { mutableStateOf("") }
    val courseModule = CourseModule()

    val overallGrade = remember(regularGrade, midtermGrade, finalGrade) {
        val regular = regularGrade.toFloatOrNull() ?: 0f
        val midterm = midtermGrade.toFloatOrNull() ?: 0f
        val final = finalGrade.toFloatOrNull() ?: 0f
        (regular * 0.3f + midterm * 0.2f + final * 0.5f).roundToInt().toString()
    }

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
                    Text(text = "一卡通号: ${student.studentId}")
                }
                if (student.isScored == "true") {
                    Text(text = "成绩: ${student.score}", fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 8.dp))
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
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "综合成绩: $overallGrade", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                val studentScore = StudentScore(
                                    studentId = student.studentId,
                                    courseId = courseId,
                                    regularGrade = regularGrade,
                                    midtermGrade = midtermGrade,
                                    finalGrade = finalGrade,
                                    overallGrade = overallGrade
                                )
                                courseModule.giveScore(studentScore)
                            },
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