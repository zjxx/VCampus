package view


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

@Composable
fun LibraryScene() {
    var selectedOption by remember { mutableStateOf("查找书籍") }
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    Row(modifier = Modifier.fillMaxSize()) {
        // 侧边导航栏
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.3f)
                .background(Color.LightGray)
                .padding(16.dp)
        ) {
            Text(
                text = "查找书籍",
                fontSize = 18.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedOption = "查找书籍" }
                    .padding(vertical = 8.dp)
            )
            Text(
                text = "查找已借阅书籍信息",
                fontSize = 18.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedOption = "查找已借阅书籍信息" }
                    .padding(vertical = 8.dp)
            )
        }

        // 主内容区域
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.7f)
                .padding(16.dp)
        ) {
            when (selectedOption) {
                "查找书籍" -> {
                    BasicTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )
                    Divider(color = Color.Gray, thickness = 1.dp)
                    Text(
                        text = if (searchText.text.isEmpty()) "无信息" else "显示搜索结果",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                "查找已借阅书籍信息" -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            text = "还书",
                            fontSize = 18.sp,
                            modifier = Modifier
                                .clickable { /* 还书逻辑 */ }
                                .padding(16.dp)
                                .background(Color.Gray)
                                .padding(16.dp)
                        )
                        Text(
                            text = "借书",
                            fontSize = 18.sp,
                            modifier = Modifier
                                .clickable { /* 借书逻辑 */ }
                                .padding(16.dp)
                                .background(Color.Gray)
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}