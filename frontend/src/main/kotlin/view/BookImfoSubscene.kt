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
import data.Book
import data.UserSession
import module.LibraryModule
import view.component.GlobalState

val userid = UserSession.userId.toString()

fun getBook(): Book {
    return GlobalState.selectedBook ?: Book()
}

@Composable
fun BookImfoSubscene(onNavigateBack: () -> Unit, libraryModule: LibraryModule) {
    val book = getBook()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Back button
        Text(
            text = "←",
            fontSize = 32.sp,
            modifier = Modifier
                .clickable { onNavigateBack() }
                .padding(8.dp)
        )

        // Book cover image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.5f)
                .padding(16.dp),
            contentAlignment = Alignment.TopStart
        ) {
            AsyncImage(
                load = { loadImageBitmap(book.coverImage) },
                painterFor = { remember { BitmapPainter(it) } },
                contentDescription = "Book Cover",
                modifier = Modifier.size(240.dp)
            )
        }

        // Book details
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "书名: ${book.bookname}, 作者: ${book.author}", fontSize = 20.sp)
            Text(text = "出版社: ${book.publisher}, 语言: ${book.language}", fontSize = 16.sp, color = Color.DarkGray)
        }

        // Divider
        Divider(color = Color(0xFF228042), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))

        // Description and kind
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "描述: ${book.description}", fontSize = 16.sp)
            Text(text = "种类: ${book.kind}", fontSize = 16.sp)
        }

        // Quantity and valid
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "数量: ${book.quantity}", fontSize = 16.sp)
            Text(text = "有效数量: ${book.valid}", fontSize = 16.sp)
        }

        // Borrow button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Button(
                onClick = { libraryModule.libAddToLits(book.isbn) },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF228042))
            ) {
                Text(text = "借书", color = Color.White)
            }
        }
    }
}