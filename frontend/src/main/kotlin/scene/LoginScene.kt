package scene

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import scene.component.pageTitle

@Composable
fun LoginScene() {
    var username by remember { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center)
    {
        Box(
            Modifier.size(1064.dp, 600.dp)
                .background(Color.White)
        ) {
            Row {
                Row(
                    modifier = Modifier.width(400.dp).height(500.dp),

                    horizontalArrangement = Arrangement.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .requiredWidth(328.dp)
                            .fillMaxHeight()
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(
                                16.dp, Alignment.CenterVertically
                            )
                        )
                        {
                            Column {
                                Image(
                                    painterResource("p1.jpg"),
                                    contentDescription = "SEU top",
                                    modifier = Modifier.width(200.dp).height(80.dp)
                                )
                            }
                            Column {
                                pageTitle("身份认证中心", "VCampus")
                            }
                            Column(
                                verticalArrangement = Arrangement.spacedBy(
                                    8.dp, Alignment.CenterVertically
                                )
                            ) {
                                OutlinedTextField(
                                    value = username,
                                    onValueChange = { username = it },
                                    label = { Text("一卡通号") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = password,
                                    onValueChange = { password = it },
                                    label = { Text("密码") },
                                    modifier = Modifier.fillMaxWidth(),
                                    visualTransformation = VisualTransformation.None,
                                    singleLine = true
                                )
                                Button(
                                    onClick = {
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color(0xFF006400),
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier.fillMaxWidth()

                                ) {
                                    Text("登录")
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Spacer(Modifier.weight(1F))
                                    TextButton(
                                        onClick = {
                                        }, colors = ButtonDefaults.textButtonColors(
                                            contentColor = Color(0xFF006400)
                                        )
                                    ) {
                                        Text("忘记密码？")
                                    }

                                }

                            }
                        }
                    }

                }
                Row{
                    Image(
                        painterResource("p2.jpg"),
                        contentDescription = "SEU Sidebar",
                        modifier=Modifier.fillMaxWidth().fillMaxHeight()
                    )
                }

                }

        }
        }
    }

