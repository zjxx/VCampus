import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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