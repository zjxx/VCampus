// src/main/kotlin/view/component/DetailCard.kt
package view.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import module.Course

@Composable
fun DetailCard(course: Course, onSelectCourse: (Course, onSuccess: (Boolean) -> Unit) -> Unit, onUnselectCourse: (Course, onSuccess: (Boolean) -> Unit) -> Unit) {
    var isSelected by remember { mutableStateOf(course.isSelect) }
    var validCapacity by remember { mutableStateOf(course.validCapacity.toInt()) }

    LaunchedEffect(isSelected) {
        validCapacity = course.validCapacity.toInt()
    }

    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text("教师: ${course.teacher}", fontWeight = FontWeight.Bold)
            course.timeAndLocationCards.forEach { timeAndLocationCard ->
                Text(" ${timeAndLocationCard.dayOfWeek} 第${timeAndLocationCard.startPeriod}-${timeAndLocationCard.endPeriod}节  教室: ${timeAndLocationCard.location}", fontWeight = FontWeight.Bold)
            }
            Text("课容量: ${course.capacity}", fontWeight = FontWeight.Bold)
            Text("剩余课容量: $validCapacity", fontWeight = FontWeight.Bold) // Display updated validCapacity
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                if (isSelected) {
                    onUnselectCourse(course) { result ->
                        if (result) {
                            isSelected = false
                            validCapacity += 1 // Increment validCapacity immediately
                        }
                    }
                } else {
                    onSelectCourse(course) { result ->
                        if (result) {
                            isSelected = true
                            validCapacity -= 1 // Decrement validCapacity immediately
                        }
                    }
                }
            }) {
                Text(if (isSelected) "退选" else "选择")
            }
        }
    }
}