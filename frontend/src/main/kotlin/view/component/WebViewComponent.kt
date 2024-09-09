import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState

@Composable
fun WebViewComponent(url:String) {
    val webViewState = rememberWebViewState(url)

    Column(modifier = Modifier.padding(16.dp)) {
        Text("WebView Display", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(8.dp))
        WebView(
            state = webViewState,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun WebViewDialog(url: String, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxSize(), // Match parent size
            shape = MaterialTheme.shapes.medium,
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                WebViewComponent(url = url)
            }
        }
    }
}