package view

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import view.component.pageTitle
import module.Class
import module.CourseModule
import view.component.ConfirmCard
import view.component.classCard

@Composable
fun ConfirmGrade(classes: List<Class>) {


    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        pageTitle(heading = "成绩审核", caption = "审核新的课程")
        classes.forEach { classItem: Class ->
            ConfirmCard(
                courseName = classItem.courseName,
                courseId = classItem.courseId,
                timeAndLocationCards = classItem.timeAndLocationCards,
                studentCount = classItem.students.size,
                students = classItem.students,
            )
        }
    }
}