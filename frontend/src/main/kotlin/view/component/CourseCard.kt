// src/main/kotlin/view/component/CourseCard.kt
package view.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import module.GroupedCourse

@Composable
fun CourseCard(groupedCourse: GroupedCourse) {
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
                Text(groupedCourse.courseIdPrefix, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Text(groupedCourse.courseName, modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold)
                Text(groupedCourse.credit, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Text(groupedCourse.property, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            }
            AnimatedVisibility(visible = expanded) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 500.dp) // Set a maximum height to avoid infinite constraints
                ) {
                    items(groupedCourse.courses.size) { index ->
                        DetailCard(course = groupedCourse.courses[index])
                    }
                }
            }
        }
    }
}