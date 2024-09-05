// src/main/kotlin/view/ModifyCourseSubscene.kt
package view

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import module.CourseData
import module.CourseModule
import view.component.ModifyCourseCard

@Composable
fun ModifyCourseSubscene(course: CourseData, onSelectCourse: (CourseData, onSuccess: (Boolean) -> Unit) -> Unit, onUnselectCourse: (CourseData, onSuccess: (Boolean) -> Unit) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("修改课程", style = MaterialTheme.typography.h5, modifier = Modifier.padding(bottom = 16.dp))
        ModifyCourseCard(course = course, onSelectCourse = onSelectCourse, onUnselectCourse = onUnselectCourse)
    }
}