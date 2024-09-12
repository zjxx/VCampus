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
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.ColorPack
import data.ColorPack.choose
import data.Merchandise
import data.StoreTransaction
import data.UserSession
import module.ShopModule
import view.component.CartMethodDialog
import view.component.GlobalState
import view.component.selectedItemList
import java.io.File


@Composable
fun ShopScene(onNavigate: (String) -> Unit, role: String) {
    var selectedOption by remember {
        if (role == "student") mutableStateOf("购物") else mutableStateOf("管理商品")
    }
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var imageUrl by remember { mutableStateOf("") }
    //var inputText by remember { mutableStateOf(TextFieldValue("")) }
    var tempItems by remember { mutableStateOf(listOf<Merchandise>()) }
    var tempTransactions by remember { mutableStateOf(listOf<StoreTransaction>()) }
    var currentScene by remember { mutableStateOf("ShopScene") }
    var isCollapsed by remember { mutableStateOf(true) }
    var showDialog by remember { mutableStateOf(false) }

    val shopModule = ShopModule(
        onSearchSuccess = { result ->
            tempItems = emptyList()
            tempItems = result
        },
        onBuySuccess = {},
        onEnterSuccess = { result ->
            tempItems = emptyList()
            tempItems = result
        },
        onAddItemToCartSuccess = {},
        onRemoveItemFromCartSuccesss = {},
        onShopAddToListSuccess = {},
        onGetAllTransactionsSuccess = { result ->
            tempTransactions = emptyList()
            tempTransactions = result
        },
        onGetTransactionsByCardNumberSuccess = { result ->
            tempTransactions = emptyList()
            tempTransactions = result
        },
        onViewSuccess = { result ->
            tempItems = emptyList()
            tempItems = result
        },
        //onViewCartComplete = {},
    )

    LaunchedEffect(shopModule.tempItems) {
        tempItems = shopModule.tempItems
    }

    if (currentScene == "ShopScene") {
        tempItems = selectedItemList
        Row(modifier = Modifier.fillMaxSize()) {
            // 侧边导航栏


            Row(modifier = Modifier.fillMaxSize()) {
                if (isCollapsed) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(0.034f)
                            .background(Color(0xff373836))
                            .drawBehind {
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
                                tint = Color.White,
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            if (role == "student" || role == "teacher") {
                                Icon(imageVector = Icons.Default.ShoppingBasket, contentDescription = "购物", tint = Color.White)
                                Spacer(modifier = Modifier.height(16.dp))
                                Icon(imageVector = Icons.Default.ShoppingCartCheckout, contentDescription = "查看购物车", tint = Color.White)
                                Spacer(modifier = Modifier.height(16.dp))
                                Icon(imageVector = Icons.Default.History, contentDescription = "消费记录", tint = Color.White)
                            } else if (role == "admin") {
                                Icon(imageVector = Icons.Default.Checklist, contentDescription = "管理商品", tint = Color.White)
                                Spacer(modifier = Modifier.height(16.dp))
                                Icon(imageVector = Icons.Default.History, contentDescription = "交易记录", tint = Color.White)
                            }
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(0.2f)
                            .background(Color(0xff373836))
                            .drawBehind {
                                drawLine(
                                    color = ColorPack.mainColor2[choose.value].value,
                                    start = Offset(size.width, 0f),
                                    end = Offset(size.width, size.height),
                                    strokeWidth = 4.dp.toPx()
                                )
                            },
                            //.shadow(4.dp, spotColor = Color.Gray, ambientColor = Color.Gray, clip = false)
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Collapse",
                                modifier = Modifier.clickable { isCollapsed = true },
                                tint = Color.White,
                            )
                        }
                        Text(
                            text = "校园超市",
                            color = Color.White,
                            modifier = Modifier.padding(16.dp)
                        )
                        Divider(color = Color.Gray, thickness = 1.dp)
                        if (role == "student" || role == "teacher") {
                            TextButton(
                                onClick = {
                                    selectedOption = "购物"
                                    //shopModule.enterShop()
                                },
                                //modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.ShoppingBasket, contentDescription = "购物", tint = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "购物", fontSize = 16.sp, color = Color.White)
                            }
                            TextButton(
                                onClick = {
                                    selectedOption = "查看购物车"
                                    tempItems = emptyList()
                                    shopModule.viewCart {}
                                },
                                //modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.ShoppingCartCheckout, contentDescription = "查看购物车", tint = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "查看购物车", fontSize = 16.sp, color = Color.White)
                            }
                            TextButton(
                                onClick = {
                                    selectedOption = "消费记录"
                                    UserSession.userId?.let { userId ->
                                        shopModule.getTransactionsByCardNumber(userId)}
                                },
                                //modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.History, contentDescription = "消费记录", tint = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "消费记录", fontSize = 16.sp, color = Color.White)
                            }
                        } else if (role == "admin") {
                            TextButton(
                                onClick = { selectedOption = "管理商品" },
                                //modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Checklist, contentDescription = "管理商品", tint = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "管理商品", fontSize = 16.sp, color = Color.White)
                            }
                            TextButton(
                                onClick = {
                                    selectedOption = "交易记录"
                                    shopModule.getAllTransactions()
                                },
                                //modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.History, contentDescription = "消费记录", tint = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "交易记录", fontSize = 16.sp, color = Color.White)
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
                    if (role == "student") {//学生界面
                        when (selectedOption) {
                            "购物" -> {
                                Text(
                                    text = "网上校园超市",
                                    fontSize = 24.sp,
                                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                )
                                Spacer(modifier = Modifier.height(12.dp))
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
                                            .clickable {
                                                tempItems = emptyList()
                                                shopModule.shopSearch(searchText.text)
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
                                    items(tempItems.size) { index ->
                                        val item = tempItems[index]
                                        Row(
                                            modifier = Modifier
                                                .padding(6.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color(0xFFFDFBEC))
                                                .clickable {
                                                    GlobalState.selectedItem = item
                                                    currentScene = "MerchandiseInfoScene"
                                                }
                                                .padding(6.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(108.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(Color(0xFFFAF6DB))
                                            ) {
                                                AsyncImage(
                                                    load = { loadImageBitmap(File(item.imageRes)) },
                                                    painterFor = { remember { BitmapPainter(it) } },
                                                    contentDescription = "Item Image",
                                                    modifier = Modifier
                                                        .size(108.dp)
                                                        .fillMaxSize(),
                                                    contentScale = ContentScale.Crop
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Divider(
                                                color = Color.DarkGray,
                                                modifier = Modifier
                                                    .fillMaxHeight()
                                                    .width(2.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Column(
                                                modifier = Modifier.height(108.dp),
                                                verticalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = item.itemname,
                                                    fontSize = 20.sp,
                                                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                                                )
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Row(
                                                        horizontalArrangement = Arrangement.Start
                                                    ) {
                                                        Text(
                                                            text = "￥",
                                                            fontSize = 14.sp,
                                                            color = Color(0xFFD33C05)
                                                        )
                                                        Text(
                                                            text = item.price,
                                                            fontSize = 18.sp,
                                                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                                            color = Color(0xFFD33C05)
                                                        )
                                                    }
                                                    Text(
                                                        text = item.salesVolume + "人付款",
                                                        fontSize = 14.sp,
                                                        color = Color.DarkGray
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            "查看购物车" -> {
                                Column (
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Button(
                                            onClick = {
                                                selectedOption = "查看购物车"
                                                tempItems = emptyList()
                                                shopModule.viewCart {}
                                            },
                                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                                            shape = CircleShape,
                                            modifier = Modifier.size(48.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.Refresh,
                                                contentDescription = "刷新",
                                                tint = Color(0xFF228042)
                                            )
                                        }
                                    }
                                    LazyVerticalGrid(
                                        columns = GridCells.Fixed(2),
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(8.dp)
                                    ) {
                                        items(tempItems.size) { index ->
                                            val item = tempItems[index]
                                            //totalPrice = totalPrice + (item.price.toDouble() * item.quantity.toDouble())

                                            Row(
                                                modifier = Modifier
                                                    .padding(8.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(Color(0xFFFDFBEC))
                                                    .border(1.dp, Color.LightGray)
                                                    .height(120.dp)
                                                    .padding(8.dp)
                                            ) {
                                                Column(
                                                    modifier = Modifier.weight(1f)
                                                ) {
                                                    Text(text = item.itemname, color = Color.Black, fontSize = 16.sp)
                                                    //Text(text = conditionText, color = textColor, fontSize = 16.sp)
                                                    Text(
                                                        text = "价格: ￥${item.price}    库存: ${item.stock}\n数量：${item.quantity}",
                                                        fontSize = 12.sp
                                                    )
                                                }
                                                Column(
                                                    modifier = Modifier
                                                        .fillMaxHeight()
                                                        .weight(0.5f)
                                                        .padding(16.dp),
                                                        //.align(Alignment.CenterVertically),
                                                    verticalArrangement = Arrangement.Bottom,
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {

                                                    Button(
                                                        onClick = {
                                                            shopModule.removeItemFromCart(
                                                                item.itemUuid,
                                                                item.quantity,
                                                            )
                                                        },
                                                        modifier = Modifier.size(100.dp, 36.dp),
                                                        colors = ButtonDefaults.buttonColors(
                                                            backgroundColor = Color(0xFF228042)
                                                        ),
                                                    ) {
                                                        Text(
                                                            "移除",
                                                            fontSize = 14.sp,
                                                            color = Color.White,
                                                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        horizontalArrangement = Arrangement.End,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Button(
                                            onClick = {
                                                showDialog = true
                                            },
                                            modifier = Modifier
                                                .size(136.dp, 48.dp)
                                                .clip(
                                                    RoundedCornerShape(
                                                        topStart = 18.dp,
                                                        bottomStart = 18.dp,
                                                        topEnd = 18.dp,
                                                        bottomEnd = 18.dp
                                                    )
                                                )
                                                .padding(start = 0.dp), // Remove padding between buttons
                                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFE5A00))
                                        ) {
                                            Text(
                                                text = "结算",
                                                color = Color.White,
                                                fontSize = 18.sp,
                                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                                            )
                                        }
                                        if (showDialog) {
                                            println("tempItems size = ${tempItems.size}")
                                            CartMethodDialog(
                                                onDismissRequest = { showDialog = false },
                                                onCampusCardPay = {
                                                    // Handle campus card payment
                                                    showDialog = false
                                                },
                                                onQRCodePay = {
                                                    // Handle QR code payment
                                                    showDialog = false
                                                },
                                                shopModule = shopModule,
                                                //items = tempItems,
                                            )
                                        }
                                    }
                                }
                            }

                            "消费记录" -> {
                                Column {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Button(
                                            onClick = {
                                                selectedOption = "消费记录"
                                                UserSession.userId?.let { userId ->
                                                    shopModule.getTransactionsByCardNumber(userId)
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                                            shape = CircleShape,
                                            modifier = Modifier.size(48.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.Refresh,
                                                contentDescription = "刷新",
                                                tint = Color(0xFF228042)
                                            )
                                        }
                                    }
                                    LazyVerticalGrid(
                                        columns = GridCells.Fixed(2),
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(8.dp)
                                    ) {
                                        items(tempTransactions.size) { index ->
                                            val transaction = tempTransactions[index]

                                            Row(
                                                modifier = Modifier
                                                    .padding(8.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(Color(0xFFFDFBEC))
                                                    .border(1.dp, Color.LightGray)
                                                    .height(120.dp)
                                                    .padding(8.dp)
                                            ) {
                                                Column(
                                                    modifier = Modifier.weight(1f)
                                                ) {
                                                    Text(text = transaction.itemName, color = Color.Black, fontSize = 16.sp)
                                                    //Text(text = conditionText, color = textColor, fontSize = 16.sp)
                                                    Text(
                                                        text = "价格: ￥${transaction.itemPrice}\n数量: ${transaction.amount}",
                                                        fontSize = 12.sp
                                                    )
                                                    Text(text = transaction.time, fontSize = 12.sp)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //________________________________________________________________________________________ ↓ for admin ↓

                    else if (role == "admin") {
                        when (selectedOption) {
                            "管理商品" -> {
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
                                                    shopModule.shopSearch(searchText.text)
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
                                        items(tempItems.size) { index ->
                                            val item = tempItems[index]
                                            Column(
                                                modifier = Modifier
                                                    .padding(8.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(Color.White)
                                                    .clickable {
                                                        GlobalState.selectedItem = item
                                                        currentScene = "MerchandiseAdminScene"
                                                    }
                                                    .padding(8.dp)
                                            ) {
                                                AsyncImage(
                                                    load = { loadImageBitmap(File(item.imageRes)) },
                                                    painterFor = { remember { BitmapPainter(it) } },
                                                    contentDescription = "Item Image",
                                                    modifier = Modifier.size(108.dp)
                                                )
                                                Text(text = item.itemname, fontSize = 14.sp)
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
                                                    currentScene = "MerchandiseModifyScene"
                                                }
                                                .padding(10.dp)
                                        ) {
                                            Text(text = "添加商品", color = Color.White, fontSize = 16.sp)
                                        }
                                    }
                                }
                            }

                            "交易记录" -> {
                                Column {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Button(
                                            onClick = {
                                                selectedOption = "交易记录"
                                                shopModule.getAllTransactions()
                                            },
                                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                                            shape = CircleShape,
                                            modifier = Modifier.size(48.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.Refresh,
                                                contentDescription = "刷新",
                                                tint = Color(0xFF228042)
                                            )
                                        }
                                    }
                                    LazyVerticalGrid(
                                        columns = GridCells.Fixed(2),
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(8.dp)
                                    ) {
                                        if(!tempTransactions.isEmpty()){
                                        items(tempTransactions.size) { index ->
                                            val transaction = tempTransactions[index]

                                            Row(
                                                modifier = Modifier
                                                    .padding(8.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(Color(0xFFFDFBEC))
                                                    .border(1.dp, Color.LightGray)
                                                    .height(120.dp)
                                                    .padding(8.dp)
                                            ) {
                                                Column(
                                                    modifier = Modifier.weight(1f)
                                                ) {
                                                    Text(text = transaction.itemName, color = Color.Black, fontSize = 16.sp)
                                                    //Text(text = conditionText, color = textColor, fontSize = 16.sp)
                                                    Text(
                                                        text = "价格: ￥${transaction.itemPrice}    数量: ${transaction.amount}",
                                                        fontSize = 12.sp
                                                    )
                                                    Text(text = transaction.time, fontSize = 12.sp)
                                                    Text(text = "用户卡号：${transaction.cardNumber}", fontSize = 12.sp)
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
        }
    } else if (currentScene == "MerchandiseInfoScene") {
        MerchandiseInfoScene(onNavigateBack = { currentScene = "ShopScene" }, shopModule = shopModule)
    } else if (currentScene == "MerchandiseAdminScene") {
        MerchandiseAdminScene(onNavigateBack = { currentScene = "ShopScene" }, shopModule = shopModule)
    } else if (currentScene == "MerchandiseModifyScene") {
        MerchandiseModifyScene(onNavigateBack = { currentScene = "ShopScene" }, item = Merchandise(), "shop/addtolist/file_upload" )
    }
}
