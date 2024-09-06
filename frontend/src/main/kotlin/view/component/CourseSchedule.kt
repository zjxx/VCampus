// src/main/kotlin/view/CourseSchedule.kt
package view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import module.CourseModule
import androidx.compose.ui.unit.sp
import module.CourseScheduleItem

@Composable
fun CourseSchedule(courses: List<CourseScheduleItem>) {


    Box(Modifier.fillMaxWidth().height(700.dp)) {
        // TABLE
        Row(Modifier.fillMaxSize()) {
            Column(Modifier.weight(1F).fillMaxHeight()) {
                Divider(
                    Modifier.fillMaxWidth(), thickness = 1.dp,
                    color = Color.LightGray
                )
                Row(Modifier.weight(1F).fillMaxWidth()) {}
                (1..13).forEach {
                    Divider(
                        Modifier.fillMaxWidth(), thickness = 1.dp,
                        color = Color.LightGray
                    )
                    Row(
                        Modifier.weight(1F).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("第 $it 节")
                    }
                }
                Divider(
                    Modifier.fillMaxWidth(), thickness = 1.dp,
                    color = Color.LightGray
                )
            }

            (1..7).forEach { weekday ->
                Column(Modifier.weight(1F).fillMaxHeight()) {
                    Divider(
                        Modifier.fillMaxWidth(), thickness = 1.dp,
                        color = Color.LightGray
                    )
                    Row(
                        Modifier.weight(1F).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            when (weekday) {
                                1 -> "星期一"
                                2 -> "星期二"
                                3 -> "星期三"
                                4 -> "星期四"
                                5 -> "星期五"
                                6 -> "星期六"
                                7 -> "星期日"
                                else -> "?"
                            }
                        )
                    }
                    (1..13).forEach { period ->
                        Divider(
                            Modifier.fillMaxWidth(), thickness = 1.dp,
                            color = Color.LightGray
                        )
                        Row(Modifier.weight(1F).fillMaxWidth()) {}
                    }
                    Divider(
                        Modifier.fillMaxWidth(), thickness = 1.dp,
                        color = Color.LightGray
                    )
                }
            }
        }
        // CLASS
        Row(Modifier.fillMaxSize()) {
            Column(Modifier.weight(1F).fillMaxHeight()) {
                Divider(
                    Modifier.fillMaxWidth(), thickness = 1.dp,
                    color = Color.Transparent
                )
                Row(Modifier.weight(1F).fillMaxWidth()) {}
                (1..13).forEach {
                    Divider(
                        Modifier.fillMaxWidth(), thickness = 1.dp,
                        color = Color.Transparent
                    )
                    Row(
                        Modifier.weight(1F).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                    }
                }
                Divider(
                    Modifier.fillMaxWidth(), thickness = 1.dp,
                    color = Color.Transparent
                )
            }

            (1..7).forEach { weekday ->
                Column(Modifier.weight(1F).fillMaxHeight()) {
                    Divider(
                        Modifier.fillMaxWidth(), thickness = 1.dp,
                        color = Color.Transparent
                    )
                    Row(Modifier.weight(1F).fillMaxWidth()) {
                    }
                    var index = 1
                    while (index <= 13) {
                        val classHere = courses.find {
                            it.dayOfWeek == when (weekday) {
                                1 -> "星期一"
                                2 -> "星期二"
                                3 -> "星期三"
                                4 -> "星期四"
                                5 -> "星期五"
                                6 -> "星期六"
                                7 -> "星期日"
                                else -> "?"
                            } && it.startPeriod == index
                        }
                        if (classHere != null) {
                            Box(
                                Modifier.fillMaxWidth().weight(
                                    (classHere.endPeriod - classHere.startPeriod + 1).toFloat()
                                )
                            ) {
                                classItem(
                                    classHere.courseName,
                                    "", // Replace with actual teacher name if available
                                    classHere.location // Replace with actual classroom if available
                                )
                            }
                            (1..(classHere.endPeriod - classHere.startPeriod + 1)).forEach {
                                Divider(
                                    Modifier.fillMaxWidth(),
                                    thickness = 1.dp,
                                    color = Color.Transparent
                                )
                            }
                            index += (classHere.endPeriod - classHere.startPeriod + 1)
                        } else {
                            Divider(
                                Modifier.fillMaxWidth(), thickness = 1.dp,
                                color = Color.Transparent
                            )
                            Row(Modifier.weight(1F).fillMaxWidth()) {
                            }
                            index += 1
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun classItem(className: String, teacherName: String, position: String) {
    val hashCode = (className + teacherName).hashCode().mod(6)
    val colors = listOf(
        Color(0xff72b8a0), Color(0xff7fc882), Color(0xff6296d1),
        Color(0xff8b5ead), Color(0xffe5c95c), Color(0xffc85f4b)
    )
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .shadow(
                elevation = 2.dp,
                shape = MaterialTheme.shapes.medium
            ),
        backgroundColor = colors[hashCode]
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(className, color = Color.White)
            Text(teacherName, color = Color.White)
            Text(position, color = Color.White)
        }
    }
}