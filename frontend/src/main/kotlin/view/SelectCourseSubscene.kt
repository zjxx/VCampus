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

@Composable
fun SelectCourseSubscene(courseModule: CourseModule) {
    var searchQuery by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val groupedCourses by courseModule.searchResults.collectAsState()

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
            Text("课程号前缀", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            Text("课程名称", modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold)
            Text("学分", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            Text("课程性质", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
        }

        // 选课内容
        groupedCourses.forEach { groupedCourse ->
            CourseCard(groupedCourse, courseModule)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}