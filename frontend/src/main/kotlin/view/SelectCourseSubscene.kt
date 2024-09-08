// src/main/kotlin/view/SelectCourseSubscene.kt
package view

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import view.component.GlobalDialog
import view.component.CourseCard
import module.CourseModule

@Composable

fun SelectCourseSubscene(courseModule: CourseModule) {
    var searchQuery by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val groupedCourses by courseModule.searchResults.collectAsState()

    Column(modifier = Modifier.verticalScroll(scrollState).padding(16.dp)) {

        Spacer(modifier = Modifier.height(16.dp))

        // Title bar
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            Spacer(modifier = Modifier.weight(0.15f))
            Text("课程号前缀", modifier = Modifier.weight(0.95f), fontWeight = FontWeight.Bold)
            Text("课程名称", modifier = Modifier.weight(1.8f), fontWeight = FontWeight.Bold)
            Text("学分", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            Text("课程性质", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
        }

        // 选课内容
        groupedCourses.forEach { groupedCourse ->
            CourseCard(groupedCourse, courseModule)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
    GlobalDialog()
}