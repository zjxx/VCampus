// File: kotlin/view/component/LocalPdfViewer.kt
package view.component

import androidx.compose.runtime.Composable

@Composable
fun LocalPdfViewer(filePath: String, onDismiss: () -> Unit) {
    PdfViewer(filePath = filePath, onDismiss = onDismiss)
}