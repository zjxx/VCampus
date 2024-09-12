// src/main/kotlin/Main.kt
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import data.ColorPack
import data.ColorPack.choose
import data.NaviItem
import data.UserSession
import dev.datlag.kcef.KCEF
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import view.*
import view.component.GlobalDialog
import view.component.TopNavBar
import java.io.File
import kotlin.math.max

fun generateWatermarkTexts(text: String, count: Int): List<String> {
    return List(count) { text }
}

@Composable
fun Watermark(text: String) {
    val watermarkTexts = generateWatermarkTexts(text, 10) // Generate 10 watermark texts
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.run { spacedBy(50.dp) } // Add spacing between rows
        ) {
            for (i in 0 until 2) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp) // Add spacing between texts
                ) {
                    for (j in 0 until 4) {
                        Text(
                            text = watermarkTexts[i * 4 + j],
                            color = Color.Gray.copy(alpha = 0.1f),
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.rotate(45f)
                        )
                    }
                }
            }
        }
    }
}



@Composable
@Preview
fun App() {
    var currentScene by remember { mutableStateOf("LoginScene") }
    var naviItems by remember { mutableStateOf(emptyList<NaviItem>()) }
    var restartRequired by remember { mutableStateOf(false) }
    var downloading by remember { mutableStateOf(0F) }
    var initialized by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            KCEF.init(builder = {
                installDir(File("kcef-bundle"))
                progress {
                    onDownloading {
                        downloading = max(it, 0F)
                    }
                    onInitialized {
                        initialized = true
                    }
                }
                settings {
                    cachePath = File("cache").absolutePath
                }
            }, onError = {
                if (it != null) {
                    it.printStackTrace()
                }
            }, onRestartRequired = {
                restartRequired = true
            })
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            KCEF.disposeBlocking()
        }
    }
    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                if (currentScene != "LoginScene") {
                    TopNavBar(naviItems = naviItems, onItemSelected = { item ->
                        currentScene = item.path
                    })
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    ColorPack.sideColor1[choose.value].value, // 渐变开始的颜色
                                    ColorPack.backgroundColor1[choose.value].value// 渐变结束的颜色
                                ),
                                startY = with(LocalDensity.current) { 0.dp.toPx() }, // 渐变开始的位置
                                endY = with(LocalDensity.current) { 14.dp.toPx() } // 渐变结束的位置
                            )
                        )
                ) {
                    when (currentScene) {
                        "LoginScene" -> LoginScene(onLoginSuccess = {
                            currentScene = "/home"
                            naviItems = when (UserSession.role) {
                                "student" -> listOf(
                                    NaviItem("主页", "/home", Icons.Default.Home, listOf("user")),
                                    NaviItem("学籍", "/student_status", Icons.Default.Person, listOf("student", "affairs_staff")),
                                    NaviItem("图书馆", "/library", Icons.Default.Book, listOf("library_user", "library_staff")),
                                    NaviItem("超市", "/shop", Icons.Default.ShoppingCart, listOf("shop_user", "shop_staff")),
                                    NaviItem("课程", "/course", Icons.Default.DateRange, listOf("student", "affairs_staff")),
                                    NaviItem("办事平台", "/affairs", Icons.Default.Build, listOf("affairs_staff")),
                                    NaviItem("退出", "/logout", Icons.Default.ExitToApp, listOf("user"))
                                )
                                "admin" -> listOf(
                                    NaviItem("主页", "/home", Icons.Default.Home, listOf("user")),
                                    NaviItem("教务", "/student_status", Icons.Default.DateRange, listOf("student", "affairs_staff")),
                                    NaviItem("图书馆", "/library", Icons.Default.Book, listOf("library_user", "library_staff")),
                                    NaviItem("超市", "/shop", Icons.Default.ShoppingCart, listOf("shop_user", "shop_staff")),
                                    NaviItem("课程管理", "/course", Icons.Default.DateRange, listOf("student", "affairs_staff")),
                                    NaviItem("办事平台", "/affairs", Icons.Default.Build, listOf("affairs_staff")),
                                    NaviItem("退出", "/logout", Icons.Default.ExitToApp, listOf("user"))
                                    )
                                "teacher" -> listOf(
                                    NaviItem("主页", "/home", Icons.Default.Home, listOf("user")),
                                    NaviItem("图书馆", "/library", Icons.Default.Book, listOf("library_user", "library_staff")),
                                    NaviItem("超市", "/shop", Icons.Default.ShoppingCart, listOf("shop_user", "shop_staff")),
                                    NaviItem("课程", "/course", Icons.Default.DateRange, listOf("student", "affairs_staff")),
                                    NaviItem("办事平台", "/affairs", Icons.Default.Build, listOf("affairs_staff")),
                                    NaviItem("退出", "/logout", Icons.Default.ExitToApp, listOf("user"))
                                )

                                else -> emptyList()
                            }
                        })
                        "StudentScene" -> StudentScene(onNavigate = { path ->
                            currentScene = path
                        }, role = UserSession.role ?: "")
                        "/home" -> HomeScene(onLogout = {
                            currentScene = "LoginScene"
                        })
                        "/student_status" -> StudentStatusScene(onNavigate = { path ->
                            currentScene = path
                        }, role = UserSession.role ?: "")
                        "/library" -> LibraryScene(onNavigate = { path ->
                            currentScene = path
                        }, role = UserSession.role ?: "") // 添加图书馆场景
                        "/shop" -> ShopScene(onNavigate = { path ->
                            currentScene = path
                        }, role = UserSession.role ?: "")// 添加超市场景
                        "/course" -> CourseScene(onNavigate = { path ->
                            currentScene = path
                        }, role = UserSession.role ?: "")
                        "/affairs" -> AffairsScene(onNavigate = { path ->
                            currentScene = path
                        }, role = UserSession.role ?: "")
                        "/logout" -> {
                            currentScene = "LoginScene"
                        }
                    // 添加更多场景
                    }
                }
                GlobalDialog() // Add GlobalDialog here
            }
            UserSession.userId?.let {Watermark(text = it)}


        }
    }
}


fun main() = application {
    Window(
        title = "VCampus",
        icon = painterResource("p3.svg"),
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(size = DpSize(1200.dp, 700.dp)) // 设置窗口初始大小
    ) {
        App()

    }
}