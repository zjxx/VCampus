package view.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
    val scrollState = rememberScrollState()

    LaunchedEffect(filePath, currentPage) {
        images = withContext(Dispatchers.IO) {
            val pdfFile = File(filePath)
            if (pdfFile.exists()) {
                PDDocument.load(pdfFile).use { document ->
                    totalPages = document.numberOfPages
                    val renderer = PDFRenderer(document)
                    val bufferedImage = renderer.renderImageWithDPI(currentPage - 1, 300f)
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
                .fillMaxWidth()
                .fillMaxHeight(0.8f),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "PDF Viewer", style = MaterialTheme.typography.h6)
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
                    Text(text = "Page $currentPage of $totalPages")
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