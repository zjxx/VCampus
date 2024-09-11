package view.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.bytedeco.ffmpeg.global.avcodec
import org.bytedeco.javacv.*
import java.util.concurrent.TimeUnit

@Composable
fun CameraComponent(onRecordingFinished: (String) -> Unit) {
    var selectedCamera by remember { mutableStateOf<FrameGrabber?>(null) }
    var isRecording by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }
    var selectedCameraIndex by remember { mutableStateOf(-1) }
    var recordingTime by remember { mutableStateOf(0) }
    var selectedPath by remember { mutableStateOf("src/main/temp/") }
    var path by remember { mutableStateOf("") }
    val cameras = remember {
        mutableStateListOf<FrameGrabber>().apply {
            for (i in 0 until 1) {
                try {
                    add(OpenCVFrameGrabber(i))
                } catch (e: Exception) {
                    // Ignore unavailable cameras
                }
            }
            // Add desktop recording option
            try {
                val desktopGrabber = FFmpegFrameGrabber("desktop").apply {
                    format = "gdigrab"
                    setOption("offset_x", "0")
                    setOption("offset_y", "0")
                    setOption("framerate", "25")
                    setOption("draw_mouse", "0")
                    setOption("video_size", "1920x1080")
                }
                add(desktopGrabber)
            } catch (e: Exception) {
                // Ignore if desktop recording is not available
            }
        }
    }

    Row(modifier = Modifier.padding(16.dp)) {
        Column(modifier = Modifier.weight(1f)) {
            Text("Select Camera", style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(8.dp))

            Box {
                Button(onClick = { expanded = true }) {
                    Text(if (selectedCameraIndex == -1) "Select Camera" else "Camera $selectedCameraIndex")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    cameras.forEachIndexed { index, camera ->
                        DropdownMenuItem(onClick = {
                            selectedCamera = camera
                            selectedCameraIndex = index
                            expanded = false
                        }) {
                            Text(if (index == cameras.size - 1) "Desktop" else "Camera $index")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    isRecording = !isRecording
                    if (isRecording) {
                        recordingTime = 0
                        val outputPath = "$selectedPath/video_${System.currentTimeMillis()}.mp4"
                        path = outputPath
                        scope.launch(Dispatchers.IO) {
                            startRecording(selectedCamera!!, outputPath)
                        }
                    } else {
                        scope.launch(Dispatchers.IO) {
                            stopRecording(selectedCamera!!)
                            onRecordingFinished(path)
                        }
                    }
                },
                enabled = selectedCamera != null && selectedPath.isNotEmpty()
            ) {
                Text(if (isRecording) "Stop Recording" else "Start Recording")
            }

            if (isRecording) {
                LaunchedEffect(Unit) {
                    while (isRecording) {
                        delay(1000)
                        recordingTime++
                    }
                }
                Text("Recording Time: ${recordingTime}s")
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

private suspend fun startRecording(grabber: FrameGrabber, outputPath: String) {
    withContext(Dispatchers.IO) {
        recorder = FFmpegFrameRecorder(outputPath, grabber.imageWidth, grabber.imageHeight).apply {
            videoCodec = avcodec.AV_CODEC_ID_H264 // 设置视频编码器
            format = "mp4" // 设置输出格式
            frameRate = 30.0 // 设置帧率
            videoBitrate = 200000 // 设置比特率
            start()
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
            recorder?.record(frame)
        }
    }
}

private suspend fun closeCameraFeed(grabber: FrameGrabber) {
    withContext(Dispatchers.IO) {
        canvasFrame?.dispose()
        canvasFrame = null
    }
}