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

@Composable
fun ClassCard(courseName: String, time: String, location: String, studentCount: Int, studentList: List<String>) {
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
                Text(courseName, modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold)
                Text(time, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Text(location, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            }
            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "上课人数: $studentCount", style = MaterialTheme.typography.body1)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "学生名单:", style = MaterialTheme.typography.body1)
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 500.dp) // Set a maximum height to avoid infinite constraints
                    ) {
                        items(studentList.size) { index ->
                            Text(text = studentList[index], style = MaterialTheme.typography.body2)
                        }
                    }
                }
            }
        }
    }
}