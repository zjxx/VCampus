// MerchandiseInfoScene.kt
package view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.Merchandise
import module.ShopModule
import view.component.GlobalState
import java.io.File

fun getItem(): Merchandise {
    return GlobalState.selectedItem ?: Merchandise()
}

@Composable
fun MerchandiseInfoScene(onNavigateBack: () -> Unit, shopModule: ShopModule) {

    val item = getItem()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Back button
        Text(
            text = "<",
            fontSize = 32.sp,
            modifier = Modifier
                .clickable { onNavigateBack() }
                .padding(8.dp)
        )

        // Upper half: Image and details
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Merchandise image
            Box(
                modifier = Modifier
                    .weight(0.4f)
                    .padding(16.dp),
                contentAlignment = Alignment.TopStart
            ) {
                AsyncImage(
                    load = { loadImageBitmap(File(item.imageRes)) },
                    painterFor = { remember { BitmapPainter(it) } },
                    contentDescription = "Merchandise Image",
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Merchandise details
            Column(
                modifier = Modifier
                    .weight(0.6f)
                    .padding(16.dp)
            ) {
                Text(
                    text = "${item.itemname}",
                    fontSize = 24.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
                Text(text = "\n> 价格: ${item.price}", fontSize = 16.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(8.dp))

                Divider(color = Color(0xFF228042), thickness = 1.dp)

                Text(
                    text = "\n描述:  ${item.description}",
                    fontSize = 18.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "\n销量:  ${item.salesVolume}\n", fontSize = 18.sp)

                Divider(color = Color(0xFF228042), thickness = 1.dp)

                Spacer(modifier = Modifier.height(8.dp))
                val stockColor = if (item.stock == "0") Color.Red else Color.Black
                Text(text = "库存: ${item.stock}", fontSize = 16.sp, color = stockColor)
            }
        }

        // Buy button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            var quantity by remember { mutableStateOf(1) }

            Row {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Decrement button
                    Button(
                        onClick = { if (quantity > 1) quantity-- },
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .size(54.dp)
                            .clip(CircleShape),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                    ) {
                        Text(text = "-", color = Color.Black, fontSize = 28.sp)
                    }

                    // Quantity text field
                    TextField(
                        value = quantity.toString(),
                        onValueChange = { newValue ->
                            newValue.toIntOrNull()?.let {
                                if (it >= 1) quantity = it
                            }
                        },
                        modifier = Modifier
                            .width(80.dp)
                            .height(60.dp)
                            .padding(horizontal = 8.dp),
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Unspecified)
                    )

                    // Increment button
                    Button(
                        onClick = { quantity++ },
                        modifier = Modifier
                            .padding(start = 4.dp, end = 4.dp)
                            .size(54.dp)
                            .clip(CircleShape),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                    ) {
                        Text(text = "+", color = Color.Black, fontSize = 28.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Row {
                        Button(
                            onClick = { shopModule.addItemToCart(item.itemUuid, quantity.toString()) },
                            modifier = Modifier
                                .size(136.dp, 48.dp)
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 18.dp,
                                        bottomStart = 18.dp,
                                        topEnd = 0.dp,
                                        bottomEnd = 0.dp
                                    )
                                )
                                .padding(end = 1.dp), // Remove padding between buttons
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFEAE00))
                        ) {
                            Text(
                                text = "加入购物车",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                            )
                        }
                        Button(
                            onClick = { shopModule.shopBuy(item.itemUuid, quantity.toString(), item.itemname) },
                            modifier = Modifier
                                .size(136.dp, 48.dp)
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 0.dp,
                                        bottomStart = 0.dp,
                                        topEnd = 18.dp,
                                        bottomEnd = 18.dp
                                    )
                                )
                                .padding(start = 0.dp), // Remove padding between buttons
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFE5A00))
                        ) {
                            Text(
                                text = "立即购买",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}



