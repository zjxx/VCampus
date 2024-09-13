package view


import PaymentWebViewDialog
import WebViewDialog
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
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.Book
import data.ColorPack
import data.ColorPack.choose
import data.UserSession
import kotlinx.coroutines.delay
import module.LibraryModule
import utils.downloadPdfIfNotExists
import view.component.DialogManager
import view.component.GlobalState
import view.component.LocalPdfViewer
import java.awt.FileDialog
import java.io.File


@Composable
fun LibraryScene(onNavigate: (String) -> Unit, role: String) {
    var selectedOption by remember {
        if (role == "student"||role=="teacher") mutableStateOf("查找书籍") else mutableStateOf("管理书籍")
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
    //var idSearchResult by remember { mutableStateOf(listOf<String>()) }
    var modifyResult by remember { mutableStateOf("") }
    var articles by remember { mutableStateOf(listOf<Book>()) }
    var showDownloadDialog by remember { mutableStateOf(false) }
    var isCollapsed by remember { mutableStateOf(true) }
    var progress by remember { mutableStateOf(0f) }

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
        },
        onIdCheckSuccess = { result ->
            borrowedBooks = emptyList()
            borrowedBooks = result
        },
        onBookModifySuccess = { result ->
            modifyResult = result
        },
        onArticleSearch = { result ->
            articles = emptyList()
            articles = result
        }
    )

    LaunchedEffect(libraryModule.tempBooks) {
        tempBooks = libraryModule.tempBooks
    }

    if (currentScene == "LibraryScene") {
        Row(modifier = Modifier.fillMaxSize()) {
            // 侧边导航栏

            Row(modifier = Modifier.fillMaxSize()) {
                if (isCollapsed) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(0.034f)
                            .background(ColorPack.sideColor2[choose.value].value).drawBehind {
                                drawLine(
                                    color = ColorPack.mainColor2[choose.value].value,
                                    start = Offset(size.width, 0f),
                                    end = Offset(size.width, size.height),
                                    strokeWidth = 4.dp.toPx()
                                )
                            },
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top
                        ) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Expand",
                                modifier = Modifier.clickable { isCollapsed = false },
                                tint = ColorPack.backgroundColor1[choose.value].value,
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            if (role == "student"||role == "teacher") {
                                Icon(imageVector = Icons.Default.Search, contentDescription = "查找书籍", tint = ColorPack.backgroundColor1[choose.value].value)
                                Spacer(modifier = Modifier.height(16.dp))
                                Icon(imageVector = Icons.Default.History, contentDescription = "查看已借阅书籍信息", tint = ColorPack.backgroundColor1[choose.value].value)
                                Spacer(modifier = Modifier.height(16.dp))
                                Icon(imageVector = Icons.Default.Book, contentDescription = "学术查找", tint = ColorPack.backgroundColor1[choose.value].value)
//                                Spacer(modifier = Modifier.height(16.dp))
//                                Icon(imageVector = Icons.Default.Image, contentDescription = "显示图片", tint = ColorPack.backgroundColor1[choose.value].value)
                            } else if (role == "admin") {
                                Icon(imageVector = Icons.Default.Checklist, contentDescription = "管理书籍", tint = ColorPack.backgroundColor1[choose.value].value)
                                Spacer(modifier = Modifier.height(16.dp))
                                Icon(imageVector = Icons.Default.History, contentDescription = "查看借阅记录", tint = ColorPack.backgroundColor1[choose.value].value)
                            }
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(0.2f)
                            .background(ColorPack.sideColor2[choose.value].value)
                            .drawBehind {
                                drawLine(
                                    color = ColorPack.mainColor2[choose.value].value,
                                    start = Offset(0f, 0f),
                                    end = Offset(0f, size.height),
                                    strokeWidth = 8.dp.toPx()
                                )
                            }
                            //.shadow(4.dp, spotColor = Color.Gray, ambientColor = Color.Gray, clip = false)
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Collapse",
                                modifier = Modifier.clickable {
                                    isCollapsed = true
                                },
                                tint = ColorPack.backgroundColor1[choose.value].value,
                            )
                        }
                        Text(
                            text = "图书馆",
                            color = ColorPack.backgroundColor1[choose.value].value,
                            modifier = Modifier.padding(16.dp)
                        )
                        Divider(color = Color.Gray, thickness = 1.dp)
                        if (role == "student" || role == "teacher") {
                            TextButton(
                                onClick = { selectedOption = "查找书籍" },
                                //modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Search, contentDescription = "查找书籍", tint = ColorPack.backgroundColor1[choose.value].value)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "查找书籍", fontSize = 16.sp, color = ColorPack.backgroundColor1[choose.value].value)
                            }
                            TextButton(
                                onClick = {
                                    selectedOption = "查看已借阅书籍信息"
                                    UserSession.userId?.let { userId ->
                                        libraryModule.libCheck(userId)
                                    }
                                },
                                //modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.History, contentDescription = "查看已借阅书籍信息", tint = ColorPack.backgroundColor1[choose.value].value)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "查看已借阅书籍信息", fontSize = 16.sp, color = ColorPack.backgroundColor1[choose.value].value)
                            }
                            TextButton(
                                onClick = { selectedOption = "学术查找" },
                                //modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Book, contentDescription = "学术查找", tint = ColorPack.backgroundColor1[choose.value].value)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "学术查找", fontSize = 16.sp, color = ColorPack.backgroundColor1[choose.value].value)
                            }
//                            TextButton(
//                                onClick = { selectedOption = "显示图片" },
//                                //modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
//                            ) {
//                                Icon(imageVector = Icons.Default.Image, contentDescription = "显示图片", tint = Color.White)
//                                Spacer(modifier = Modifier.width(8.dp))
//                                Text(text = "显示图片", fontSize = 16.sp, color = Color.White)
//                            }
                        } else if (role == "admin") {
                            TextButton(
                                onClick = { selectedOption = "管理书籍" },
                                //modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Checklist, contentDescription = "管理书籍", tint = ColorPack.backgroundColor1[choose.value].value)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "管理书籍", fontSize = 16.sp, color = ColorPack.backgroundColor1[choose.value].value)
                            }
                            TextButton(
                                onClick = {
                                    selectedOption = "查看借阅记录"
                                    libraryModule.libIdCheck("")
                                },
                                //modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.History, contentDescription = "查看借阅记录", tint = ColorPack.backgroundColor1[choose.value].value)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "查看借阅记录", fontSize = 16.sp, color = ColorPack.backgroundColor1[choose.value].value)
                            }
                        }
                    }
                }


                // 主内容区域
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.8f)
                        .padding(16.dp)
                ) {
                    if (role == "student"|| role == "teacher") {//学生界面
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
                                                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Black)
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
                                            .background(ColorPack.mainColor1[choose.value].value)
                                            .clickable {
                                                libraryModule.libSearch(searchText.text, searchType)
                                            }
                                            .padding(16.dp)
                                    ) {
                                        Text(text = "搜索", color = ColorPack.backgroundColor1[choose.value].value, fontSize = 16.sp)
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Divider(color = Color.Gray, thickness = 1.dp)
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(5),
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(top = 8.dp)
                                ) {
                                    items(tempBooks.size) { index ->
                                        val book = tempBooks[index]
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier
                                                .padding(8.dp)
                                                //.clip(RoundedCornerShape(8.dp))
                                                .height(280.dp)
                                                .background(ColorPack.backgroundColor2[choose.value].value)
                                                .clickable {
                                                    GlobalState.selectedBook = book
                                                    currentScene = "BookImfoSubscene"
                                                }
                                                .padding(8.dp)
                                                .drawBehind {
                                                    drawLine(
                                                        color = ColorPack.sideColor2[choose.value].value,
                                                        start = Offset(0f, size.height),
                                                        end = Offset(size.width, size.height),
                                                        strokeWidth = 4.dp.toPx()
                                                    )
                                                },
                                        ) {
                                            AsyncImage(
                                                load = { loadImageBitmap(File(book.coverImage)) },
                                                painterFor = { remember { BitmapPainter(it) } },
                                                contentDescription = "Book Cover",
                                                modifier = Modifier
                                                    .size(120.dp)
                                                    .weight(0.9f)
                                            )
                                            Text(
                                                text = book.bookname,
                                                fontSize = 16.sp,
                                                color = ColorPack.sideColor2[choose.value].value
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
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
                                            Icon(
                                                Icons.Default.Refresh,
                                                contentDescription = "刷新",
                                                tint = ColorPack.mainColor1[choose.value].value
                                            )
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
                                            val backgroundColor =
                                                if (book.condition == "borrowing") ColorPack.backgroundColor1[choose.value].value else ColorPack.backgroundColor2[choose.value].value
                                            val textColor =
                                                if (book.condition == "borrowing") ColorPack.mainColor1[choose.value].value else ColorPack.sideColor2[choose.value].value
                                            val conditionText =
                                                if (book.condition == "borrowing") " - 状态：借阅中" else " - 状态：曾借阅"
                                            Row(
                                                modifier = Modifier
                                                    .padding(8.dp)
                                                    .background(ColorPack.backgroundColor1[choose.value].value)
                                                    .clip(RoundedCornerShape(5.dp))
                                                    .border(0.8.dp, ColorPack.mainColor1[choose.value].value)
                                                    .height(160.dp)
                                                    .drawBehind {
                                                        drawLine(
                                                            color = ColorPack.mainColor1[choose.value].value,
                                                            start = Offset(0f, 0f),
                                                            end = Offset(0f, size.height),
                                                            strokeWidth = 10.dp.toPx()
                                                        )
                                                    }
                                                    .padding(8.dp),
                                            ) {
                                                Column(
                                                    modifier = Modifier.weight(1f)
                                                ) {
                                                    Text(text = book.bookname, color = textColor, fontSize = 16.sp)
                                                    Text(text = conditionText, color = textColor, fontSize = 16.sp)
                                                    Divider(color = ColorPack.mainColor2[choose.value].value, thickness = 1.2.dp)
                                                    Text(
                                                        text = "借书时间 > ${book.borrow_date}\n还书时间 > ${book.return_date}",
                                                        fontSize = 14.sp
                                                    )
                                                }
                                                Column(
                                                    modifier = Modifier.align(Alignment.CenterVertically),
                                                    verticalArrangement = Arrangement.Center,
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    Spacer(modifier = Modifier.weight(0.36f))
                                                    Button(
                                                        onClick = {
                                                            val imageISBN = book.isbn
                                                            val filePath = "src/main/temp/" + imageISBN + ".pdf"
                                                            //到时这个filePath改成src/main/temp/书的ISBN.pdf
                                                            if (!File(filePath).exists()) {
                                                                downloadPdfIfNotExists(
                                                                    "http://47.99.141.236/img/" + imageISBN + ".pdf",
                                                                    filePath
                                                                )
                                                            }
                                                            selectedPdfPath = filePath
                                                        },
                                                        modifier = Modifier.size(100.dp, 36.dp),
                                                        colors = ButtonDefaults.buttonColors(
                                                            backgroundColor = ColorPack.mainColor1[choose.value].value
                                                        ),
                                                        enabled = book.condition != "haveBorrowed"
                                                    ) {
                                                        Text("阅读", fontSize = 14.sp, color = ColorPack.backgroundColor1[choose.value].value)
                                                    }
                                                    Spacer(modifier = Modifier.height(8.dp))
                                                    Button(
                                                        onClick = {
                                                            libraryModule.libReturnBook(
                                                                UserSession.userId ?: "", book.isbn
                                                            )
                                                        },
                                                        modifier = Modifier.size(100.dp, 36.dp),
                                                        colors = ButtonDefaults.buttonColors(
                                                            backgroundColor = ColorPack.mainColor1[choose.value].value
                                                        ),
                                                        enabled = book.condition != "haveBorrowed"
                                                    ) {
                                                        Text("还书", fontSize = 14.sp, color = ColorPack.backgroundColor1[choose.value].value)
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

                            "学术查找" -> {
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
                                            .background(ColorPack.mainColor1[choose.value].value)
                                            .clickable {
                                                articles = emptyList()
                                                libraryModule.libArticleSearch(searchText.text)//__________学术搜索相关
                                            }
                                            .padding(16.dp)
                                    ) {
                                        Text(text = "搜索", color = ColorPack.backgroundColor1[choose.value].value, fontSize = 16.sp)
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Divider(color = Color.Gray, thickness = 1.dp)
                                if (articles.isNotEmpty()) {
                                    LazyVerticalGrid(
                                        columns = GridCells.Fixed(2),
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .padding(8.dp)
                                    ) {

                                        items(articles.size) { index ->
                                            val book = articles[index]

                                            Row(
                                                modifier = Modifier
                                                    .padding(8.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(
                                                        brush = Brush.horizontalGradient(
                                                            colors = listOf(
                                                                ColorPack.backgroundColor2[choose.value].value, // 渐变开始的颜色
                                                                ColorPack.backgroundColor1[choose.value].value// 渐变结束的颜色
                                                            ),
                                                            startX = with(LocalDensity.current) { 0.dp.toPx() }, // 渐变开始的位置
                                                            endX = with(LocalDensity.current) { 260.dp.toPx() } // 渐变结束的位置
                                                        )
                                                    )
                                                    .height(160.dp)
                                                    .padding(8.dp)
                                            ) {
                                                Column(
                                                    modifier = Modifier.weight(1f)
                                                ) {
                                                    val textColor = ColorPack.sideColor2[choose.value].value
                                                    Text(text = book.bookname, color = textColor, fontSize = 18.sp)
                                                    Divider(color = ColorPack.sideColor1[choose.value].value, thickness = 2.dp)
                                                    Text(text = "Published by: ${book.publisher}", color = textColor, fontSize = 16.sp)
                                                    Text(text = book.publishDate, color = textColor, fontSize = 16.sp)
                                                }
                                                Column(
                                                    modifier = Modifier.align(Alignment.CenterVertically),
                                                    verticalArrangement = Arrangement.Center,
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    Spacer(modifier = Modifier.weight(0.4f))
                                                    Button(
                                                        onClick = {
                                                            val fileDialog = FileDialog(ComposeWindow(), "选择文件保存地址", FileDialog.SAVE)
                                                            fileDialog.isVisible = true
                                                            val directory = fileDialog.directory
                                                            val file = fileDialog.file
                                                            if (directory != null && file != null) {
                                                                val filePath = "$directory$file"
                                                                val imageISBN = book.isbn
                                                                val remoteUrl = "http://47.99.141.236/img/$imageISBN.pdf"
                                                                downloadPdfIfNotExists(remoteUrl, filePath)
                                                                showDownloadDialog = true
                                                            }
                                                        },
                                                        modifier = Modifier.size(96.dp, 40.dp),
                                                        colors = ButtonDefaults.buttonColors(
                                                            backgroundColor = ColorPack.backgroundColor1[choose.value].value
                                                        ),
                                                    ) {
                                                        Icon(imageVector = Icons.Default.Download, contentDescription = "下载阅读", tint = ColorPack.sideColor2[choose.value].value)
                                                        Text(text = "PDF", fontSize = 14.sp, color = ColorPack.sideColor2[choose.value].value)
                                                    }
                                                    if (showDownloadDialog) {
                                                        LaunchedEffect(Unit) {
                                                            for (i in 1..5) {
                                                                delay(1000)
                                                                progress = i / 5f
                                                            }
                                                            DialogManager.showDialog("下载成功")
                                                            showDownloadDialog = false
                                                        }
                                                        LinearProgressIndicator(//进度条
                                                            progress = progress,
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .padding(16.dp)
                                                        )
                                                    }

                                                }
                                            }
                                        }
                                    }
                                } else if (articles.size == 0) {

                                    var showDialog by remember { mutableStateOf(false) }
                                    var url by remember { mutableStateOf("") }

                                    Box (
                                        modifier = Modifier
                                            .fillMaxSize()
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .padding(16.dp)
                                                .align(Alignment.TopStart),
                                        ) {
                                            Text("未找到相关学术文章，推荐使用以下相关连接：", color = Color.Red, fontSize = 18.sp)
                                            Spacer(modifier = Modifier.height(8.dp))

                                            val headers = listOf("东大图书馆", "外部链接")//大标题
                                            val data = listOf(
                                                listOf("数据库导航", "校外访问"),
                                                listOf("IEEE", "ACM", "知网", "万方")
                                            )

                                            fun navigateToWebView(cellData: String) {
                                                url = when (cellData) {
                                                    "数据库导航" -> "http://www.lib.seu.edu.cn/list.php?fid=19"
                                                    "校外访问" -> "http://www.lib.seu.edu.cn/list.php?fid=122"
                                                    "IEEE" -> "https://ieeexplore.ieee.org/"
                                                    "ACM" -> "http://dl.acm.org/"
                                                    "知网" -> "https://www.cnki.net/"
                                                    "万方" -> "https://g.wanfangdata.com.cn/"
                                                    else -> ""
                                                }
                                                showDialog = true
                                            }

                                            MultiColumnTable(headers = headers, data = data) { cellData ->
                                                // Handle cell click
                                                println("Clicked on: $cellData")
                                                navigateToWebView(cellData)

                                            }
                                            if (showDialog) {
                                                WebViewDialog(url = url, onDismiss = { showDialog = false })
                                            }

//                                            MultiColumnTable(headers = headers, data = data, onCellClick = { cellData ->
//
//                                            })
                                        }
                                    }

                                }
                            }

                            "显示图片" -> {
                                Column(modifier = Modifier.padding(top = 8.dp)) {
                                    PaymentWebViewDialog(1.0) {//最低1块钱
                                        if(it == "success"){
                                            println(it)
                                        }

                                    }

                                }
                            }
                        }
                    }
//_______________________________________________________________________________________________________ ↓ for admin ↓

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
                                                .background(ColorPack.mainColor1[choose.value].value)
                                                .clickable {
                                                    libraryModule.libSearch(searchText.text, searchType)
                                                }
                                                .padding(16.dp)
                                        ) {
                                            Text(text = "搜索", color = ColorPack.backgroundColor1[choose.value].value, fontSize = 16.sp)
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Divider(color = Color.Gray, thickness = 1.dp)
                                    LazyVerticalGrid(
                                        columns = GridCells.Fixed(5),
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(top = 8.dp)
                                    ) {
                                        items(tempBooks.size) { index ->
                                            val book = tempBooks[index]
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                modifier = Modifier
                                                    .padding(8.dp)
                                                    //.clip(RoundedCornerShape(8.dp))
                                                    .height(280.dp)
                                                    .background(ColorPack.backgroundColor2[choose.value].value)
                                                    .clickable {
                                                        GlobalState.selectedBook = book
                                                        currentScene = "BookAdminSubscene"
                                                    }
                                                    .padding(8.dp)
                                                    .drawBehind {
                                                        drawLine(
                                                            color = ColorPack.sideColor2[choose.value].value,
                                                            start = Offset(0f, size.height),
                                                            end = Offset(size.width, size.height),
                                                            strokeWidth = 4.dp.toPx()
                                                        )
                                                    },
                                            ) {
                                                AsyncImage(
                                                    load = { loadImageBitmap(File(book.coverImage)) },
                                                    painterFor = { remember { BitmapPainter(it) } },
                                                    contentDescription = "Book Cover",
                                                    modifier = Modifier
                                                        .size(120.dp)
                                                        .weight(0.9f)
                                                )
                                                Text(
                                                    text = book.bookname,
                                                    fontSize = 16.sp,
                                                    color = ColorPack.sideColor2[choose.value].value
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
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
                                                .background(ColorPack.mainColor1[choose.value].value)
                                                .clickable {
                                                    currentScene = "BookModifySubscene"
                                                }
                                                .padding(10.dp)
                                        ) {
                                            Text(text = "录入图书", color = ColorPack.backgroundColor1[choose.value].value, fontSize = 16.sp)
                                        }
                                    }
                                }
                            }

                            "查看借阅记录" -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .padding(16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Button(
                                            onClick = {
                                                selectedOption = "查看借阅记录"
                                                UserSession.userId?.let { userId ->
                                                    libraryModule.libIdCheck("")
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                                            shape = CircleShape,
                                            modifier = Modifier.size(48.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.Refresh,
                                                contentDescription = "刷新",
                                                tint = ColorPack.mainColor1[choose.value].value
                                            )
                                        }
                                    }
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
                                                .background(ColorPack.mainColor1[choose.value].value)
                                                .clickable {
                                                    libraryModule.libIdCheck(searchText.text)
                                                }
                                                .padding(16.dp)
                                        ) {
                                            Text(text = "搜索", color = ColorPack.backgroundColor1[choose.value].value, fontSize = 16.sp)
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Divider(color = Color.Gray, thickness = 1.dp)
                                    LazyVerticalGrid(
                                        columns = GridCells.Fixed(2),
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .padding(8.dp)
                                    ) {
                                        items(borrowedBooks.size) { index ->
                                            val book = borrowedBooks[index]
                                            val backgroundColor =
                                                if (book.condition == "borrowing") ColorPack.backgroundColor1[choose.value].value else ColorPack.backgroundColor2[choose.value].value
                                            val textColor =
                                                if (book.condition == "borrowing") ColorPack.mainColor1[choose.value].value else ColorPack.sideColor2[choose.value].value
                                            val conditionText =
                                                if (book.condition == "borrowing") " - 状态：借阅中" else " - 状态：曾借阅"
                                            Row(
                                                modifier = Modifier
                                                    .padding(8.dp)
                                                    .background(backgroundColor)
                                                    .clip(RoundedCornerShape(5.dp))
                                                    .border(0.8.dp, ColorPack.mainColor1[choose.value].value)
                                                    .height(160.dp)
                                                    .drawBehind {
                                                        drawLine(
                                                            color = ColorPack.mainColor1[choose.value].value,
                                                            start = Offset(0f, 0f),
                                                            end = Offset(0f, size.height),
                                                            strokeWidth = 10.dp.toPx()
                                                        )
                                                    }
                                                    .padding(8.dp),
                                            ) {
                                                Column(
                                                    modifier = Modifier.weight(1f)
                                                ) {
                                                    Text(text = book.bookname, color = textColor, fontSize = 16.sp)
                                                    Text(text = conditionText, color = textColor, fontSize = 16.sp)
                                                    Text(
                                                        text = "借书时间: ${book.borrow_date}\n还书时间: ${book.return_date}",
                                                        fontSize = 12.sp
                                                    )
                                                    Text(text = "记录所属user：${book.userId}", color = textColor, fontSize = 16.sp)
                                                }


                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }


            }
        }
    }else if (currentScene == "BookImfoSubscene") {
        BookImfoSubscene(onNavigateBack = { currentScene = "LibraryScene" }, libraryModule = libraryModule)

    } else if (currentScene == "BookAdminSubscene") {
        BookAdminSubscene(onNavigateBack = { currentScene = "LibraryScene" }, libraryModule = libraryModule)

    } else if (currentScene == "BookModifySubscene") {
        BookModifySubscene(onNavigateBack = { currentScene = "LibraryScene" }, book = Book(), "lib/add/file_upload")

    }
    }
