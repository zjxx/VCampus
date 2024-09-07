// src/main/kotlin/view/RecordSubscene.kt
package view

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RecordSubscene() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "录课场景")
        Button(onClick = { }) {
            Text(text = "返回课程")
        }
    }
}