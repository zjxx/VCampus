// src/main/kotlin/view/component/ModifydetailCard.kt
package view.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import module.CourseData
import module.TimeAndLocationCardData
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.Alignment
import module.CourseModule

@Composable
fun ModifydetailCard(course: CourseData, onDeleteSuccess: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var timeAndLocationCards by remember { mutableStateOf(course.timeAndLocationCards) }
    var capacity by remember { mutableStateOf(course.capacity) }
    var major by remember { mutableStateOf(course.major) }
    var teacher by remember { mutableStateOf(course.teacher) }
    var teacherId by remember { mutableStateOf(course.teacherId) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val courseModule = CourseModule()
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("确认删除") },
            text = { Text("你确定要删除该课程吗？") },
            confirmButton = {
                TextButton(onClick = {
                    courseModule.deleteCourse(course) {
                        onDeleteSuccess()
                        showDeleteDialog = false
                    }
                }) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("取消")
                }
            }
        )
    }

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
                Column(modifier = Modifier.weight(1f)) {
                    Text("教师: ${teacher}", fontWeight = FontWeight.Bold)
                    Text("一卡通号: ${teacherId}", fontWeight = FontWeight.Bold)
                }
                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(Icons.Default.Delete, contentDescription = "删除")
                }
            }
            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    timeAndLocationCards.forEachIndexed { index, cardData ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                                    var expandedDayOfWeek by remember { mutableStateOf(false) }
                                    var expandedStartPeriod by remember { mutableStateOf(false) }
                                    var expandedEndPeriod by remember { mutableStateOf(false) }

                                    Box(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                                        OutlinedTextField(
                                            value = cardData.dayOfWeek,
                                            onValueChange = { newDayOfWeek ->
                                                timeAndLocationCards = timeAndLocationCards.toMutableList().apply {
                                                    this[index] = this[index].copy(dayOfWeek = newDayOfWeek)
                                                }
                                            },
                                            label = { Text("星期") },
                                            readOnly = true,
                                            trailingIcon = {
                                                IconButton(onClick = { expandedDayOfWeek = true }) {
                                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                                }
                                            }
                                        )
                                        DropdownMenu(
                                            expanded = expandedDayOfWeek,
                                            onDismissRequest = { expandedDayOfWeek = false }
                                        ) {
                                            listOf("星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日").forEach { option ->
                                                DropdownMenuItem(onClick = {
                                                    timeAndLocationCards = timeAndLocationCards.toMutableList().apply {
                                                        this[index] = this[index].copy(dayOfWeek = option)
                                                    }
                                                    expandedDayOfWeek = false
                                                }) {
                                                    Text(option)
                                                }
                                            }
                                        }
                                    }

                                    Box(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                                        OutlinedTextField(
                                            value = cardData.startPeriod,
                                            onValueChange = { newStartPeriod ->
                                                timeAndLocationCards = timeAndLocationCards.toMutableList().apply {
                                                    this[index] = this[index].copy(startPeriod = newStartPeriod)
                                                }
                                            },
                                            label = { Text("开始节") },
                                            readOnly = true,
                                            trailingIcon = {
                                                IconButton(onClick = { expandedStartPeriod = true }) {
                                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                                }
                                            }
                                        )
                                        DropdownMenu(
                                            expanded = expandedStartPeriod,
                                            onDismissRequest = { expandedStartPeriod = false }
                                        ) {
                                            (1..12).forEach { option ->
                                                DropdownMenuItem(onClick = {
                                                    timeAndLocationCards = timeAndLocationCards.toMutableList().apply {
                                                        this[index] = this[index].copy(startPeriod = option.toString())
                                                    }
                                                    expandedStartPeriod = false
                                                }) {
                                                    Text(option.toString())
                                                }
                                            }
                                        }
                                    }

                                    Box(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                                        OutlinedTextField(
                                            value = cardData.endPeriod,
                                            onValueChange = { newEndPeriod ->
                                                timeAndLocationCards = timeAndLocationCards.toMutableList().apply {
                                                    this[index] = this[index].copy(endPeriod = newEndPeriod)
                                                }
                                            },
                                            label = { Text("结束节") },
                                            readOnly = true,
                                            trailingIcon = {
                                                IconButton(onClick = { expandedEndPeriod = true }) {
                                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                                }
                                            }
                                        )
                                        DropdownMenu(
                                            expanded = expandedEndPeriod,
                                            onDismissRequest = { expandedEndPeriod = false }
                                        ) {
                                            (1..12).forEach { option ->
                                                DropdownMenuItem(onClick = {
                                                    timeAndLocationCards = timeAndLocationCards.toMutableList().apply {
                                                        this[index] = this[index].copy(endPeriod = option.toString())
                                                    }
                                                    expandedEndPeriod = false
                                                }) {
                                                    Text(option.toString())
                                                }
                                            }
                                        }
                                    }

                                    OutlinedTextField(
                                        value = cardData.location,
                                        onValueChange = { newLocation ->
                                            timeAndLocationCards = timeAndLocationCards.toMutableList().apply {
                                                this[index] = this[index].copy(location = newLocation)
                                            }
                                        },
                                        label = { Text("教室") },
                                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                                    )

                                    IconButton(
                                        onClick = {
                                            timeAndLocationCards = timeAndLocationCards.toMutableList().apply { removeAt(index) }
                                        },
                                        modifier = Modifier.align(Alignment.CenterVertically)
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "删除")
                                    }
                                }

                                if (cardData.startPeriod.toIntOrNull() ?: 0 > cardData.endPeriod.toIntOrNull() ?: 0) {
                                    Text("开始节必须小于或等于结束节", color = MaterialTheme.colors.error)
                                }
                            }
                        }
                    }

                    Button(
                        onClick = {
                            timeAndLocationCards = timeAndLocationCards + TimeAndLocationCardData("", "", "", "")
                        },
                        modifier = Modifier.align(Alignment.End).padding(bottom = 8.dp)
                    ) {
                        Text("添加时间和教室")
                    }
                    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                        OutlinedTextField(
                            value = teacher,
                            onValueChange = {
                                teacher = it
                                course.teacher = it
                            },
                            label = { Text("老师") },
                            modifier = Modifier.weight(1f).padding(end = 8.dp)
                        )
                        OutlinedTextField(
                            value = teacherId,
                            onValueChange = {
                                teacherId = it
                                course.teacherId = it
                            },
                            label = { Text("一卡通号") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                        OutlinedTextField(
                            value = major,
                            onValueChange = {
                                major = it
                                course.major = it },
                            label = { Text("专业") },
                            modifier = Modifier.weight(1f).padding(end = 8.dp)
                        )
                        OutlinedTextField(
                            value = capacity,
                            onValueChange = {
                                capacity = it
                                course.capacity = it },
                            label = { Text("课容量") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                        var expandedGrade by remember { mutableStateOf(false) }
                        var expandedSemester by remember { mutableStateOf(false) }

                        Box(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                            OutlinedTextField(
                                value = course.grade,
                                onValueChange = { course.grade = it },
                                label = { Text("年级") },
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = { expandedGrade = true }) {
                                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                    }
                                }
                            )
                            DropdownMenu(
                                expanded = expandedGrade,
                                onDismissRequest = { expandedGrade = false }
                            ) {
                                listOf("大一", "大二", "大三", "大四").forEach { option ->
                                    DropdownMenuItem(onClick = {
                                        course.grade = option
                                        expandedGrade = false
                                    }) {
                                        Text(option)
                                    }
                                }
                            }
                        }

                        Box(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                            OutlinedTextField(
                                value = course.semester,
                                onValueChange = { course.semester = it },
                                label = { Text("学期") },
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = { expandedSemester = true }) {
                                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                    }
                                }
                            )
                            DropdownMenu(
                                expanded = expandedSemester,
                                onDismissRequest = { expandedSemester = false }
                            ) {
                                listOf("第一学期", "第二学期", "第三学期").forEach { option ->
                                    DropdownMenuItem(onClick = {
                                        course.semester = option
                                        expandedSemester = false
                                    }) {
                                        Text(option)
                                    }
                                }
                            }
                        }

                    }
                    Button(
                        onClick = {
                            courseModule.modifyCourse(course)
                        },
                        modifier = Modifier.align(Alignment.End).padding(top = 16.dp)
                    ) {
                        Text("确认修改")
                    }
                }
            }
        }
    }
}