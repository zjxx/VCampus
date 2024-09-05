// src/main/kotlin/view/ModifyCourseSubscene.kt
package view

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import module.CourseModule

@Composable
fun ModifyCourseSubscene() {
    val courseModule = remember { CourseModule() }
    val scope = rememberCoroutineScope()
    var courseName by remember { mutableStateOf("") }
    var courseId by remember { mutableStateOf("") }
    var teacher by remember { mutableStateOf("") }
    var capacity by remember { mutableStateOf("") }
    var validCapacity by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var credit by remember { mutableStateOf("") }
    var property by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("修改课程", style = MaterialTheme.typography.h5, modifier = Modifier.padding(bottom = 16.dp))

        OutlinedTextField(
            value = courseName,
            onValueChange = { courseName = it },
            label = { Text("课程名称") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = courseId,
            onValueChange = { courseId = it },
            label = { Text("课程ID") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = teacher,
            onValueChange = { teacher = it },
            label = { Text("教师") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = capacity,
            onValueChange = { capacity = it },
            label = { Text("课容量") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = validCapacity,
            onValueChange = { validCapacity = it },
            label = { Text("剩余课容量") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("教室") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = time,
            onValueChange = { time = it },
            label = { Text("时间") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = credit,
            onValueChange = { credit = it },
            label = { Text("学分") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = property,
            onValueChange = { property = it },
            label = { Text("属性") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        Button(
            onClick = {
                scope.launch {
                    // Modify course logic here
                }
            },
            modifier = Modifier.align(Alignment.End).padding(top = 16.dp)
        ) {
            Text("提交")
        }
    }
}