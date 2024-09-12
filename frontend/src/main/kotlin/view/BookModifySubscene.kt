package view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.Book
import data.ColorPack
import data.ColorPack.choose
import module.LibraryModule
import utils.NettyClientProvider
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
fun BookModifySubscene(onNavigateBack: () -> Unit, book: Book, type: String) {
    var bookName by remember { mutableStateOf(book.bookname) }
    var author by remember { mutableStateOf(book.author) }
    var isbn by remember { mutableStateOf(book.isbn) }
    var language by remember { mutableStateOf(book.language) }
    var kind by remember { mutableStateOf(book.kind) }
    var publisher by remember { mutableStateOf(book.publisher) }
    var publishDate by remember { mutableStateOf(book.publishDate) }
    var description by remember { mutableStateOf(book.description) }
    var quantity by remember { mutableStateOf(book.quantity.toString()) }
    var valid by remember { mutableStateOf(book.valid.toString()) }
    var filePath by remember { mutableStateOf<String?>("src/main/temp/$isbn.png") }
    var modifyResult by remember { mutableStateOf("") }
    val nettyClient = NettyClientProvider.nettyClient
    var idSearchResult by remember { mutableStateOf(listOf<String>()) }

    val libraryModule = LibraryModule(
        onSearchSuccess = {},
        onCheckSuccess = {},
        onImageFetchSuccess = {},
        onAddToListSuccess = {},
        onIdCheckSuccess = {},
        onBookModifySuccess = {result ->
            modifyResult = result
        },
        onArticleSearch = {}
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Back button
        Text(
            text = "<",
            fontSize = 32.sp,
            modifier = Modifier
                .clickable { onNavigateBack() }
                .padding(8.dp)
        )

        // Image selection button
        Button(
            onClick = {
                filePath = null // Clear the displayed image
                //FileDialog限制只能png

                val fileDialog = FileDialog(Frame(), "Select Image", FileDialog.LOAD)
                fileDialog.isVisible = true
                val selectedFile = fileDialog.file
                if (selectedFile != null) {
                    filePath = "${fileDialog.directory}$selectedFile"
                }
//                val fileChooser = JFileChooser().apply {
//                    fileFilter = FileNameExtensionFilter("Image Files", "png")
//                    isAcceptAllFileFilterUsed = false
//                }
//                val result = fileChooser.showOpenDialog(null)
//                if (result == JFileChooser.APPROVE_OPTION) {
//                    filePath = fileChooser.selectedFile.absolutePath
//                }
            },
            modifier = Modifier.size(168.dp, 64.dp).padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = ColorPack.mainColor1[choose.value].value)
        ) {
            Text(text = "选择封面", color = ColorPack.backgroundColor1[choose.value].value, fontSize = 18.sp)
        }

        Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            // Left column for image
            Column(modifier = Modifier.width(216.dp).padding(end = 16.dp)) {
                filePath?.let {
                    AsyncImage(
                        load = { loadImageBitmap(File(it)) },
                        painterFor = { remember { BitmapPainter(it) } },
                        contentDescription = "Book Cover",
                        modifier = Modifier.size(216.dp)
                    )
                }
            }

            // Main column with text fields and save button
            Column(modifier = Modifier.weight(1f)) {
                // First row: Book name, Author, ISBN, Language
                Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    OutlinedTextField(
                        value = bookName,
                        onValueChange = { bookName = it },
                        label = { Text("书名") },
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    )
                    OutlinedTextField(
                        value = author,
                        onValueChange = { author = it },
                        label = { Text("作者") },
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    )
                    OutlinedTextField(
                        value = isbn,
                        onValueChange = { isbn = it },
                        label = { Text("ISBN") },
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    )
                    OutlinedTextField(
                        value = language,
                        onValueChange = { language = it },
                        label = { Text("语言") },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Second row: Kind, Publisher, Publish Date
                Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    OutlinedTextField(
                        value = kind,
                        onValueChange = { kind = it },
                        label = { Text("类型") },
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    )
                    OutlinedTextField(
                        value = publisher,
                        onValueChange = { publisher = it },
                        label = { Text("出版社") },
                        modifier = Modifier.weight(2f).padding(end = 8.dp)
                    )
                    OutlinedTextField(
                        value = publishDate,
                        onValueChange = { publishDate = it },
                        label = { Text("出版日期") },
                        modifier = Modifier.weight(0.8f).padding(end = 8.dp)
                    )
                    OutlinedTextField(
                        value = quantity,
                        onValueChange = { quantity = it },
                        label = { Text("书籍总数") },
                        modifier = Modifier.weight(0.5f).padding(end = 8.dp)
                    )
                    OutlinedTextField(
                        value = valid,
                        onValueChange = { valid = it },
                        label = { Text("剩余可借") },
                        modifier = Modifier.weight(0.5f)
                    )
                }

                // Third row: Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("描述") },
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                )

                // Save button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Button(
                        onClick = {
                            // Send the book data to the server
                            val request = mapOf(
                                "bookName" to bookName,
                                "author" to author,
                                "publisher" to publisher,
                                "publishDate" to publishDate,
                                "language" to language,
                                "ISBN" to isbn,
                                "description" to description,
                                "kind" to kind,
                                "quantity" to quantity,
                                "Valid_Quantity" to valid)
                            //删除本地src/main/temp/ISBN.png

                            libraryModule.bookModify(request, type, filePath)
                            if (modifyResult == "success") {
                                val fileex = "src/main/temp/$isbn.png"
                                val file = File(fileex)
                                if (file.exists()) {
                                    try {
                                        file.delete()
                                        println("File deleted successfully.")
                                    } catch (e: Exception) {
                                        println("Error deleting file: ${e.message}")
                                    }
                                }
                                onNavigateBack()
                            }
                        },
                        modifier = Modifier.size(168.dp, 48.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = ColorPack.mainColor1[choose.value].value)
                    ) {
                        Text(text = "保存", color = ColorPack.backgroundColor1[choose.value].value, fontSize = 18.sp)
                    }
                }
            }
        }
    }
}

//{\"bookName\":\"献给阿尔吉侬的花束\",\"author\":\"丹尼尔·凯斯\",\"publisher\":\"广西师范大学出版社\",\"publishDate\":\"2015\",\"language\":\"中文\",\"ISBN\":\"9787549565115\",\"description\":\"声称能改造智能的科学实验在白老鼠阿尔吉侬身上获得了突破性的进展，下一步急需进行人体实验。个性和善、学习态度积极的心智障碍者查理·高登成为最佳人选。手术成功后，查理的智商从68跃升为185，然而那些从未有过的情绪和记忆也逐渐浮现。\",\"Kind\":\"科幻\",\"quantity\":5,\"Valid_Quantity\":6}"