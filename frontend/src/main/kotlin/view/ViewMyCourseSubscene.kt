// src/main/kotlin/view/ViewMyCoursesSubscene.kt
package view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import module.CourseModule
import module.CourseScheduleItem

@Composable
fun ViewMyCoursesSubscene(courseModule: CourseModule) {
    val courses by courseModule.courseSchedule.collectAsState()
    CourseSchedule(courses= courses)
}