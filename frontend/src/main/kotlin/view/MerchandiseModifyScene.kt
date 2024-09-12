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
import data.ColorPack
import data.ColorPack.choose
import data.Merchandise
import module.ShopModule
import utils.NettyClientProvider
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
fun MerchandiseModifyScene(onNavigateBack: () -> Unit, item: Merchandise, type: String) {
    var itemUuid by remember { mutableStateOf(item.itemUuid) }
    var itemName by remember { mutableStateOf(item.itemname) }
    var price by remember { mutableStateOf(item.price) }
    var imageRes by remember { mutableStateOf(item.imageRes) }
    var barcode by remember { mutableStateOf(item.barcode) }
    var stock by remember { mutableStateOf(item.stock) }
    var salesVolumn by remember { mutableStateOf(item.salesVolume) }
    var description by remember { mutableStateOf(item.description) }
    var quantity by remember { mutableStateOf(item.quantity) }

    //var currentScene by remember { mutableStateOf("BookAdminSubscene") }

    var filePath by remember { mutableStateOf<String?>("src/main/temp/$itemUuid.jpg") }
    var modifyResult by remember { mutableStateOf("") }
    val nettyClient = NettyClientProvider.nettyClient
    //var idSearchResult by remember { mutableStateOf(listOf<String>()) }

    val shopModule = ShopModule(
        onSearchSuccess = {},
        onBuySuccess = {},
        onEnterSuccess = {},
        onAddItemToCartSuccess = {},
        onRemoveItemFromCartSuccesss = {},
        onShopAddToListSuccess = {},
        onGetAllTransactionsSuccess = {},
        onGetTransactionsByCardNumberSuccess = {},
        onViewSuccess = {},
    )

    //if (currentScene == "MerchandiseModifyScene") {
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
                Text(text = "选择图片", color = ColorPack.backgroundColor1[choose.value].value, fontSize = 18.sp)
            }

            Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                // Left column for image
                Column(modifier = Modifier.width(216.dp).padding(end = 16.dp)) {
                    filePath?.let {
                        AsyncImage(
                            load = { loadImageBitmap(File(it)) },
                            painterFor = { remember { BitmapPainter(it) } },
                            contentDescription = "Item Image",
                            modifier = Modifier.size(216.dp)
                        )
                    }
                }

                // Main column with text fields and save button
                Column(modifier = Modifier.weight(1f)) {
                    // First row: Book name, Author, ISBN, Language
                    Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                        OutlinedTextField(
                            value = itemName,
                            onValueChange = { itemName = it },
                            label = { Text("名称") },
                            modifier = Modifier.weight(1f).padding(end = 8.dp)
                        )
                        OutlinedTextField(
                            value = price,
                            onValueChange = { price = it },
                            label = { Text("单价") },
                            modifier = Modifier.weight(1f).padding(end = 8.dp)
                        )
//                    OutlinedTextField(
//                        value = itemUuid,
//                        onValueChange = { itemUuid = it },
//                        label = { Text("uuid") },
//                        modifier = Modifier.weight(1f).padding(end = 8.dp)
//                    )
                        OutlinedTextField(
                            value = stock,
                            onValueChange = { stock = it },
                            label = { Text("库存") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Second row: Kind, Publisher, Publish Date
                    Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                        OutlinedTextField(
                            value = barcode,
                            onValueChange = { barcode = it },
                            label = { Text("条形码号") },
                            modifier = Modifier.weight(2f).padding(end = 8.dp)
                        )
                        OutlinedTextField(
                            value = salesVolumn,
                            onValueChange = { salesVolumn = it },
                            label = { Text("销量") },
                            modifier = Modifier.weight(1f).padding(end = 8.dp)
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
                                    "itemName" to itemName,
                                    "price" to price,
                                    "barcode" to barcode,
                                    "stock" to stock,
                                    "salesVolumn" to salesVolumn,
                                    "description" to description,
                                )
                                //删除本地src/main/temp/ISBN.png
                                val fileex = "src/main/temp/$itemUuid.png"
                                val file = File(fileex)
                                if (file.exists()) {
                                    try {
                                        file.delete()
                                        println("File deleted successfully.")
                                    } catch (e: Exception) {
                                        println("Error deleting file: ${e.message}")
                                    }
                                }
                                shopModule.shopAddToList(request, type, filePath)
                                if (modifyResult == "success") {
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