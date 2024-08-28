package module

import com.google.gson.Gson
import data.UserSession
import network.NettyClientProvider
import view.component.DialogManager

class LibraryModule (
    private val onSearchSuccess: (String) -> Unit,
    private val onCheckSuccess: (String) -> Unit
) {
    private val nettyClient = NettyClientProvider.nettyClient

    fun libSearch(bookname: String) {
        val request = mapOf("role" to UserSession.role, "bookname" to bookname)
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

    fun libCheck(userId: String) {
        val request = mapOf("role" to UserSession.role, "userId" to UserSession.userId)
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
            DialogManager.showDialog("请求失败")
        }
    }

    fun libAddToList(userId: String, bookname: String) {
        val request = mapOf("role" to UserSession.role, "userId" to UserSession.userId, "bookname" to bookname)
        nettyClient.sendRequest(request, "lib/addtolist") { response: String ->
            handleResponseAddToList(response)
        }
    }

    private fun handleResponseAddToList(response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        println("Response message: ${responseJson["message"]}")
        println("Response status: ${responseJson["status"]}")
        if (responseJson["status"] == "success") {
            DialogManager.showDialog("办理成功")
        } else {
            DialogManager.showDialog("办理失败")
        }
    }

    fun libReturnBook(userId: String, bookname: String) {
        val request = mapOf("role" to UserSession.role, "userId" to UserSession.userId, "bookname" to bookname)
        nettyClient.sendRequest(request, "lib/returnbook") { response: String ->
            handleResponseReturnBook(response)
        }
    }

    private fun handleResponseReturnBook(response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        println("Response message: ${responseJson["message"]}")
        println("Response status: ${responseJson["status"]}")
        if (responseJson["status"] == "success") {
            DialogManager.showDialog("归还成功")
        } else {
            DialogManager.showDialog("归还失败")
        }
    }

    fun libRenewBook(userId: String, bookname: String) {
        val request = mapOf("role" to UserSession.role, "userId" to UserSession.userId, "bookname" to bookname)
        nettyClient.sendRequest(request, "lib/renewbook") { response: String ->
            handleResponseRenewBook(response)
        }
    }

    private fun handleResponseRenewBook(response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        println("Response message: ${responseJson["message"]}")
        println("Response status: ${responseJson["status"]}")
        if (responseJson["status"] == "success") {
            DialogManager.showDialog("续借成功")
        } else {
            DialogManager.showDialog("续借失败")
        }
    }
}