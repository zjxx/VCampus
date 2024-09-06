package view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Refresh
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
import utils.downloadPdfIfNotExists
import view.component.FilePicker
import view.component.GlobalState
import view.component.LocalPdfViewer
import java.io.File


@Composable
fun LibraryScene(onNavigate: (String) -> Unit, role: String) {
    var selectedOption by remember {
        if(role == "student")mutableStateOf("查找书籍") else mutableStateOf("管理书籍")
    }
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var imageUrl by remember { mutableStateOf("") }
    //var inputText by remember { mutableStateOf(TextFieldValue("")) }
    var tempBooks by remember { mutableStateOf(listOf<Book>()) }
    var borrowedBooks by remember { mutableStateOf(listOf<Book>()) }
    var currentScene by remember { mutableStateOf("LibraryScene") }
    var searchType by remember { mutableStateOf("bookName") }
    var expanded by remember { mutableStateOf(false) }
    var selectedOption1 by remember { mutableStateOf("书名") }
    var addtolistresult by remember { mutableStateOf("") }
    var selectedPdfPath by remember { mutableStateOf<String?>(null) }


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
        },
        onAddToListSuccess = { result ->
            addtolistresult = result
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
                                    value = selectedOption1,
                                    onValueChange = { selectedOption1 = it },
                                    modifier = Modifier
                                        .weight(0.15f)
                                        .clickable { expanded = true },
                                    readOnly = true,
                                    trailingIcon = {
                                        IconButton(onClick = { expanded = true }) {
                                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                        }
                                    }
                                )

                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    DropdownMenuItem(onClick = {
                                        selectedOption1 = "书名"
                                        searchType = "bookName"
                                        expanded = false
                                    }) {
                                        Text("书名")
                                    }
                                    DropdownMenuItem(onClick = {
                                        selectedOption1 = "ISBN"
                                        searchType = "ISBN"
                                        expanded = false
                                    }) {
                                        Text("ISBN")
                                    }
                                }
                                Spacer(modifier = Modifier.width(8.dp))
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
                                        .clickable {
                                            libraryModule.libSearch(searchText.text, searchType)
                                        }
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
                                            load = { loadImageBitmap(File(book.coverImage)) },
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
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = {
                                        selectedOption = "查看已借阅书籍信息"
                                        UserSession.userId?.let { userId ->
                                            libraryModule.libCheck(userId)
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                                    shape = CircleShape,
                                    modifier = Modifier.size(48.dp)
                                ) {
                                    Icon(Icons.Default.Refresh, contentDescription = "刷新", tint = Color(0xFF228042))
                                }
                            }
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(8.dp)
                            ) {
                                items(borrowedBooks.size) { index ->
                                    val book = borrowedBooks[index]
                                    val backgroundColor = if (book.condition == "borrowing") Color.White else Color(0xFFC1EAEF)
                                    val textColor = if (book.condition == "borrowing") Color(0xFF228042) else Color.Black
                                    val conditionText = if (book.condition == "borrowing") "借阅中" else "曾借阅"
                                    Row(
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(backgroundColor)
                                            .border(1.dp, Color.LightGray)
                                            .height(160.dp)
                                            .padding(8.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(text = book.bookname, color = textColor, fontSize = 16.sp)
                                            Text(text = conditionText, color = textColor, fontSize = 16.sp)
                                            Text(text = "借书时间 > ${book.borrow_date}\n还书时间 > ${book.return_date}", fontSize = 12.sp)
                                        }
                                        Column(
                                            modifier = Modifier.align(Alignment.CenterVertically),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Button(
                                                onClick = {
                                                    val imageISBN=book.isbn
                                                    val filePath = "src/main/temp/"+imageISBN+".pdf"
                                                    //到时这个filePath改成src/main/temp/书的ISBN.pdf
                                                    if(!File(filePath).exists()){
                                                        downloadPdfIfNotExists("http://47.99.141.236/img/" + imageISBN + ".pdf", filePath)
                                                    }
                                                    selectedPdfPath = filePath
                                                },
                                                modifier = Modifier.size(100.dp, 36.dp),
                                                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF228042)),
                                                enabled = book.condition != "haveBorrowed"
                                            ) {
                                                Text("阅读", fontSize = 14.sp, color = Color.White)
                                            }
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Button(
                                                onClick = { libraryModule.libReturnBook(UserSession.userId ?: "", book.isbn) },
                                                modifier = Modifier.size(100.dp, 36.dp),
                                                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF228042)),
                                                enabled = book.condition != "haveBorrowed"
                                            ) {
                                                Text("还书", fontSize = 14.sp, color = Color.White)
                                            }
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Button(
                                                onClick = { libraryModule.libRenewBook(UserSession.userId ?: "", book.isbn) },
                                                modifier = Modifier.size(100.dp, 36.dp),
                                                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF228042))
                                            ) {
                                                Text("续借", fontSize = 14.sp, color = Color.White)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                            selectedPdfPath?.let { path ->
                                LocalPdfViewer(filePath = path, onDismiss = { selectedPdfPath = null })
                            }
                    }

                        "显示图片" -> {
                            Column(modifier = Modifier.padding(top = 8.dp)) {
                                FilePicker()
                                Button(onClick = {
                                    val imageISBN="9787550263932"
                                    val filePath = "src/main/temp/"+imageISBN+".pdf"
                                    //到时这个filePath改成src/main/temp/书的ISBN.pdf
                                    if(!File(filePath).exists()){
                                        downloadPdfIfNotExists("http://47.99.141.236/img/" + imageISBN + ".pdf", filePath)
                                    }
                                    selectedPdfPath = filePath
                                }) {
                                    Text("展示pdf")
                                }
                                selectedPdfPath?.let { path ->
                                    LocalPdfViewer(filePath = path, onDismiss = { selectedPdfPath = null })
                                    }
                            }
                        }
                    }
                }
                //________________________________________________________________________________________ ↓ for admin ↓

                else if (role == "admin") {
                    when (selectedOption) {
                        "管理书籍" -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(0.8f)
                                    .padding(16.dp)
                            ) {
                                // Existing code for search functionality
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedTextField(
                                        value = selectedOption1,
                                        onValueChange = { selectedOption1 = it },
                                        modifier = Modifier
                                            .weight(0.15f)
                                            .clickable { expanded = true },
                                        readOnly = true,
                                        trailingIcon = {
                                            IconButton(onClick = { expanded = true }) {
                                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                            }
                                        }
                                    )

                                    DropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false }
                                    ) {
                                        DropdownMenuItem(onClick = {
                                            selectedOption1 = "书名"
                                            searchType = "bookName"
                                            expanded = false
                                        }) {
                                            Text("书名")
                                        }
                                        DropdownMenuItem(onClick = {
                                            selectedOption1 = "ISBN"
                                            searchType = "ISBN"
                                            expanded = false
                                        }) {
                                            Text("ISBN")
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
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
                                            .clickable {
                                                libraryModule.libSearch(searchText.text, searchType)
                                            }
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
                                                    currentScene = "BookAdminSubscene"
                                                }
                                                .padding(8.dp)
                                        ) {
                                            AsyncImage(
                                                load = { loadImageBitmap(File(book.coverImage)) },
                                                painterFor = { remember { BitmapPainter(it) } },
                                                contentDescription = "Book Cover",
                                                modifier = Modifier.size(108.dp)
                                            )
                                            Text(text = book.bookname, fontSize = 14.sp)
                                        }
                                    }
                                }
                                // New Row for buttons
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFF228042))
                                            .clickable {
                                                // Add action for "录入图书"

                                            }
                                            .padding(10.dp)
                                    ) {
                                        Text(text = "录入图书", color = Color.White, fontSize = 16.sp)
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFF228042))
                                            .clickable {
                                                // Add action for "批量录入"

                                            }
                                            .padding(10.dp)
                                    ) {
                                        Text(text = "批量录入", color = Color.White, fontSize = 16.sp)
                                    }
                                }
                            }
                        }

                        "查看借阅记录" -> {
                            // Add implementation for viewing borrowing records
                        }
                    }
                }
            }
        }
    } else if (currentScene == "BookImfoSubscene") {
        BookImfoSubscene(onNavigateBack = { currentScene = "LibraryScene" }, libraryModule = libraryModule)

    } else if (currentScene == "BookAdminSubscene") {
        BookAdminSubscene(onNavigateBack = { currentScene = "LibraryScene" }, libraryModule = libraryModule)
    }
}