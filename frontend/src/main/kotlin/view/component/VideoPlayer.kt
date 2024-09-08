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

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.runtime.*
import androidx.compose.ui.window.DialogProperties

@Composable
fun ShowVideoPlayerDialog(url: String) {
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Video Player") },
            text = {
                VideoPlayer(url = url)
            },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Close")
                }
            },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        )
    }
}




@Composable
fun VideoPlayer(url: String) {
    var videoPath by remember { mutableStateOf<String?>(url) }
    val mediaPlayerComponent = remember { EmbeddedMediaPlayerComponent() }
    val nettyClient = NettyClientProvider.nettyClient

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {


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