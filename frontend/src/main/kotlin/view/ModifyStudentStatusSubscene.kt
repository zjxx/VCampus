package view

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.graphics.Color

import module.StudentStatusModule
import view.component.CombinedStudentStatusCard

@Composable
fun ModifyStudentStatusSubscene() {
    val studentStatusModule = remember { StudentStatusModule() }
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf(listOf<StudentStatusModule>()) }
    val scrollState = rememberScrollState()
    Box(modifier = Modifier.verticalScroll(scrollState).padding(16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("搜索学籍信息") },
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                )
                Button(onClick = {
                    studentStatusModule.searchAdmin(searchQuery) { results ->
                        searchResults = results
                    }
                }) {
                    Text("搜索")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            searchResults.forEach { student ->
                CombinedStudentStatusCard(student) {
                    searchResults = searchResults.filter { it.studentId != student.studentId }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}