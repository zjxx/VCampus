// LibraryScene.kt
package view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.graphics.painter.BitmapPainter
import module.LibraryModule
import java.io.File
import java.net.URL

@Composable
fun LibraryScene(onNavigate: (String) -> Unit, role: String) {
    var selectedOption by remember { mutableStateOf("查找书籍") }
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var searchResults by remember { mutableStateOf(listOf<String>()) }
    var checkResults by remember { mutableStateOf(listOf<String>()) }
    var imageUrl by remember { mutableStateOf("") }
    var inputText by remember { mutableStateOf(TextFieldValue("")) }

    val libraryModule = LibraryModule(
        onSearchSuccess = { result ->
            searchResults = result.split("\n")
        },
        onCheckSuccess = { result ->
            checkResults = result.split("\n")
        },
        onImageFetchSuccess = { url ->
            imageUrl = url
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
            if(role == "student") {
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
                Text(
                    text = "显示图片",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedOption = "显示图片" }
                        .padding(vertical = 8.dp)
                )
            }
            else if(role == "admin") {
                Text(
                    text = "管理书籍",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedOption = "管理书籍" }
                        .padding(vertical = 8.dp)
                )
                Text(
                    text = "查看借阅记录",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedOption = "查看借阅记录" }
                        .padding(vertical = 8.dp)
                )
            }
        }

        // 主内容区域
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.8f)
                .padding(16.dp)
        ) {
            if (role == "student") {
                when (selectedOption) {
                    "查找书籍" -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = searchText,
                                onValueChange = { searchText = it },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp)
                                    .background(Color.White)
                                    .padding(16.dp),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
                                    .background(Color(0xFF228042))
                                    .padding(16.dp)
                                    .clickable { libraryModule.libSearch(searchText.text, role) }
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
                    "显示图片" -> {
                        Column(modifier = Modifier.padding(top = 8.dp)) {
                            BasicTextField(
                                value = inputText,
                                onValueChange = { inputText = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .background(Color.DarkGray)
                                    .padding(16.dp)
                                    .clickable {
                                        imageUrl = ""
                                        libraryModule.fetchImageUrl(inputText.text)
                                    }
                            ) {
                                Text(text = "获取图片", color = Color.White)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            if (imageUrl.isNotEmpty()) {
                                AsyncImage(
                                    load = { loadImageBitmap(imageUrl) },
                                    painterFor = { remember { BitmapPainter(it) } },
                                    contentDescription = "Fetched Image",
                                    modifier = Modifier.width(200.dp)
                                )
                            }
                        }
                    }

                }
            } else if (role == "admin") {
                when (selectedOption) {
                    "管理书籍" -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = searchText,
                                onValueChange = { searchText = it },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp)
                                    .background(Color.White)
                                    .padding(16.dp),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
                                    .background(Color(0xFF228042))
                                    .padding(16.dp)
                                    .clickable { libraryModule.libSearch(searchText.text, role) }
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

                    "查看借阅记录" -> {
                        // Add implementation for viewing borrowing records
                    }

                    "显示图片" -> {
                        Column(modifier = Modifier.padding(top = 8.dp)) {
                            BasicTextField(
                                value = inputText,
                                onValueChange = { inputText = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .background(Color.DarkGray)
                                    .padding(16.dp)
                                    .clickable {
                                        imageUrl = ""
                                        libraryModule.fetchImageUrl(inputText.text)
                                    }
                            ) {
                                Text(text = "获取图片", color = Color.White)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            if (imageUrl.isNotEmpty()) {
                                AsyncImage(
                                    load = { loadImageBitmap(imageUrl) },
                                    painterFor = { remember { BitmapPainter(it) } },
                                    contentDescription = "Fetched Image",
                                    modifier = Modifier.width(200.dp)
                                )
                            }
                        }
                    }
            }
        }
            }
    }
}