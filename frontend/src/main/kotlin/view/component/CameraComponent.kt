package view.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.bytedeco.javacv.CanvasFrame
import org.bytedeco.javacv.FFmpegFrameRecorder
import org.bytedeco.javacv.FrameGrabber
import org.bytedeco.javacv.OpenCVFrameGrabber

@Composable
fun CameraComponent() {
    var selectedCamera by remember { mutableStateOf<FrameGrabber?>(null) }
    var isRecording by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val cameras = remember {
        mutableStateListOf<FrameGrabber>().apply {
            for (i in 0 until 10) {
                try {
                    add(OpenCVFrameGrabber(i))
                } catch (e: Exception) {
                    // Ignore unavailable cameras
                }
            }
        }
    }

    Row(modifier = Modifier.padding(16.dp)) {
        Column(modifier = Modifier.weight(1f)) {
            Text("Select Camera", style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(8.dp))
            cameras.forEachIndexed { index, camera ->
                Button(onClick = { selectedCamera = camera }) {
                    Text("Camera ${camera.toString()}")
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (selectedCamera != null) {
                Button(onClick = {
                    isRecording = !isRecording
                    if (isRecording) {
                        scope.launch(Dispatchers.IO) {
                            startRecording(selectedCamera!!)
                        }
                    } else {
                        scope.launch(Dispatchers.IO) {
                            stopRecording(selectedCamera!!)
                        }
                    }
                }) {
                    Text(if (isRecording) "Stop Recording" else "Start Recording")
                }
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            if (selectedCamera != null) {
                LaunchedEffect(selectedCamera) {
                    displayCameraFeed(selectedCamera!!)
                }
            }
        }
    }
}

private var recorder: FFmpegFrameRecorder? = null
private var canvasFrame: CanvasFrame? = null

private suspend fun startRecording(grabber: FrameGrabber) {
    withContext(Dispatchers.IO) {
        closeCameraFeed(grabber)
        try {
            grabber.start()
        } catch (e: Exception) {
            println("Error starting grabber: ${e.message}")
            return@withContext
        }

        recorder = FFmpegFrameRecorder("output.mp4", grabber.imageWidth, grabber.imageHeight).apply {
            start()
        }

        while (true) {
            val frame = try {
                grabber.grab()
            } catch (e: Exception) {
                println("Error grabbing frame: ${e.message}")
                break
            }
            if (frame == null) break
            recorder?.record(frame)
        }
    }
}


private suspend fun stopRecording(grabber: FrameGrabber) {
    withContext(Dispatchers.IO) {
        recorder?.stop()
        recorder?.release()
        recorder = null
        canvasFrame?.dispose()
        canvasFrame = null
        grabber.stop()
    }
}

private suspend fun displayCameraFeed(grabber: FrameGrabber) {
    withContext(Dispatchers.IO) {
        canvasFrame = CanvasFrame("Camera Feed").apply {
            isVisible = true
            defaultCloseOperation = CanvasFrame.EXIT_ON_CLOSE
        }

        try {
            grabber.start()
        } catch (e: Exception) {
            println("Error starting grabber: ${e.message}")
            return@withContext
        }

        while (canvasFrame?.isVisible == true) {
            val frame = try {
                grabber.grab()
            } catch (e: Exception) {
                println("Error grabbing frame: ${e.message}")
                break
            }
            if (frame == null) break
            canvasFrame?.showImage(frame)
        }
    }
}

private suspend fun closeCameraFeed(grabber: FrameGrabber) {
    withContext(Dispatchers.IO) {
        canvasFrame?.dispose()
        canvasFrame = null
        grabber.stop()
    }
}