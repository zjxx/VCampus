package view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import view.component.pageTitle

@Composable
fun SearchStudentStatusSubscene() {
    var gender by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val genderOptions = listOf("男", "女")

    Column(modifier = Modifier.padding(16.dp)) {
        pageTitle(heading = "个人学籍信息", caption = "查找个人学籍信息")

        Box(modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(vertical = 8.dp)) {
            OutlinedTextField(
                value = gender,
                onValueChange = {},
                label = { Text("性别") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
                readOnly = true,
                trailingIcon = {
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.align(Alignment.TopStart).offset(y = 56.dp) // Adjust offset to display below
            ) {
                genderOptions.forEach { option ->
                    DropdownMenuItem(onClick = {
                        gender = option
                        expanded = false
                    }) {
                        Text(text = option)
                    }
                }
            }
        }
    }
}