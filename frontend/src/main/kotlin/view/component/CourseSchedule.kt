// src/main/kotlin/view/CourseSchedule.kt
package view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CourseSchedule(courses: List<Course>) {
    val daysOfWeek = listOf("星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日")
    val periods = (1..13).toList()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "", modifier = Modifier.weight(1f)) // Empty cell for top-left corner
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f).padding(8.dp).background(Color.LightGray)
                )
            }
        }

        periods.forEach { period ->
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "第${period}节",
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f).padding(8.dp).background(Color.LightGray)
                )
                daysOfWeek.forEach { day ->
                    val course = courses.find { it.day == day && it.period == period }
                    Box(
                        modifier = Modifier.weight(1f).padding(8.dp).background(Color.White)
                    ) {
                        course?.let {
                            Text(text = it.name, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}

data class Course(val name: String, val day: String, val period: Int)