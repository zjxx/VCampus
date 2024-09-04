package view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.Book
import data.UserSession
import module.LibraryModule
import view.component.GlobalState

@Composable
fun LibraryScene(onNavigate: (String) -> Unit, role: String) {
    var selectedOption by remember { mutableStateOf("查找书籍") }
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var imageUrl by remember { mutableStateOf("") }
    var inputText by remember { mutableStateOf(TextFieldValue("")) }
    var tempBooks by remember { mutableStateOf(listOf<Book>()) }
    var borrowedBooks by remember { mutableStateOf(listOf<Book>()) }
    var currentScene by remember { mutableStateOf("LibraryScene") }

    val libraryModule = LibraryModule(
        onSearchSuccess = { result ->
            tempBooks = emptyList()
            tempBooks = result
        },
        onCheckSuccess = { result ->
            borrowedBooks = emptyList()
            borrowedBooks = result
        },
        onImageFetchSuccess = { url ->
            imageUrl = url
        }
    )

    LaunchedEffect(libraryModule.tempBooks) {
        tempBooks = libraryModule.tempBooks
    }

    if (currentScene == "LibraryScene") {
        Row(modifier = Modifier.fillMaxSize()) {
            // 侧边导航栏
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.2f)
                    .background(Color(0XFFB9E5E8))
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
                            .clickable {
                                selectedOption = "查看已借阅书籍信息"
                                UserSession.userId?.let { userId ->
                                    libraryModule.libCheck(userId)
                                }
                            }
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
                if (role == "student") {//学生界面
                    when (selectedOption) {
                        "查找书籍" -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = searchText,
                                    onValueChange = { searchText = it },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF228042))
                                        .clickable { libraryModule.libSearch(searchText.text, role) }
                                        .padding(16.dp)
                                ) {
                                    Text(text = "搜索", color = Color.White, fontSize = 16.sp)
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Divider(color = Color.Gray, thickness = 1.dp)
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(4),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(top = 8.dp)
                            ) {
                                items(tempBooks.size) { index ->
                                    val book = tempBooks[index]
                                    Column(
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color.White)
                                            .clickable {
                                                GlobalState.selectedBook = book
                                                currentScene = "BookImfoSubscene"
                                            }
                                            .padding(8.dp)
                                    ) {
                                        AsyncImage(
                                            load = { loadImageBitmap(book.coverImage) },
                                            painterFor = { remember { BitmapPainter(it) } },
                                            contentDescription = "Book Cover",
                                            modifier = Modifier.size(108.dp)
                                        )
                                        Text(text = book.bookname, fontSize = 14.sp)
                                    }
                                }
                            }
                        }

                        "查看已借阅书籍信息" -> {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(8.dp)
                            ) {
                                items(borrowedBooks.size) { index ->
                                    val book = borrowedBooks[index]
                                    val backgroundColor = if (book.condition == "borrowing") Color.White else Color.LightGray
                                    val textColor = if (book.condition == "borrowing") Color(0xFF228042) else Color.Black
                                    val conditionText = if (book.condition == "borrowing") "借阅中" else "曾借阅"
                                    Column(
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .background(backgroundColor)
                                            //.clickable { onNavigate("BookInfoSubscene") }
                                            .padding(8.dp)
                                    ) {
                                        Text(text = book.bookname, color = textColor, fontSize = 16.sp)
                                        Text(text = conditionText, color = textColor, fontSize = 18.sp)
                                        Text(text = "借书时间：${book.borrow_date}\n还书时间：${book.return_date}", fontSize = 12.sp)
                                    }
                                }
                            }
                        }

                        "显示图片" -> {
                            Column(modifier = Modifier.padding(top = 8.dp)) {
                                OutlinedTextField(
                                    value = inputText,
                                    onValueChange = { inputText = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color.Blue)
                                        .clickable { libraryModule.fetchImageUrl(inputText.text) }
                                        .padding(8.dp)
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
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color.Blue)
                                        .clickable { libraryModule.libSearch(searchText.text, role) }
                                        .padding(8.dp)
                                ) {
                                    Text(text = "搜索", color = Color.White)
                                }
                            }
                            Divider(color = Color.Gray, thickness = 1.dp)
                            Column(modifier = Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                                .padding(top = 8.dp)) {
                                if (tempBooks.isEmpty()) {
                                    Text(text = "无信息", fontSize = 16.sp)
                                } else {
                                    tempBooks.forEach { book ->
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(8.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color.LightGray)
                                                .clickable { currentScene = "BookImfoSubscene" }
                                                .padding(8.dp)
                                        ) {
                                            AsyncImage(
                                                load = { loadImageBitmap(book.coverImage) },
                                                painterFor = { remember { BitmapPainter(it) } },
                                                contentDescription = "Book Cover",
                                                modifier = Modifier.size(128.dp)
                                            )
                                            Text(text = book.bookname, fontSize = 16.sp)
                                        }
                                    }
                                }
                            }
                        }

                        "查看借阅记录" -> {
                            // Add implementation for viewing borrowing records
                        }

                        "显示图片" -> {
                            Column(modifier = Modifier.padding(top = 8.dp)) {
                                OutlinedTextField(
                                    value = inputText,
                                    onValueChange = { inputText = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color.Blue)
                                        .clickable { libraryModule.fetchImageUrl(inputText.text) }
                                        .padding(8.dp)
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
    } else if (currentScene == "BookImfoSubscene") {
        BookImfoSubscene(onNavigateBack = { currentScene = "LibraryScene" }, libraryModule = libraryModule)
    }
}