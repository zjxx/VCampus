// src/main/kotlin/Main.kt
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.rememberWindowState
import view.LoginScene
import view.StudentScene
import view.HomeScene
import view.StudentStatusScene
import view.LibraryScene // 导入 LibraryScene
import view.component.TopNavBar
import view.component.GlobalDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import data.NaviItem

@Composable
@Preview
fun App() {
    var currentScene by remember { mutableStateOf("LoginScene") }
    var naviItems by remember { mutableStateOf(emptyList<NaviItem>()) }
    var role by remember { mutableStateOf("") }

    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            if (currentScene != "LoginScene") {
                TopNavBar(naviItems = naviItems, onItemSelected = { item ->
                    currentScene = item.path
                })
            }
            Box(modifier = Modifier.fillMaxSize()) {
                when (currentScene) {
                    "LoginScene" -> LoginScene(onLoginSuccess = {
                        currentScene = "StudentScene"
                        role = "student" // 假设登录成功后返回的角色
                        naviItems = listOf(
                            NaviItem("主页", "/home", Icons.Default.Home, listOf("user")),
                            NaviItem("学籍", "/student_status", Icons.Default.Person, listOf("student", "affairs_staff")),
                            NaviItem("图书馆", "/library", Icons.Default.Book, listOf("library_user", "library_staff"))
                        )
                    })
                    "StudentScene" -> StudentScene(onNavigate = { path ->
                        currentScene = path
                    }, role = role)
                    "/home" -> HomeScene()
                    "/student_status" -> StudentStatusScene(onNavigate = { path ->
                        currentScene = path
                    },role = role)
                    "/library" -> LibraryScene() // 添加图书馆场景
                    // 添加更多场景
                }
            }
            GlobalDialog() // Add GlobalDialog here
        }
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(size = DpSize(1064.dp, 600.dp)) // 设置窗口初始大小
    ) {
        App()

    }
}