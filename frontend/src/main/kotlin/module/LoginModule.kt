// 修改 LoginModule.kt
package module

import com.google.gson.Gson
import network.NettyClientProvider

class LoginModule(
    private val onLoginFailed: (String) -> Unit,
    private val onLoginSuccess: () -> Unit
) {
    private val nettyClient = NettyClientProvider.nettyClient

    fun onLoginClick(username: String, password: String) {
        val request = mapOf("username" to username, "password" to password)
        nettyClient.setRole("student")
        nettyClient.sendRequest(request, "login") { response: String ->
            handleResponse(response)
        }
    }

    private fun handleResponse(response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        println("Response message: ${responseJson["message"]}")
        println("Response status: ${responseJson["status"]}")
        if (responseJson["status"] == "success") {
            onLoginSuccess()
        } else {
            onLoginFailed(responseJson["message"] as String)
        }
    }
}