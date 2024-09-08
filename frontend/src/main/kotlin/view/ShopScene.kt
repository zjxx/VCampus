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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.Merchandise
import data.StoreTransaction
import data.UserSession
import module.ShopModule
import view.component.GlobalState
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
        onGetAllTransactionsSuccess = {},
        onGetTransactionsByCardNumberSuccess = { result ->
            tempTransactions = emptyList()
            tempTransactions = result
        },
        onViewSuccess = { result ->
            tempItems = emptyList()
            tempItems = result
        }
    )

    LaunchedEffect(shopModule.tempItems) {
        tempItems = shopModule.tempItems
    }

    if (currentScene == "ShopScene") {
        Row(modifier = Modifier.fillMaxSize()) {
            // 侧边导航栏


            Row(modifier = Modifier.fillMaxSize()) {
                if (isCollapsed) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(0.03f)
                            .background(Color.LightGray)
                            .shadow(4.dp, spotColor = Color.Gray, ambientColor = Color.Gray, clip = false),
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
                                modifier = Modifier.clickable { isCollapsed = false }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            if (role == "student") {
                                Icon(imageVector = Icons.Default.Person, contentDescription = "购物")
                                Spacer(modifier = Modifier.height(16.dp))
                                Icon(imageVector = Icons.Default.Schedule, contentDescription = "查看购物车")
                                Spacer(modifier = Modifier.height(16.dp))
                                Icon(imageVector = Icons.Default.Image, contentDescription = "消费记录")
                            } else if (role == "admin") {
                                Icon(imageVector = Icons.Default.Add, contentDescription = "管理商品")
                                Spacer(modifier = Modifier.height(16.dp))
                                Icon(imageVector = Icons.Default.List, contentDescription = "交易记录")
                            }
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(0.2f)
                            .background(Color.LightGray)
                            .shadow(4.dp, spotColor = Color.Gray, ambientColor = Color.Gray, clip = false)
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Collapse",
                                modifier = Modifier.clickable { isCollapsed = true }
                            )
                        }
                        if (role == "student") {
                            TextButton(
                                onClick = {
                                    selectedOption = "购物"
                                    shopModule.enterShop()
                                },
                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Person, contentDescription = "购物")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "购物", fontSize = 18.sp, color = Color.Black)
                            }
                            TextButton(
                                onClick = {
                                    selectedOption = "查看购物车"
                                    shopModule.viewCart()
                                },
                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Schedule, contentDescription = "查看购物车")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "查看购物车", fontSize = 18.sp, color = Color.Black)
                            }
                            TextButton(
                                onClick = { selectedOption = "消费记录" },
                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Image, contentDescription = "消费记录")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "消费记录", fontSize = 18.sp, color = Color.Black)
                            }
                        } else if (role == "admin") {
                            TextButton(
                                onClick = { selectedOption = "管理商品" },
                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = "管理商品")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "管理商品", fontSize = 18.sp, color = Color.Black)
                            }
                            TextButton(
                                onClick = { selectedOption = "消费记录" },
                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.List, contentDescription = "消费记录")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "消费记录", fontSize = 18.sp, color = Color.Black)
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
                                                    modifier = Modifier.size(108.dp)
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
                                Column {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Button(
                                            onClick = {
                                                selectedOption = "查看购物车"
                                                shopModule.viewCart()
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
                                            .fillMaxHeight()
                                            .padding(8.dp)
                                    ) {
                                        items(tempItems.size) { index ->
                                            val item = tempItems[index]

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
                                                        text = "价格 > ${item.price}\n库存 > ${item.stock}",
                                                        fontSize = 12.sp
                                                    )
                                                }
                                                Column(
                                                    modifier = Modifier.align(Alignment.CenterVertically),
                                                    verticalArrangement = Arrangement.Center,
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
                                                        //enabled = item. != "haveBorrowed"
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
                                            .fillMaxHeight()
                                            .padding(8.dp)
                                    ) {
                                        items(tempItems.size) { index ->
                                            val item = tempItems[index]

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
                                                        text = "价格 > ${item.price}\n库存 > ${item.stock}",
                                                        fontSize = 12.sp
                                                    )
                                                }
                                                Column(
                                                    modifier = Modifier.align(Alignment.CenterVertically),
                                                    verticalArrangement = Arrangement.Center,
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
                                                        //enabled = item. != "haveBorrowed"
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
                                                        currentScene = ""
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
                                                    currentScene = ".."
                                                }
                                                .padding(10.dp)
                                        ) {
                                            Text(text = "添加商品", color = Color.White, fontSize = 16.sp)
                                        }
                                    }
                                }
                            }

                            "交易记录" -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .padding(16.dp)
                                ) {
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
                                                    shopModule.getAllTransactions()
                                                }
                                                .padding(16.dp)
                                        ) {
                                            Text(text = "搜索", color = Color.White, fontSize = 16.sp)
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Divider(color = Color.Gray, thickness = 1.dp)
                                    // Display borrowing records
                                }
                            }
                        }
                    }
                }
            }
        }
    } else if (currentScene == "MerchandiseInfoScene") {
        MerchandiseInfoScene(onNavigateBack = { currentScene = "ShopScene" }, shopModule = shopModule)
    }
}
