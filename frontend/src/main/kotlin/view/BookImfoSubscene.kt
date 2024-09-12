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
import data.ColorPack
import data.ColorPack.choose
import data.UserSession
import module.LibraryModule
import view.component.GlobalState
import java.io.File

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
            text = "<",
            fontSize = 32.sp,
            color = ColorPack.sideColor2[choose.value].value,
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
            // Book cover image
            Box(
                modifier = Modifier
                    .weight(0.45f)
                    .padding(16.dp),
                contentAlignment = Alignment.TopStart
            ) {
                AsyncImage(
                    load = { loadImageBitmap(File(book.coverImage)) },
                    painterFor = { remember { BitmapPainter(it) } },
                    contentDescription = "Book Cover",
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Book details
            Column(
                modifier = Modifier
                    .weight(0.6f)
                    .padding(16.dp)
            ) {
                Text(text = "《 ${book.bookname} 》", fontSize = 24.sp, color = ColorPack.sideColor2[choose.value].value, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                Text(text = "\n作者: ${book.author}", fontSize = 20.sp, color = ColorPack.sideColor2[choose.value].value)
                Text(text = "\n> 出版社: ${book.publisher}\n> 语言: ${book.language}", fontSize = 16.sp, color = ColorPack.sideColor2[choose.value].value)
                Spacer(modifier = Modifier.height(8.dp))

                Divider(color = ColorPack.mainColor1[choose.value].value, thickness = 1.dp)

                Text(text = "\n描述:  ${book.description}", fontSize = 18.sp,  color = ColorPack.sideColor2[choose.value].value, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "\n种类:  ${book.kind}\n", fontSize = 18.sp, color = ColorPack.sideColor2[choose.value].value)

                Divider(color = ColorPack.mainColor1[choose.value].value, thickness = 1.dp)

                Spacer(modifier = Modifier.height(8.dp))
                val quantityColor = if (book.quantity == 0) Color.Red else ColorPack.sideColor2[choose.value].value
                val validColor = if (book.quantity == 0) Color.Red else ColorPack.sideColor2[choose.value].value
                Text(text = "书籍总数: ${book.quantity}", fontSize = 16.sp, color = quantityColor)
                Text(text = "剩余可借: ${book.valid}", fontSize = 16.sp, color = validColor)
            }
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
                modifier = Modifier.size(168.dp, 48.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = ColorPack.mainColor1[choose.value].value)
            ) {
                Text(text = "借书", color = ColorPack.backgroundColor1[choose.value].value, fontSize = 18.sp)
            }
        }
    }
}