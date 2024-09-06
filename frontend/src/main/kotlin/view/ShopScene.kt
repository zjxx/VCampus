// ShopScene.kt
package view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import data.Merchandise
import module.ShopModule
import java.io.File

@Composable
fun ShopScene(onNavigate: (String) -> Unit, role: String) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var searchResults by remember { mutableStateOf(listOf<Merchandise>()) }
    var selectedMerchandise by remember { mutableStateOf<Merchandise?>(null) }

    val shopModule = ShopModule(
        onSearchSuccess = { results ->
            searchResults = results
            if (results.isNotEmpty()) {
                selectedMerchandise = results[0]
            }
        },
        onBuySuccess = { result -> },
        onEnterSuccess = { results -> searchResults = results },
        onAddItemToCartSuccess = { result -> },
        onRemoveItemFromCartSuccesss = { result -> },
        onShopAddToListSuccess = { result -> },
        onGetAllTransactionsSuccess = { result -> },
        onGetTransactionsByCardNumberSuccess = { result -> },
    )

    Column(modifier = Modifier.fillMaxSize()) {
        // Top section
        Column(
            modifier = Modifier
                .weight(0.2f)
                .padding(16.dp)
        ) {
            Text(
                text = "网上校园商城",
                fontSize = 24.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
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
                    Text(text = "搜索", color = Color.White, fontSize = 14.sp)
                }
            }
        }

        // Main content
        Row(modifier = Modifier.weight(0.8f)) {
            // Left section: Scrollable list
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.382f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                searchResults.forEach { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { selectedMerchandise = item }
                            .background(Color.LightGray)
                            .padding(16.dp)
                    ) {
                        Column {
                            AsyncImage(
                                load = { loadImageBitmap(File(item.imageRes)) },
                                painterFor = { remember { BitmapPainter(it) } },
                                contentDescription = "",
                                modifier = Modifier.size(128.dp)
                            )
                            Text(text = item.itemname, fontSize = 16.sp)
                            Text(text = "价格: ${item.price}", fontSize = 14.sp)
                            Text(text = "库存: ${item.stock}", fontSize = 14.sp)
                        }
                    }
                }
            }

            // Right section: Detailed information
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.618f)
                    .padding(16.dp)
            ) {
                selectedMerchandise?.let { item ->
                    MerchandiseInfoScene(item)
                } ?: run {
                    Text(text = "请选择一个商品", fontSize = 16.sp)
                }
            }
        }
    }
}