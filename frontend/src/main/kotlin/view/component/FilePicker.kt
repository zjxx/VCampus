package view.component

import androidx.compose.runtime.*
import androidx.compose.ui.window.singleWindowApplication
import java.io.File
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import java.nio.file.Files
import java.nio.file.Paths
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter
import network.NettyClient
import network.NettyClientProvider

@Composable
fun FilePicker() {
    var filePath by remember { mutableStateOf<String?>(null) }
    val nettyClient = NettyClientProvider.nettyClient

    Column {
        Button(onClick = {
            val fileChooser = JFileChooser().apply {
                fileFilter = FileNameExtensionFilter("PNG Images", "png")
                isAcceptAllFileFilterUsed = false
            }
            val result = fileChooser.showOpenDialog(null)
            if (result == JFileChooser.APPROVE_OPTION) {
                filePath = fileChooser.selectedFile.absolutePath
            }
        }) {
            Text("Select PNG File")
        }

        filePath?.let {
            Text("Selected file: $it")
            Button(onClick = {
                nettyClient.sendFile("lib/file_upload",it) { response ->
                    println("Server response: $response")
                }
            }) {
                Text("Send to Server")
            }
        }
    }
}