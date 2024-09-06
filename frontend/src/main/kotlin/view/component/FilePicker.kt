// FilePicker.kt
package view.component

import androidx.compose.runtime.*
import java.io.File
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.unit.dp
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter
import utils.NettyClientProvider
import view.AsyncImage
import view.loadImageBitmap

@Composable
fun FilePicker() {
    var filePath by remember { mutableStateOf<String?>(null) }
    val nettyClient = NettyClientProvider.nettyClient

    Column {
        Button(onClick = {
            val fileChooser = JFileChooser().apply {
                fileFilter = FileNameExtensionFilter("Image Files", "png", "jpg")
                isAcceptAllFileFilterUsed = false
            }
            val result = fileChooser.showOpenDialog(null)
            if (result == JFileChooser.APPROVE_OPTION) {
                filePath = fileChooser.selectedFile.absolutePath
            }
        }) {
            Text("Select PNG or JPG File")
        }

        filePath?.let {
            Text("Selected file: $it")
            //展示该图片
            AsyncImage(
                load = { loadImageBitmap(File(it)) },
                painterFor = { remember { BitmapPainter(it) } },
                contentDescription = "Book Cover",
                modifier = Modifier.size(216.dp)
            )
            Button(onClick = {
                nettyClient.sendFile("lib/file_upload", it) { response ->
                    println("Server response: $response")
                }
            }) {
                Text("Send to Server")
            }
        }
    }
}