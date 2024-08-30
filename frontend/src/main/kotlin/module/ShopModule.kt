package module

import com.google.gson.Gson
import data.UserSession
import network.NettyClientProvider
import view.component.DialogManager

class ShopModule (
    private val onSearchSuccess: (String) -> Unit,
    private val onBuySuccess: (String) -> Unit
){
    private val nettyClient = NettyClientProvider.nettyClient

    fun shopSearch(merchandisename: String){
        val request = mapOf("role" to UserSession.role, "merchandisename" to merchandisename)
        nettyClient.sendRequest(request, "shop/search") { response: String ->
            handleResponseSearch(response)
        }
    }

    private fun handleResponseSearch(response: String){
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        println("Response message: ${responseJson["message"]}")
        println("Response status: ${responseJson["status"]}")
        if (responseJson["status"] == "success") {
            onSearchSuccess(responseJson["message"] as String)
        } else {
            DialogManager.showDialog("无相关商品")
        }
    }

    fun shopBuy(userId: String, merchandisename: String){
        val request = mapOf("role" to UserSession.role, "userId" to UserSession.userId, "merchandisename" to merchandisename)
        nettyClient.sendRequest(request, "shop/buy") { response: String ->
            handleResponseBuy(response)
        }
    }

    private fun handleResponseBuy(response: String){
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        println("Response message: ${responseJson["message"]}")
        println("Response status: ${responseJson["status"]}")
        if (responseJson["status"] == "success") {
            onBuySuccess(responseJson["message"] as String)
        } else {
            //DialogManager.showDialog("购买失败")
        }
    }

    fun shopAddToList(userId: String, merchandisename: String){
        val request = mapOf("role" to UserSession.role, "userId" to UserSession.userId, "merchandisename" to merchandisename)
        nettyClient.sendRequest(request, "shop/addtolist") { response: String ->
            handleResponseAddToList(response)
        }
    }

    private fun handleResponseAddToList(response: String){
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        println("Response message: ${responseJson["message"]}")
        println("Response status: ${responseJson["status"]}")
        if (responseJson["status"] == "success") {
            //onAddToListSuccess(responseJson["message"] as String)
        } else {
            //DialogManager.showDialog("添加失败")
        }
    }
}