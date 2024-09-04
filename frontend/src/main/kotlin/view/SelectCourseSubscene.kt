package view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun SelectCourseSubscene() {
    Column(modifier = Modifier.padding(16.dp)) {
        // Title bar
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            Text("课程号", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            Text("课程名称", modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold)
            Text("教学班个数", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            Text("课程性质", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            Text("学分", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
        }
        // 选课内容
        Text("选课内容")
    }
}