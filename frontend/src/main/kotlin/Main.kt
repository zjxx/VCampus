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
import data.UserSession

@Composable
@Preview
fun App() {
    var currentScene by remember { mutableStateOf("LoginScene") }
    var naviItems by remember { mutableStateOf(emptyList<NaviItem>()) }

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
                        naviItems = when (UserSession.role) {
                            "student" -> listOf(
                                NaviItem("主页", "/home", Icons.Default.Home, listOf("user")),
                                NaviItem("学籍", "/student_status", Icons.Default.Person, listOf("student", "affairs_staff")),
                                NaviItem("图书馆", "/library", Icons.Default.Book, listOf("library_user", "library_staff")),
                                NaviItem("超市", "/shop", Icons.Default.ShoppingCart, listOf("shop_user", "shop_staff"))
                            )
                            "admin" -> listOf(
                                NaviItem("主页", "/home", Icons.Default.Home, listOf("user")),
                                NaviItem("教务", "/student_status", Icons.Default.DateRange, listOf("student", "affairs_staff")),
                                NaviItem("图书馆", "/library", Icons.Default.Book, listOf("library_user", "library_staff")),
                                NaviItem("超市", "/shop", Icons.Default.ShoppingCart, listOf("shop_user", "shop_staff")),
                            )
                            else -> emptyList()
                        }
                    })
                    "StudentScene" -> StudentScene(onNavigate = { path ->
                        currentScene = path
                    }, role = UserSession.role ?: "")
                    "/home" -> HomeScene()
                    "/student_status" -> StudentStatusScene(onNavigate = { path ->
                        currentScene = path
                    }, role = UserSession.role ?: "")
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