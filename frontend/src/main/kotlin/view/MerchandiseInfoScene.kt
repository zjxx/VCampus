// MerchandiseInfoScene.kt
package view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.Merchandise
import module.ShopModule
import java.io.File

@Composable
fun MerchandiseInfoScene(item: Merchandise) {
    var quantity by remember { mutableStateOf(1) }
    val shopModule = ShopModule(
        onSearchSuccess = { },
        onBuySuccess = { result -> },
        onEnterSuccess = { },
        onAddItemToCartSuccess = { result -> },
        onRemoveItemFromCartSuccesss = { result -> },
        onShopAddToListSuccess = { result -> },
        onGetAllTransactionsSuccess = { result -> },
        onGetTransactionsByCardNumberSuccess = { result -> },
    )

    Column(modifier = Modifier.fillMaxSize()) {
        // Top section
        Row(modifier = Modifier.weight(0.6f).padding(16.dp)) {
            AsyncImage(
                load = { loadImageBitmap(File(item.imageRes)) },
                painterFor = { remember { BitmapPainter(it) } },
                contentDescription = "Merchandise Image",
                modifier = Modifier.size(128.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = item.itemname, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = item.description, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "商品ID: ${item.itemUuid}", fontSize = 16.sp)
                Text(text = "库存: ${item.stock}", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "销量: ${item.salesVolume}", fontSize = 16.sp)
                Text(text = "价格: ${item.price}", fontSize = 16.sp)
            }
        }

        // Bottom section
        Column(modifier = Modifier.weight(0.4f).padding(16.dp)) {
            // Quantity selector
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "数量: ", fontSize = 16.sp)
                OutlinedTextField(
                    value = quantity.toString(),
                    onValueChange = { quantity = it.toIntOrNull() ?: 1 },
                    modifier = Modifier.width(80.dp),
                    singleLine = true
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Add to Cart button
            Button(
                onClick = { shopModule.addItemToCart(item.itemUuid, "cartUuid") },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Text(text = "加入购物车", fontSize = 16.sp)
            }

            // Buy Now button
            Button(
                onClick = { shopModule.shopBuy(item.itemUuid, quantity.toString(), item.itemname) },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Text(text = "立即购买", fontSize = 16.sp)
            }
        }
    }
}