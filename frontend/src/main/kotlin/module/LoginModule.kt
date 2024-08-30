// 修改 LoginModule.kt
package module

import com.google.gson.Gson
import data.UserSession
import network.NettyClientProvider
import view.component.DialogManager

class LoginModule(
    private val onLoginSuccess: () -> Unit
) {
    private val nettyClient = NettyClientProvider.nettyClient

    fun onLoginClick(username: String, password: String) {
        val request = mapOf("username" to username, "password" to password)
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
            UserSession.role = responseJson["role"] as String
            UserSession.userId = responseJson["userId"] as String
            onLoginSuccess()
        } else {
            DialogManager.showDialog(responseJson["message"] as String)
        }
    }
}