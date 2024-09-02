package view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PaymentScene(onNavigate: (String) -> Unit, totalPrice: Double, cardBalance: Double) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        // 显示总价格
        Text(text = "总价格: $totalPrice", fontSize = 24.sp, modifier = Modifier.padding(16.dp))

        // 显示校园卡余额
        Text(text = "校园卡余额: $cardBalance", fontSize = 16.sp, modifier = Modifier.padding(16.dp))

        // 确认付款按钮
        Button(
            onClick = { /* Handle payment logic */ },
            modifier = Modifier.align(Alignment.End).padding(16.dp)
        ) {
            Text(text = "确认付款")
        }
    }
}