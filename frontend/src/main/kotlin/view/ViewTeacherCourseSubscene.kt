// src/main/kotlin/view/ViewTeacherCourseSubscene.kt
package view

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import module.Class
import module.CourseModule
import view.component.classCard

@Composable
fun ViewTeacherCourseSubscene(classes: List<Class>) {


    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        classes.forEach { classItem: Class ->
            classCard(
                courseName = classItem.courseName,
                courseId = classItem.courseId,
                timeAndLocationCards = classItem.timeAndLocationCards,
                studentCount = classItem.students.size,
                students = classItem.students,
            )
        }
    }
}