package view.component

import androidx.compose.runtime.Composable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.animation.AnimatedVisibility

@Composable
fun courseCard(courseName: String, courseId: String, overallGrade: String, regularGrade: String, midtermGrade: String, finalGrade: String) {
    var expanded = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { expanded.value = !expanded.value },
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = courseName, fontWeight = FontWeight.Bold)
            Text(text = "课程ID: $courseId", fontWeight = FontWeight.Bold)
            Text(text = "总成绩: $overallGrade", fontWeight = FontWeight.Bold)
            AnimatedVisibility(visible = expanded.value) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    Text(text = "平时成绩: $regularGrade")
                    Text(text = "期中成绩: $midtermGrade")
                    Text(text = "期末成绩: $finalGrade")
                }
            }
        }
    }
}