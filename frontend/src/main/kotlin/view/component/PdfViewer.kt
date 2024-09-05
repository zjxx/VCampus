package view.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import java.io.File
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@Composable
fun PdfViewer(filePath: String, onDismiss: () -> Unit) {
    var pdfText by remember { mutableStateOf("Loading...") }
    var currentPage by remember { mutableStateOf(1) }
    var totalPages by remember { mutableStateOf(1) }
    val scrollState = rememberScrollState()

    LaunchedEffect(filePath, currentPage) {
        pdfText = withContext(Dispatchers.IO) {
            val pdfFile = File(filePath)
            if (pdfFile.exists()) {
                PDDocument.load(pdfFile).use { document ->
                    totalPages = document.numberOfPages
                    val pdfStripper = PDFTextStripper().apply {
                        startPage = currentPage
                        endPage = currentPage
                    }
                    pdfStripper.getText(document)
                }
            } else {
                "File not found: $filePath"
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
                    Text(text = pdfText)
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