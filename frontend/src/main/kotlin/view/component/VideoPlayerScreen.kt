package view.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.unit.dp
import java.awt.BorderLayout
import java.awt.FileDialog
import java.awt.Frame
import javax.swing.JPanel
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent
import utils.NettyClient
import utils.NettyClientProvider

@Composable
fun VideoPlayerScreen() {
    var videoPath by remember { mutableStateOf<String?>(null) }
    val mediaPlayerComponent = remember { EmbeddedMediaPlayerComponent() }
    val nettyClient = NettyClientProvider.nettyClient

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row {
            Button(onClick = {
            val fileDialog = FileDialog(Frame(), "Select Video", FileDialog.LOAD)
            fileDialog.isVisible = true
            val selectedFile = fileDialog.file
            if (selectedFile != null) {
                videoPath = "${fileDialog.directory}$selectedFile"
            }
        }) {
            Text("Select Video")
        }
            videoPath?.let { path ->

                Button(onClick = {
                    nettyClient.sendFile(request = mapOf("key" to "value"), type = "course/file_upload/video", filePath = path) { response ->
                        println("Response: $response")
                    }
                }) {
                    Text("Send Video")
                }
            } }


        Spacer(modifier = Modifier.height(16.dp))

        videoPath?.let { path ->
            LaunchedEffect(path) {
                mediaPlayerComponent.mediaPlayer().media().play(path)
            }

            SwingPanel(
                factory = {
                    JPanel(BorderLayout()).apply {
                        add(mediaPlayerComponent, BorderLayout.CENTER)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}