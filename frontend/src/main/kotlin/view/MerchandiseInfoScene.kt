// MerchandiseInfoScene.kt
package view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
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
                Text(text = "${item.itemname}", fontSize = 24.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                Text(text = "\n> 价格: ${item.price}\n> uuid: ${item.itemUuid}", fontSize = 16.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(8.dp))

                Divider(color = Color(0xFF228042), thickness = 1.dp)

                Text(text = "\n描述:  ${item.description}", fontSize = 18.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
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
            Button(
                onClick = { shopModule.shopBuy(item.itemUuid, "1", item.itemname) },
                modifier = Modifier.size(168.dp, 48.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF228042))
            ) {
                Text(text = "购买", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}