// src/main/kotlin/view/AddFromFileSubscene.kt
package view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import module.StudentStatusModule
import view.component.FileCard

@Composable
fun AddFromFileSubscene(students: List<StudentStatusModule>, onUpdateFile: (List<StudentStatusModule>) -> Unit, filePath: String) {
    val scrollState = rememberScrollState()
    Column(modifier = Modifier.padding(16.dp)) {
        Text("从文件导入的学生信息", style = MaterialTheme.typography.h6, modifier = Modifier.padding(bottom = 8.dp))
        Box(modifier = Modifier.verticalScroll(scrollState).padding(16.dp)) {
            Column {
                students.forEach { student ->
                    FileCard(
                        studentStatusModule = student,
                        onDeleteSuccess = {
                            val updatedStudents = students.toMutableList().apply { remove(student) }
                            onUpdateFile(updatedStudents)
                        },
                        onUpdateSuccess = { updatedStudent ->
                            val updatedStudents = students.map { if (it.studentId == updatedStudent.studentId) updatedStudent else it }
                            onUpdateFile(updatedStudents)
                        },
                        filePath = filePath
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}