// src/main/kotlin/view/HomeScene.kt
package view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import data.UserSession
import module.LoginModule
import view.component.EmailDialog
import view.component.DialogManager
import java.util.*
import androidx.compose.animation.core.*
import androidx.compose.ui.unit.IntOffset

@Composable
fun HomeScene(onLogout: () -> Unit) {
    var showEmailDialog by remember { mutableStateOf(UserSession.status == "noemail") }
    val loginModule = LoginModule(onLoginSuccess = {}, onLogout = onLogout)
    val scrollState = rememberScrollState()
    if (showEmailDialog) {
        EmailDialog(
            onDismiss = { showEmailDialog = false },
            onConfirm = { email ->
                loginModule.updateEmail(UserSession.userId ?: "", email, {
                    UserSession.status = "success"
                    showEmailDialog = false
                }, { errorMessage ->
                    DialogManager.showDialog(errorMessage)
                })
            }
        )
    }

    val greeting = getGreeting()
    val userId = UserSession.userId ?: "Unknown ID"
    val userName = UserSession.userName ?: "Unknown User"
    val role = UserSession.role ?: "Unknown Role"
    val roleTitle = when (role) {
        "student" -> "同学"
        "teacher" -> "老师"
        "admin" -> "管理员"
        else -> "用户"
    }

    val courses = remember { mutableStateOf(UserSession.courses) }

    val infiniteTransition = rememberInfiniteTransition()
    val offsetX by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f, // Adjust this value based on your text length and screen width
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(modifier = Modifier.fillMaxSize().verticalScroll(scrollState).padding(16.dp)) {
        Column {
            Column(modifier = Modifier.width(200.dp).height(80.dp)) {
                Image(
                    painter = painterResource("p1.jpg"),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(100.dp)
                )
            }
            Column(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.offset { IntOffset(offsetX.toInt(), 0) }) {
                    Text("$greeting, $userName$roleTitle")
                }
                Spacer(modifier = Modifier.height(16.dp))
                // 添加更多内容
                if (role == "student") {
                    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                        Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("我的课表")
                                }
                                Divider(
                                    modifier = Modifier
                                        .padding(vertical = 8.dp)
                                        .height(2.dp),
                                    thickness = 2.dp
                                )
                                courses.value.forEach { course ->
                                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                                        Column(modifier = Modifier.padding(8.dp)) {
                                            Text(" ${course.name}", fontWeight = FontWeight.Bold, color = Color.Black)
                                            Text(" ${course.time}节    教室： ${course.classroom}", color = Color.Gray, fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                        }
                        Divider(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .height(2.dp),
                            thickness = 2.dp
                        )
                        Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("跑操次数: 10", fontWeight = FontWeight.Bold, color = Color.Black)
                                Text("srtp学分: 5", fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

fun getGreeting(): String {
    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when (currentHour) {
        in 0..5 -> "晚上好"
        in 6..11 -> "早上好"
        in 12..13 -> "中午好"
        in 14..17 -> "下午好"
        else -> "晚上好"
    }
}