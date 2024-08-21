// File: kotlin/module/LoginModule.kt
package module

import network.NettyClientProvider

class LoginModule {
    private val nettyClient = NettyClientProvider.nettyClient

    fun onLoginClick(username: String, password: String) {
        val request = mapOf("username" to username, "password" to password,"type" to "login")
        nettyClient.sendRequest(request) { response ->
            handleResponse(response)
        }
    }

    private fun handleResponse(response: String) {
        println("Received response: $response")
        // 处理响应
    }
}