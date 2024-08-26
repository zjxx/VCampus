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
import module.LibraryModule

@Composable
fun LibraryScene() {
    var selectedOption by remember { mutableStateOf("查找书籍") }
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var searchResults by remember { mutableStateOf(listOf<String>()) }
    var checkResults by remember { mutableStateOf(listOf<String>()) }

    val libraryModule = LibraryModule(
        onSearchSuccess = { result ->
            searchResults = result.split("\n")
        },
        onCheckSuccess = { result ->
            checkResults = result.split("\n")
        }
    )

    Row(modifier = Modifier.fillMaxSize()) {
        // 侧边导航栏
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.2f)
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
                text = "查看已借阅书籍信息",
                fontSize = 18.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedOption = "查看已借阅书籍信息" }
                    .padding(vertical = 8.dp)
            )
        }

        // 主内容区域
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.8f)
                .padding(16.dp)
        ) {
            when (selectedOption) {
                "查找书籍" -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicTextField(
                            value = searchText,
                            onValueChange = { searchText = it },
                            modifier = Modifier
                                .weight(1f)
                                .padding(bottom = 8.dp),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .background(Color.DarkGray)
                                .padding(16.dp)
                                .clickable { libraryModule.libSearch(searchText.text) }
                        ) {
                            Text(text = "搜索", color = Color.White)
                        }
                    }
                    Divider(color = Color.Gray, thickness = 1.dp)
                    Column(modifier = Modifier.padding(top = 8.dp)) {
                        if (searchResults.isEmpty()) {
                            Text(text = "无信息", fontSize = 16.sp)
                        } else {
                            searchResults.forEach { result ->
                                Text(text = result, fontSize = 16.sp, modifier = Modifier.padding(vertical = 4.dp))
                            }
                        }
                    }
                }
                "查看已借阅书籍信息" -> {
                    Column(modifier = Modifier.padding(top = 8.dp)) {
                        if (checkResults.isEmpty()) {
                            Text(text = "无信息", fontSize = 16.sp)
                        } else {
                            checkResults.forEach { result ->
                                Text(text = result, fontSize = 16.sp, modifier = Modifier.padding(vertical = 4.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}