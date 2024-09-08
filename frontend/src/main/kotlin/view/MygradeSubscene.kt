package view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Column
import module.CourseModule
import view.component.courseCard // Ensure this import is present

@Composable
fun MygradeSubscene(courseModule: CourseModule) {
    val courses by courseModule.ScoreMy.collectAsState()
    Column {
        courses.forEach { scoreMy ->
            courseCard(
                courseName = scoreMy.courseName,
                courseId = scoreMy.courseId,
                overallGrade = scoreMy.score,
                regularGrade = scoreMy.participationScore,
                midtermGrade = scoreMy.midtermScore,
                finalGrade = scoreMy.finalScore
            )
        }
    }
}