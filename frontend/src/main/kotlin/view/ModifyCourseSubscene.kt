// src/main/kotlin/view/ModifyCourseSubscene.kt
package view

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import module.CourseModule
import module.CourseData
import view.component.pageTitle
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.unit.dp
import view.component.ModifyCourseCard

@Composable
fun ModifyCourseSubscene(courseModule: CourseModule) {
    val courses by courseModule.course.collectAsState()
    val groupedCourses = courses.groupBy { it.courseIdPrefix }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        pageTitle(heading = "修改课程", caption = "修改课程")
        groupedCourses.forEach { (courseIdPrefix, courses) ->
            ModifyCourseCard(courses = courses, onDeleteSuccess = {})
        }
    }
}