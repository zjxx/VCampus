// 修改 StudentScene.kt
package view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import NaviItem
import view.component.TopNavBar

@Composable
fun StudentScene(onNavigate: (String) -> Unit) {
    val naviItems = listOf(
        NaviItem("主页", "/home", Icons.Default.Home, listOf("user")),
        NaviItem("学籍", "/student_status", Icons.Default.Person, listOf("student", "affairs_staff")),
        // 添加更多导航项
    )

    Column(modifier = Modifier.fillMaxSize()) {
        TopNavBar(naviItems = naviItems, onItemSelected = { item -> onNavigate(item.path) })
        Box(modifier = Modifier.fillMaxSize()) {
            Text("Welcome to the Student Scene")
        }
    }
}