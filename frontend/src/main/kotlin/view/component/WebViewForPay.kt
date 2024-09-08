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
fun WebViewDialog(url: String, targetUrl: String, onDismiss: (String?) -> Unit) {
    val webViewState = rememberWebViewState(url)
    var previousUrl by remember { mutableStateOf<String?>(null) }
    var result by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(webViewState.lastLoadedUrl) {
        val currentUrl = webViewState.lastLoadedUrl
        if (currentUrl != null && currentUrl != previousUrl) {
            if (currentUrl.contains(targetUrl)) {
                result = "success"
                onDismiss(result)
            }
            previousUrl = currentUrl
        }
    }

    Dialog(onDismissRequest = { onDismiss(result) }) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = MaterialTheme.shapes.medium,
            elevation = 24.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("WebView Display", style = MaterialTheme.typography.h6)
                    IconButton(onClick = { onDismiss(result) }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                WebView(
                    state = webViewState,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun ShowWebViewDialog(url: String, targetUrl: String,onDismiss: (String?) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf<String?>(null) }

    if (showDialog) {
        WebViewDialog(url = url, targetUrl = targetUrl, onDismiss = {
            result = it
            showDialog = false
            onDismiss(result)
        })
    }

    Button(onClick = { showDialog = true }) {
        Text("支付")
    }

    result?.let {
        Text("Result: $it")
    }
}