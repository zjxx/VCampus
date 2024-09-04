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
fun DetailCard(course: Course) {
    var isSelected by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text("教师: ${course.teacher}", fontWeight = FontWeight.Bold)
            course.timeSlots.forEach { timeSlot ->
                Text("周次: ${timeSlot.week} 第${timeSlot.begin}-${timeSlot.end}节", fontWeight = FontWeight.Bold)
            }
            Text("教室: ${course.location}", fontWeight = FontWeight.Bold)
            Text("客容量: ${course.capacity}", fontWeight = FontWeight.Bold)
            Text("已选人数: ${course.validCapacity}", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                isSelected = !isSelected
                // Add your selection/deselection logic here
            }) {
                Text(if (isSelected) "退选" else "选择")
            }
        }
    }
}