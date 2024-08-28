// src/main/kotlin/view/ShopScene.kt
package view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import module.ShopModule

@Composable
fun ShopScene() {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var searchResults by remember { mutableStateOf(listOf<String>()) }

    val shopModule = ShopModule(
        onSearchSuccess = { result ->
            searchResults = result.split("\n")
        },
        onBuySuccess = { result ->
            // Handle buy success
        }
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Title
        Text(
            text = "欢迎光临校园超市！",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        // Divider
        Divider(
            color = Color(0xFFFFD700), // Dark yellow color
            thickness = 2.dp,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 8.dp)
        )

        // Search Module
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
                    .padding(8.dp)
                    .background(Color.White)
                    .padding(16.dp),
                singleLine = true,
            )
            Box(
                modifier = Modifier
                    .weight(0.15f)
                    .clip(RoundedCornerShape(8.dp))
                    .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .background(Color(0xFF228042))
                    .clickable { shopModule.shopSearch(searchText.text) }
                    .padding(16.dp)
            ) {
                Text(text = "搜索", color = Color.White)
            }
        }

        // Search Results
        Divider(color = Color.Gray, thickness = 1.dp)
        Column(modifier = Modifier.padding(top = 8.dp)) {
            if (searchResults.isEmpty()) {
                Text(text = "无信息", fontSize = 16.sp)
            } else {
                searchResults.forEach { result ->
                    Text(text = result, fontSize = 16.sp, modifier = Modifier.padding(vertical = 4.dp))
                }
            }
        }
    }
}