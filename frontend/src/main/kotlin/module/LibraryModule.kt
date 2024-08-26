package module

import com.google.gson.Gson
import network.NettyClientProvider
import view.component.DialogManager

class LibraryModule (
    private val onSearchSuccess: (String) -> Unit,
    private val onCheckSuccess: (String) -> Unit
) {
    private val nettyClient = NettyClientProvider.nettyClient

    fun libSearch(bookname: String) {
        val request = mapOf("bookname" to bookname)
        nettyClient.sendRequest(request, "lib/search") { response: String ->
            handleResponseSearch(response)
        }
    }

    private fun handleResponseSearch(response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        println("Response message: ${responseJson["message"]}")
        println("Response status: ${responseJson["status"]}")
        if (responseJson["status"] == "success") {
            onSearchSuccess(responseJson["message"] as String)
        } else {
            DialogManager.showDialog("搜索失败")
        }
    }

    fun libCheck(username: String) {
        val request = mapOf("username" to username)
        nettyClient.sendRequest(request, "lib/check") { response: String ->
            handleResponseCheck(response)
        }
    }

    private fun handleResponseCheck(response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        println("Response message: ${responseJson["message"]}")
        println("Response status: ${responseJson["status"]}")
        if (responseJson["status"] == "success") {
            onCheckSuccess(responseJson["message"] as String)
        } else {
            DialogManager.showDialog("查看失败")
        }
    }
}