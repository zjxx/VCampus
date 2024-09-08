import java.net.HttpURLConnection
import java.net.URL
import java.io.OutputStreamWriter
import java.io.BufferedReader
import java.io.InputStreamReader
import java.security.MessageDigest

fun generateMD5Signature(merchantNum: String, orderNo: String, amount: Double, notifyUrl: String, secretKey: String): String {
    val stringToSign = "$merchantNum$orderNo$amount$notifyUrl$secretKey"
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(stringToSign.toByteArray())
    return digest.joinToString("") { "%02x".format(it) }
}

fun sendPostRequest(amount: Double): String {
    val merchantNum = "426139470564573184"
    val orderNo = "test"
    val notifyUrl = "https://www.zhifux.com/"
    val payType = "alipay"
    val returnUrl = "https://www.zhifux.com/"
    val secretKey = "444369581c323be673a53bd2d1b4fea9"
    val sign = generateMD5Signature(merchantNum, orderNo, amount, notifyUrl, secretKey)

    val params = "merchantNum=$merchantNum&orderNo=$orderNo&amount=$amount&notifyUrl=$notifyUrl&payType=$payType&sign=$sign&returnUrl=$returnUrl"

    val url = URL("https://api-38jxu2b2wr9d.zhifu.fm.it88168.com/api/startOrder")

    with(url.openConnection() as HttpURLConnection) {
        requestMethod = "POST"
        doOutput = true
        setRequestProperty("Content-Type", "application/x-www-form-urlencoded")

        OutputStreamWriter(outputStream).use { writer ->
            writer.write(params)
            writer.flush()
        }

        val response = StringBuilder()
        BufferedReader(InputStreamReader(inputStream)).use { reader ->
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
        }
        return response.toString()
    }
}