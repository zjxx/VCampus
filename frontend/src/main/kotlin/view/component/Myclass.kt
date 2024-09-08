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
import androidx.compose.ui.graphics.Color


@Composable
fun classCard(
    courseName: String,
    courseId: String,
    timeAndLocationCards: List<TimeAndLocationCardData>,
    studentCount: Int,
    students: List<Student>,
    classStatus: String
) {
    var expanded by remember { mutableStateOf(false) }
    val courseIdColor = if (classStatus == "审核未通过") Color.Red else Color.Black
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { expanded = !expanded },
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = courseName, fontWeight = FontWeight.Bold,color = courseIdColor)
            Text(text = "课程ID: $courseId", fontWeight = FontWeight.Bold,color = courseIdColor)
            timeAndLocationCards.forEach { card ->
                Text(text = "时间: ${card.dayOfWeek} ${card.startPeriod}节-${card.endPeriod}节")
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
    val courseModule = CourseModule()
    var ParticipationScore by remember { mutableStateOf(student.ParticipationScore) }
    var MidtermScore by remember { mutableStateOf(student.MidtermScore) }
    var FinalScore by remember { mutableStateOf(student.FinalScore) }

    val overallGrade = remember(ParticipationScore, MidtermScore, FinalScore) {
        val regular = ParticipationScore.toFloatOrNull() ?: 0f
        val midterm = MidtermScore.toFloatOrNull() ?: 0f
        val final = FinalScore.toFloatOrNull() ?: 0f
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
                            value = ParticipationScore,
                            onValueChange = { ParticipationScore = it },
                            label = { Text("平时分") },
                            modifier = Modifier.weight(1f).padding(end = 8.dp)
                        )
                        OutlinedTextField(
                            value = MidtermScore,
                            onValueChange = { MidtermScore = it },
                            label = { Text("期中成绩") },
                            modifier = Modifier.weight(1f).padding(end = 8.dp)
                        )
                        OutlinedTextField(
                            value = FinalScore,
                            onValueChange = { FinalScore = it },
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
                        if (student.isScored == "true") {
                            Button(
                                onClick = {
                                    val studentScore = StudentScore(
                                        studentId = student.studentId,
                                        courseId = courseId,
                                        regularGrade = ParticipationScore,
                                        midtermGrade = MidtermScore,
                                        finalGrade = FinalScore,
                                        overallGrade = overallGrade
                                    )
                                    courseModule.ModifyScore(studentScore)
                                },
                                modifier = Modifier.width(150.dp)
                            ) {
                                Text(text = "修改")
                            }
                        } else {
                            Button(
                                onClick = {
                                    val studentScore = StudentScore(
                                        studentId = student.studentId,
                                        courseId = courseId,
                                        regularGrade = ParticipationScore,
                                        midtermGrade = MidtermScore,
                                        finalGrade = FinalScore,
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
}