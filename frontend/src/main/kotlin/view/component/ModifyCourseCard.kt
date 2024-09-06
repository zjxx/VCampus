// src/main/kotlin/view/component/ModifyCourseCard.kt
package view.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import module.CourseData
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown

@Composable
fun ModifyCourseCard(courses: List<CourseData>, onDeleteSuccess: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var expandedProperty by remember { mutableStateOf(false) }
    val propertyOptions = listOf("必修", "选修")

    val course = courses.first()
    var courseName by remember { mutableStateOf(course.courseName) }
    var courseId by remember { mutableStateOf(course.courseId) }
    var credit by remember { mutableStateOf(course.credit) }
    var property by remember { mutableStateOf(course.property) }
    var courseIdPrefix by remember { mutableStateOf(course.courseIdPrefix) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .heightIn(max = 600.dp) // Set a maximum height to avoid infinite constraints
            .clickable { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(courseName, modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold)
                Text("课程号: $courseIdPrefix", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Text("学分: $credit", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Text("课程性质: $property", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            }
            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                        OutlinedTextField(
                            value = courseName,
                            onValueChange = { courseName = it },
                            label = { Text("课程名称") },
                            modifier = Modifier.weight(1f).padding(end = 8.dp)
                        )
                        OutlinedTextField(
                            value = courseIdPrefix,
                            onValueChange = { courseIdPrefix = it },
                            label = { Text("课程号") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                        OutlinedTextField(
                            value = credit,
                            onValueChange = { credit = it },
                            label = { Text("学分") },
                            modifier = Modifier.weight(1f).padding(end = 8.dp)
                        )
                        Box(modifier = Modifier.weight(1f)) {
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
                                        Text(option)
                                    }
                                }
                            }
                        }
                    }
                    courses.forEach { course ->
                        ModifydetailCard(course = course, onDeleteSuccess = {})
                    }
                }
            }
        }
    }
}