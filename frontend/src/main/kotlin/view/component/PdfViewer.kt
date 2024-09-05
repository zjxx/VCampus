package view.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer
import java.io.File
import java.awt.image.BufferedImage
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

@Composable
fun PdfViewer(filePath: String, onDismiss: () -> Unit) {
    var images by remember { mutableStateOf<List<ImageBitmap>>(emptyList()) }
    var currentPage by remember { mutableStateOf(1) }
    var totalPages by remember { mutableStateOf(1) }
    var imageWidth by remember { mutableStateOf(0) }
    var imageHeight by remember { mutableStateOf(0) }
    val scrollState = rememberScrollState()

    LaunchedEffect(filePath, currentPage) {
        images = withContext(Dispatchers.IO) {
            val pdfFile = File(filePath)
            if (pdfFile.exists()) {
                PDDocument.load(pdfFile).use { document ->
                    totalPages = document.numberOfPages
                    val renderer = PDFRenderer(document)
                    val bufferedImage = renderer.renderImageWithDPI(currentPage - 1, 300f)
                    imageWidth = bufferedImage.width
                    imageHeight = bufferedImage.height
                    listOf(bufferedImage.toComposeImageBitmap())
                }
            } else {
                emptyList()
            }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .width((imageWidth / 2).dp)
                .height((imageHeight / 2).dp + 100.dp), // Add extra space for buttons
            shape = MaterialTheme.shapes.medium
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "在线阅读", style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(scrollState)
                ) {
                    images.forEach { image ->
                        Image(bitmap = image, contentDescription = null)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { if (currentPage > 1) currentPage-- },
                        enabled = currentPage > 1
                    ) {
                        Text("Previous")
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.alignByBaseline()
                    ) {
                        TextField(
                            value = currentPage.toString(),
                            onValueChange = { newValue ->
                                val newPage = newValue.toIntOrNull()
                                if (newPage != null && newPage in 1..totalPages) {
                                    currentPage = newPage
                                }
                            },
                            modifier = Modifier.width(80.dp).alignByBaseline(),
                            textStyle = MaterialTheme.typography.body1,
                            singleLine = true
                        )
                        Text(
                            text = "of $totalPages",
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier.alignByBaseline()
                        )
                    }
                    Button(
                        onClick = { if (currentPage < totalPages) currentPage++ },
                        enabled = currentPage < totalPages
                    ) {
                        Text("Next")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onDismiss) {
                    Text("Close")
                }
            }
        }
    }
}

fun BufferedImage.toComposeImageBitmap(): ImageBitmap {
    val outputStream = ByteArrayOutputStream()
    ImageIO.write(this, "png", outputStream)
    val byteArray = outputStream.toByteArray()
    return org.jetbrains.skia.Image.makeFromEncoded(byteArray).toComposeImageBitmap()
}