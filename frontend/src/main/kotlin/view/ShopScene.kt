package view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.Merchandise

@Composable
fun ShopScene(onNavigate: (String) -> Unit, role: String) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var searchResults by remember { mutableStateOf(listOf<Merchandise>()) }
    var cartItems by remember { mutableStateOf(listOf<Merchandise>()) }

//    val shopModule = ShopModule(
//        onSearchSuccess = { result ->
//            searchResults = result.map {
//                Merchandise(
//                    id = it.id,
//                    name = it.name,
//                    price = it.price,
//                    imageRes = it.imageRes
//                )
//            }
//        },
//        onBuySuccess = { result ->
//            // Handle buy success
//        }
//    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // 搜索模块
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .weight(0.85f)
                    .padding(8.dp),
                singleLine = true,
            )
            Box(
                modifier = Modifier
                    .weight(0.15f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF228042))
                    //.clickable { shopModule.shopSearch(searchText.text) }
                    .padding(16.dp)
            ) {
                Text(text = "搜索", color = Color.White)
            }
        }

        // 商品列表
        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            searchResults.chunked(2).forEach { rowItems ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    rowItems.forEach { item ->
                        Column(
                            modifier = Modifier
                                .weight(0.5f)
                                .padding(8.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.LightGray)
                                .clickable { onNavigate("PaymentScene") }
                                .padding(8.dp)
                        ) {
//                            Image(
//                                painter = painterResource(id = item.imageRes),
//                                contentDescription = "Merchandise Image",
//                                modifier = Modifier.size(128.dp)
//                            )
                            Text(text = item.name, fontSize = 16.sp)
                            Text(text = "价格: ${item.price}", fontSize = 16.sp)
                        }
                    }
                }
            }
        }

        // 底部按钮
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = { onNavigate("ShopCartScene") },
                modifier = Modifier.weight(0.2f)
            ) {
                Text(text = "加入购物车")
            }
            Button(
                onClick = { onNavigate("PaymentScene") },
                modifier = Modifier.weight(0.2f)
            ) {
                Text(text = "立即购买")
            }
        }
    }
}