// src/main/kotlin/view/BookInfoSubscene.kt
package view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.Book

@Composable
fun BookInfoSubscene(onNavigate: (String) -> Unit, book: Book, isBorrowed: Boolean) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // 返回按钮
        Text(
            text = "返回",
            fontSize = 18.sp,
            modifier = Modifier
                .clickable { onNavigate("LibraryScene") }
                .padding(8.dp)
        )

        Row(modifier = Modifier.fillMaxSize()) {
            // 左侧书籍封面图片
            Box(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight()
                    .padding(16.dp)
                    .background(Color.LightGray)
                    .clip(RoundedCornerShape(8.dp))
            ) {
//                Image(
//                    painter = painterResource(id = book.coverImageRes),
//                    contentDescription = "Book Cover",
//                    contentScale = ContentScale.FillHeight,
//                    modifier = Modifier
//                        .fillMaxHeight(0.6f)
//                        .align(Alignment.Center)
//                )
            }

            // 右侧书籍详细信息
            Column(
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxHeight()
                    .padding(16.dp)
            ) {
                // 书名和作者
                Text(text = "书名: ${book.title}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = "作者: ${book.author}", fontSize = 16.sp)

                Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

                // 出版信息
                Text(text = "出版信息: ${book.publisher}, ${book.publishDate}", fontSize = 16.sp)

                Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

                // 图书简介
                Text(text = "图书简介: ${book.description}", fontSize = 16.sp)

                Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

                // 借阅信息和借书按钮
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "借阅信息: ${book.borrowInfo}", fontSize = 16.sp, modifier = Modifier.weight(0.5f))
                    Button(
                        onClick = { /* Handle borrow book */ },
                        enabled = !isBorrowed,
                        modifier = Modifier.weight(0.5f)
                    ) {
                        Text(text = if (isBorrowed) "已借阅" else "借书")
                    }
                }
            }
        }
    }
}