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
fun VideoPlayerScreen(path: String,courseId:String) {
    var videoPath by remember { mutableStateOf<String?>(path) }
    val mediaPlayerComponent = remember { EmbeddedMediaPlayerComponent() }
    val nettyClient = NettyClientProvider.nettyClient
    var videoName by remember { mutableStateOf<String>("") }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Row {
            //写一个文本框，输入了文本框下面的button才能点击
            TextField(
                value = videoName,
                onValueChange = { videoName = it },
                label = { Text("Video Name") },
                modifier = Modifier.weight(1f)
            )
            Button(onClick = {
                nettyClient.sendVideo(
                    request = mapOf("courseId" to courseId, "videoName" to videoName),
                    type = "course/file_upload/video",
                    filePath = path
                ) { response ->
                    println("Response: $response")
                }
            }) {
                Text("Send Video")
            }
        }




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
