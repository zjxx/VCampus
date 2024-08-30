package view

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import module.StudentStatusModule
import view.component.CombinedStudentStatusCard

@Composable
fun ModifyStudentStatusSubscene() {
    val studentStatusModule = remember { StudentStatusModule() }
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("搜索学籍信息") },
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            )
            Button(onClick = { studentStatusModule.searchAdmin(searchQuery) }) {
                Text("搜索")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        CombinedStudentStatusCard(studentStatusModule)
    }
}