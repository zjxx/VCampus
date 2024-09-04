// src/main/kotlin/view/component/DetailCard.kt
package view.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import module.CourseDetail
import module.CourseModule

@Composable
fun DetailCard(courseId: String) {
    val courseModule = remember { CourseModule() }
    var courseDetails by remember { mutableStateOf<List<CourseDetail>>(emptyList()) }

    LaunchedEffect(courseId) {
        courseModule.fetchCourseDetails(courseId)
        courseDetails = courseModule.courseDetails
    }

    Column {
        courseDetails.forEach { detail ->
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .width(200.dp) // Set a fixed width for each DetailCard
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("教师: ${detail.teacherName}", fontWeight = FontWeight.Bold)
                    Text("上课时间: ${detail.classTime}")
                    Text("客容量: ${detail.capacity}")
                    Text("已选人数: ${detail.selectedCount}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {
                        // Handle select/deselect action
                    }) {
                        Text("选择")
                    }
                }
            }
        }
    }
}