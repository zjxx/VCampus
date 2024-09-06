package module

import com.google.gson.Gson
import data.Merchandise
import data.StoreTransaction
import data.UserSession
import utils.NettyClientProvider
import utils.downloadImageIfNotExists
import view.component.DialogManager


class ShopModule (
    private val onSearchSuccess: (List<Merchandise>) -> Unit,
    private val onBuySuccess: (String) -> Unit,
    private val onEnterSuccess: (List<Merchandise>) -> Unit,
    private val onAddItemToCartSuccess: (String) -> Unit,
    private val onRemoveItemFromCartSuccesss: (String) -> Unit,
    private val onShopAddToListSuccess: (String) -> Unit,
    private val onGetAllTransactionsSuccess: (List<StoreTransaction>) -> Unit,
    private val onGetTransactionsByCardNumberSuccess: (List<StoreTransaction>) -> Unit,
){
    private val nettyClient = NettyClientProvider.nettyClient
    var tempItems = mutableListOf<Merchandise>()
    var tempTransactions = mutableListOf<StoreTransaction>()

//______________________________________________________________________________________
   //搜索商品
    fun shopSearch(itemName: String) {
        tempItems.clear()
        val request = mapOf("itemname" to itemName)
        nettyClient.sendRequest(request, "shop/search") { response: String ->
            handleShopSearch(response)
        }
    }

    private fun handleShopSearch(response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        println("Response status: ${responseJson["status"]}")
        if (responseJson["status"] == "success") {
            val num = responseJson["length"] as String
            if (!num.equals("0")) {
                for (i in 0 until num.toInt()) {
                    val itemIndex = responseJson["item" + i.toString()] as String
                    val itemJson = Gson().fromJson(itemIndex, MutableMap::class.java) as MutableMap<String, Any>
                    println("itemJson: ${itemJson}")

                    var imageURL = itemJson["uuid"] as String
                    var tempImage = "http://47.99.141.236/img/" + imageURL + ".jpg"
                    var localPath = "src/main/temp/" + imageURL + ".jpg"
                    downloadImageIfNotExists(tempImage, localPath)
                    val item = Merchandise(
                        itemUuid = itemJson["uuid"] as String,
                        itemname = itemJson["itemname"] as String,
                        price = itemJson["price"] as String,
                        imageRes = localPath,
                        barcode = itemJson["barcode"] as String,
                        stock = itemJson["stock"] as String,
                        salesVolume = itemJson["salesVolume"] as String,
                        description = itemJson["description"] as String
                    )
                    println("image: ${item.imageRes}")
                    tempItems.add(item)
                }
                onSearchSuccess(tempItems)
            }
        }
        else {
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    }

    //_______________________________________________________________________________________
    //购买商品
    fun shopBuy(uuid: String, amount: String, itemName: String) {
        val request = mapOf("itemUuid" to uuid, "amount" to amount, "cardNumber" to UserSession.userId.toString(), "itemName" to itemName)
        nettyClient.sendRequest(request, "shop/buy") { response: String ->
            handleShopBuy(response)
        }
    }

    private fun handleShopBuy(response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        println("Response status: ${responseJson["status"]}")

        if (responseJson["status"] == "success") {
            onBuySuccess("success")
            DialogManager.showDialog("购买成功")
        }
        else {
            onBuySuccess("fail")
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    }

    //_______________________________________________________________________________________
    //获取随机商品
    fun enterShop() {
        val request = ""
        nettyClient.sendRequest(request,"shop/entershop") { response: String ->
            handleEnterShop(response)
        }
    }

    private fun handleEnterShop(response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        println("Response status: ${responseJson["status"]}")
        if (responseJson["status"] == "success") {
            val num = responseJson["length"] as String
            if (!num.equals("0")) {
                for (i in 0 until num.toInt()) {
                    val itemIndex = responseJson["item" + i.toString()] as String
                    val itemJson = Gson().fromJson(itemIndex, MutableMap::class.java) as MutableMap<String, Any>
                    println("itemJson: ${itemJson}")

                    var imageURL = itemJson["uuid"] as String
                    var tempImage = "http://47.99.141.236/img/" + imageURL + ".jpg"
                    var localPath = "src/main/temp/" + imageURL + ".jpg"
                    downloadImageIfNotExists(tempImage, localPath)
                    val item = Merchandise(
                        itemUuid = itemJson["uuid"] as String,
                        itemname = itemJson["itemname"] as String,
                        price = itemJson["price"] as String,
                        imageRes = localPath,
                        barcode = itemJson["barcode"] as String,
                        stock = itemJson["stock"] as String,
                        salesVolume = itemJson["salesVolume"] as String,
                        description = itemJson["description"] as String
                    )
                    println("image: ${item.imageRes}")
                    tempItems.add(item)
                }
                onEnterSuccess(tempItems)
            }
        }
        else {
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    }

    //_______________________________________________________________________________________
    //添加购物车
    fun addItemToCart(uuid: String, cartUuid: String) {
        val request = mapOf("userId" to UserSession.userId.toString(), "itemId" to uuid, "uuid" to cartUuid)
        nettyClient.sendRequest(request,"shop/addItemToCart") { response: String ->
            handleAddItemToCart(response)
        }
    }

    private fun handleAddItemToCart (response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        println("Response status: ${responseJson["status"]}")
        if (responseJson["status"] == "success") {
            onAddItemToCartSuccess
        } else {
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    }

    //_______________________________________________________________________________________
    //从购物车移除
    fun removeItemFromCart(uuid: String, cartUuid: String) {
        val request = mapOf("userId" to UserSession.userId.toString(), "itemId" to uuid, "uuid" to cartUuid)
        nettyClient.sendRequest(request,"shop/removeItemFromCart") { response: String ->
            handleRemoveItemFromCart(response)
        }
    }

    private fun handleRemoveItemFromCart (response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        println("Response status: ${responseJson["status"]}")
        if (responseJson["status"] == "success") {
            onRemoveItemFromCartSuccesss
        } else {
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    }

    //_______________________________________________________________________________________
    //查看购物车
    fun viewCart() {
        val request = mapOf("userId" to UserSession.userId.toString())
        nettyClient.sendRequest(request,"shop/viewCart") { response: String ->
            handleViewCart(response)
        }
    }

    private fun handleViewCart (response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        println("Response status: ${responseJson["status"]}")
        if (responseJson["status"] == "success") {
            val num = responseJson["length"] as String
            if (!num.equals("0")) {
                for (i in 0 until num.toInt()) {
                    val itemIndex = responseJson["item" + i.toString()] as String
                    val itemJson = Gson().fromJson(itemIndex, MutableMap::class.java) as MutableMap<String, Any>
                    println("itemJson: ${itemJson}")

                    var imageURL = itemJson["uuid"] as String
                    var tempImage = "http://47.99.141.236/img/" + imageURL + ".jpg"
                    var localPath = "src/main/temp/" + imageURL + ".jpg"
                    downloadImageIfNotExists(tempImage, localPath)
                    val item = Merchandise(
                        itemUuid = itemJson["uuid"] as String,
                        itemname = itemJson["itemname"] as String,
                        price = itemJson["price"] as String,
                        imageRes = localPath,
                        barcode = itemJson["barcode"] as String,
                        stock = itemJson["stock"] as String,
                        salesVolume = itemJson["salesVolume"] as String,
                        description = itemJson["description"] as String
                    )
                    println("image: ${item.imageRes}")
                    tempItems.add(item)
                }
                onSearchSuccess(tempItems)
            }
        } else {
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    }

    //_______________________________________________________________________________________
    //增加商品or增加商品
    fun shopAddToList (request: Any, type: String, filePath: String?) {
        filePath?.let {
            nettyClient.sendFile(request, type, it) { response: String ->
                handleShopAddToList(response)
            }
        }
    }

    private fun handleShopAddToList (response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        println("Response status: ${responseJson["status"]}")
        val modifyResult: String

        if (responseJson["status"] == "success" ) {
            modifyResult = "success"
            DialogManager.showDialog("修改成功")
        }else {
            modifyResult = "failed"
            DialogManager.showDialog(responseJson["reason"] as String)
        }
        onShopAddToListSuccess(modifyResult)
    }

    //_______________________________________________________________________________________
    //删除商品
    fun shopDeleteItem (uuid: String) {
        val request = mapOf("uuid" to uuid)
        nettyClient.sendRequest(request,"shop/deleteItem") { response: String ->
            handleShopDeleteItem(response)
        }
    }

    private fun handleShopDeleteItem (response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        println("Response status: ${responseJson["status"]}")

        if (responseJson["status"] == "success") {
            DialogManager.showDialog("已删除")
        } else {
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    }

    //_______________________________________________________________________________________
    //获取所有交易记录:Admin
    fun getAllTransactions () {
        tempItems.clear()
        val request = ""
        nettyClient.sendRequest(request, "shop/getAllTransactions") { response: String ->
            handleGetAllTransactions(response)
        }
    }

    private fun handleGetAllTransactions (response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        println("Response status: ${responseJson["status"]}")

        if (responseJson["status"] == "success") {
            val num = responseJson["length"] as String
            if (!num.equals("0")) {
                for (i in 0 until num.toInt()) {

                    val temp = StoreTransaction(
                        uuid = responseJson["uuid"] as String,
                        itemUuid = responseJson["itemUuid"] as String,
                        itemName = responseJson["itemName"] as String,
                        itemPrice = responseJson["itemPrice"] as String,
                        amount = responseJson["barcode"] as String,
                        cardNumber = responseJson["stock"] as String,
                        time = responseJson["salesVolume"] as String,
                    )
                    tempTransactions.add(temp)
                }
                onGetAllTransactionsSuccess(tempTransactions)
            }
        } else {
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    }

    //_______________________________________________________________________________________
    //获取订单记录:User
    fun getTransactionsByCardNumber (cardNumber: String) {
        tempItems.clear()
        val request = mapOf("cardNumber" to cardNumber)
        nettyClient.sendRequest(request, "shop/getTransactionsByCardNumber") { response: String ->
            handleGetTransactionsByCardNumber(response)
        }
    }

    private fun handleGetTransactionsByCardNumber (response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        println("Response status: ${responseJson["status"]}")

        if (responseJson["status"] == "success") {
            val num = responseJson["length"] as String
            if (!num.equals("0")) {
                for (i in 0 until num.toInt()) {

                    val temp = StoreTransaction(
                        uuid = responseJson["uuid"] as String,
                        itemUuid = responseJson["itemUuid"] as String,
                        itemName = responseJson["itemName"] as String,
                        itemPrice = responseJson["itemPrice"] as String,
                        amount = responseJson["barcode"] as String,
                        cardNumber = responseJson["stock"] as String,
                        time = responseJson["salesVolume"] as String,
                    )
                    tempTransactions.add(temp)
                }
                onGetTransactionsByCardNumberSuccess(tempTransactions)
            }
        } else {
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    }

    //_______________________________________________________________________________________
}

