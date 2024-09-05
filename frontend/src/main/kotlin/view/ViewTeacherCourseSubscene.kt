package view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import view.component.ClassCard

@Composable
fun ViewTeacherCourseSubscene() {
    val courses = listOf(
        // Example data
        Triple("课程A", "周一 8:00-10:00", "教室101"),
        Triple("课程B", "周二 10:00-12:00", "教室102")
    )
    val studentCounts = listOf(30, 25)
    val studentLists = listOf(
        listOf("学生1", "学生2", "学生3"),
        listOf("学生4", "学生5", "学生6")
    )

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Text("课程列表", style = MaterialTheme.typography.h5, modifier = Modifier.padding(bottom = 16.dp))
        }
        items(courses.size) { index ->
            ClassCard(
                courseName = courses[index].first,
                time = courses[index].second,
                location = courses[index].third,
                studentCount = studentCounts[index],
                studentList = studentLists[index]
            )
        }
    }
}