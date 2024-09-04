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
import view.component.CourseCard
import module.CourseModule
import module.Course

@Composable
fun SelectCourseSubscene() {
    var searchQuery by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val courseModule = remember { CourseModule() }
    val courses by remember { mutableStateOf(courseModule.searchResults) }

    Column(modifier = Modifier.verticalScroll(scrollState).padding(16.dp)) {
        // 搜索框和搜索按钮
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("搜索课程") },
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            )
            Button(onClick = {
                courseModule.searchCourses(searchQuery)
            }) {
                Text("搜索")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Title bar
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            Text("课程号", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            Text("课程名称", modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold)
            Text("教学班个数", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            Text("课程性质", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            Text("学分", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            Text("教师", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold) // Add teacher column
        }

        // 选课内容
        courses.forEach { course ->
            CourseCard(course)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}