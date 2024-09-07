package view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Column
import module.CourseModule
import view.component.courseCard

@Composable
fun MygradeSubscene(courseModule: CourseModule) {
    val courses by courseModule.course.collectAsState()
    Column {
        courses.forEach { course ->
            courseCard(
                courseName = course.courseName,
                courseId = course.courseId,
                overallGrade = course.grade,
                regularGrade = "85", // Replace with actual data
                midtermGrade = "90", // Replace with actual data
                finalGrade = "95" // Replace with actual data
            )
        }
    }
}