// src/main/kotlin/view/ViewMyCoursesSubscene.kt
package view

import androidx.compose.runtime.Composable

@Composable
fun ViewMyCoursesSubscene() {
    val courses = listOf(
        Course("数学", "星期一", 1),
        Course("英语", "星期二", 2),
        Course("物理", "星期三", 3),
        Course("化学", "星期四", 4),
        Course("生物", "星期五", 5),
        Course("历史", "星期六", 6),
        Course("地理", "星期日", 7)
    )

    CourseSchedule(courses)
}