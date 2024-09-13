
// src/main/kotlin/view/AddCourseSubscene.kt
package view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import module.CourseModule
import module.CourseData
import module.TimeAndLocationCardData
import view.component.pageTitle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete

@Composable
fun AddCourseSubscene() {
    val courseModule = remember { CourseModule() }
    val scope = rememberCoroutineScope()
    var courseName by remember { mutableStateOf("") }
    var courseId by remember { mutableStateOf("") }
    var credit by remember { mutableStateOf("") }
    var capacity by remember { mutableStateOf("") }
    var grade by remember { mutableStateOf("") }
    var major by remember { mutableStateOf("") }
    var semester by remember { mutableStateOf("") }
    var property by remember { mutableStateOf("") }
    var teacher by remember { mutableStateOf("") } // New state
    var teacherId by remember { mutableStateOf("") } // New state
    var timeAndLocationCards by remember { mutableStateOf(listOf(TimeAndLocationCardData("", "", "", ""))) }
    var courseIdPrefix by remember { mutableStateOf("") }
    val gradeOptions = listOf("大一", "大二", "大三", "大四")
    val semesterOptions = listOf("第一学期", "第二学期", "第三学期")
    val propertyOptions = listOf("选修", "任选")
    Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
        pageTitle(heading = "增加课程", caption = "增加新的课程")

        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            OutlinedTextField(
                value = courseName,
                onValueChange = { courseName = it },
                label = { Text("课程名称") },
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            )
            OutlinedTextField(
    value = courseId,
    onValueChange = {
        if (it.length <= 8) {
            courseId = it
        }
    },
    label = { Text("课程号") },
    modifier = Modifier.weight(1f).padding(end = 8.dp)
)
            Box(modifier = Modifier.weight(1f)) {
    var expandedProperty by remember { mutableStateOf(false) }
    val propertyOptions = listOf("选修", "必修")

    OutlinedTextField(
        value = property,
        onValueChange = { property = it },
        label = { Text("课程性质") },
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { expandedProperty = true }) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }
        }
    )
    DropdownMenu(
        expanded = expandedProperty,
        onDismissRequest = { expandedProperty = false }
    ) {
        propertyOptions.forEach { option ->
            DropdownMenuItem(onClick = {
                property = option
                expandedProperty = false
            }) {
                Text(text = option)
            }
        }
    }
}
        }
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            OutlinedTextField(
                value = teacher,
                onValueChange = { teacher = it },
                label = { Text("老师") },
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            )
            OutlinedTextField(
                value = teacherId,
                onValueChange = { teacherId = it },
                label = { Text("一卡通号   ") },
                modifier = Modifier.weight(1f)
            )
        }

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
                value = credit,
                onValueChange = { credit = it },
                label = { Text("学分") },
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            )
            OutlinedTextField(
                value = capacity,
                onValueChange = { capacity = it },
                label = { Text("课容量") },
                modifier = Modifier.weight(1f)
            )
        }

        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            var expandedGrade by remember { mutableStateOf(false) }
            var expandedSemester by remember { mutableStateOf(false) }

            Box(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                OutlinedTextField(
                    value = grade,
                    onValueChange = { grade = it },
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
                    gradeOptions.forEach { option ->
                        DropdownMenuItem(onClick = {
                            var validgrade = "24";
                            if(option == "大二"){
                                validgrade = "23";
                            }
                            else if(option == "大三"){
                                validgrade = "22";
                            }
                            else if(option == "大四"){
                                validgrade = "21";
                            }
                            grade = validgrade
                            expandedGrade = false
                        }) {
                            Text(option)
                        }
                    }
                }
            }

            Box(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                OutlinedTextField(
                    value = semester,
                    onValueChange = { semester = it },
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
                    semesterOptions.forEach { option ->
                        DropdownMenuItem(onClick = {
                            semester = option
                            expandedSemester = false
                        }) {
                            Text(option)
                        }
                    }
                }
            }

            OutlinedTextField(
                value = major,
                onValueChange = { major = it },
                label = { Text("专业") },
                modifier = Modifier.weight(1f)
            )
        }

        Button(
            onClick = {
                val time = timeAndLocationCards.joinToString(";") { "${it.getDayOfWeekNumber()}-${it.startPeriod}-${it.endPeriod}" }
                val location = timeAndLocationCards.joinToString(";") { it.location }
                val courseData = CourseData(
                    courseName = courseName,
                    courseId = courseId,
                    credit = credit,
                    capacity = capacity,
                    grade = grade,
                    major = major,
                    semester = semester,
                    property = property,
                    time=time,
                    location = location,
                    timeAndLocationCards = timeAndLocationCards,
                    teacher = teacher, // New field
                    teacherId = teacherId ,// New field
                 courseIdPrefix = courseIdPrefix
                )
                scope.launch {
                    courseModule.addCourse(courseData) { success ->
                        if (success) {
                            // Handle success
                        } else {
                            // Handle failure
                        }
                    }
                }
            },
            modifier = Modifier.align(Alignment.End).padding(top = 16.dp)
        ) {
            Text("提交")
        }
    }
}