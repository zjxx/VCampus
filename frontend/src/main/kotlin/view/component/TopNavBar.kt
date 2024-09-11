// 修改 TopNavBar.kt
package view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.NaviItem
import module.ShopModule


@Composable
fun TopNavBar(naviItems: List<NaviItem>, onItemSelected: (NaviItem) -> Unit) {
    var selectedItem by remember { mutableStateOf<NaviItem?>(null) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color(0xFF006400)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row {
            naviItems.filter { it.name != "退出" }.forEach { item ->
                Row(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .clickable {
                            selectedItem = item
                            onItemSelected(item)
                        }
                        .background(if (item == selectedItem) Color(0xFF004d00) else Color.Transparent)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = item.icon, contentDescription = item.name, tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = item.name, fontSize = 14.sp, color = Color.White)
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f)) // Add spacer here
        Button(onClick = { onItemSelected(naviItems.first { it.name == "退出" }) },colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF006400))) {
            Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "退出", tint = Color.White)
        }
    }
}