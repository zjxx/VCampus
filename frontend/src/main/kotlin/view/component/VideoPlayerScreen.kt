package view.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.unit.dp
import java.awt.BorderLayout
import java.awt.Canvas
import java.awt.FileDialog
import java.awt.Frame
import javax.swing.JPanel
import uk.co.caprica.vlcj.factory.MediaPlayerFactory
import uk.co.caprica.vlcj.player.base.MediaPlayer
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent

@Composable
fun VideoPlayerScreen() {
    var videoPath by remember { mutableStateOf<String?>(null) }
    val mediaPlayerComponent = remember { EmbeddedMediaPlayerComponent() }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
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
        }
    }
}