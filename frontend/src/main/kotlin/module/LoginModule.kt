package module

import com.google.gson.Gson
import data.UserSession
import utils.NettyClientProvider
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
        if (responseJson["status"] == "success"||responseJson["status"] == "noemail") {
            nettyClient.setRole(responseJson["role"] as String)
            UserSession.userId = responseJson["userId"] as String
            UserSession.role = responseJson["role"] as String
            //UserSession.userName = responseJson["userName"] as String
            onLoginSuccess()
        } else {
            DialogManager.showDialog(responseJson["message"] as String)
        }
    }

    fun sendVerificationCode(email: String, userId: String) {
        val request = mapOf("email" to email, "userId" to userId)
        nettyClient.sendRequest(request, "sendCode") { response: String ->
            handleResponseSendVerificationCode(response)
        }
    }

    private fun handleResponseSendVerificationCode(response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        if (responseJson["status"] == "success") {
           // UserSession.code = responseJson["Code"] as String
            DialogManager.showDialog(responseJson["message"] as String)
        } else {
            DialogManager.showDialog(responseJson["message"] as String)
        }
    }

    fun verifyCode(inputCode: String): Boolean {
        return inputCode == UserSession.code
    }

    fun updatePassword(userId: String, newPassword: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val request = mapOf("userId" to userId, "newPassword" to newPassword)
        nettyClient.sendRequest(request, "updatePassword") { response: String ->
            handleResponseUpdatePassword(response, onSuccess, onError)
        }
    }

    private fun handleResponseUpdatePassword(response: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        if (responseJson["status"] == "success") {
            onSuccess()
        } else {
            onError(responseJson["message"] as String)
        }
    }
}