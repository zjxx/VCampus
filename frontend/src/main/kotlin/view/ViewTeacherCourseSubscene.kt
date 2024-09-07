// src/main/kotlin/view/ViewTeacherCourseSubscene.kt
package view

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import view.component.Student
import view.component.classCard

@Composable
fun ViewTeacherCourseSubscene() {
    val students = listOf(
        Student("Alice", "123456"),
        Student("Bob", "654321")
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        classCard(
            courseName = "Math 101",
            classTime = "10:00 AM - 11:30 AM",
            classroom = "Room 101",
            studentCount = students.size,
            students = students,
        )
    }
}