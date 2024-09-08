import androidx.compose.runtime.Composable
import com.google.gson.JsonParser

@Composable
fun PaymentWebViewDialog(amount: Double, onDismiss: (String?) -> Unit) {
    val test = sendPostRequest(amount)
    val jsonObject = JsonParser.parseString(test).asJsonObject
    val dataObject = jsonObject.getAsJsonObject("data")
    val payUrl = dataObject.get("payUrl").asString
    println("Pay URL: $payUrl")

    ShowWebViewDialog(
        url = payUrl,
        targetUrl = "https://www.zhifux.com/",
        onDismiss = { result ->
            onDismiss(result)
        }
    )
}