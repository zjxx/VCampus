package view.component
import androidx.compose.foundation.background
import androidx.compose.ui.Alignment
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import NaviItem
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun TopNavBar(naviItems: List<NaviItem>, onItemSelected: (NaviItem) -> Unit) {
    var selectedItem by remember { mutableStateOf<NaviItem?>(null) }

    Row(modifier = Modifier.fillMaxWidth().height(56.dp).background(Color(0xFF006400)),horizontalArrangement = Arrangement.Center, // 水平居中
        verticalAlignment = Alignment.CenterVertically)  {
        naviItems.forEach { item ->
            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .clickable {
                        selectedItem = item
                        onItemSelected(item)
                    }
                    .background(if (item == selectedItem) Color(0xFF006400) else Color.Transparent)
                    .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(imageVector = item.icon, contentDescription = item.name)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = item.name, fontSize = 14.sp)
            }
        }
    }
}
