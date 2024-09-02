package view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.Merchandise

@Composable
fun ShopCartScene(onNavigate: (String) -> Unit, cartItems: List<Merchandise>) {
    var totalPrice by remember { mutableStateOf(cartItems.sumOf { it.price }) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // 返回按钮
        Text(
            text = "返回",
            fontSize = 18.sp,
            modifier = Modifier
                .clickable { onNavigate("ShopScene") }
                .padding(8.dp)
        )

        // 商品列表
        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            cartItems.forEach { item ->
                Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
//                    Image(
//                        painter = painterResource(id = item.imageRes),
//                        contentDescription = "Merchandise Image",
//                        modifier = Modifier.size(64.dp)
//                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = "ID: ${item.id}", fontSize = 16.sp)
                        Text(text = "价格: ${item.price}", fontSize = 16.sp)
                    }
                }
            }
        }

        // 统计价格总和和购买按钮
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "总价格: $totalPrice", fontSize = 18.sp, modifier = Modifier.weight(0.8f))
            Button(
                onClick = { onNavigate("PaymentScene") },
                modifier = Modifier.weight(0.2f)
            ) {
                Text(text = "购买")
            }
        }
    }
}