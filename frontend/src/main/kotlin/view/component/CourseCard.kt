// src/main/kotlin/view/component/CourseCard.kt
package view.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import module.Course

@Composable
fun CourseCard(course: Course) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(course.id, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Text(course.name, modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold)
                Text(course.classCount, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Text(course.type, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Text(course.credits, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            }
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    DetailCard(course.id) // Pass course ID to DetailCard
                }
            }
        }
    }
}