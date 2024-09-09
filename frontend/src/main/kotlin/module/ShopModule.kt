package module

import com.google.gson.Gson
import com.google.gson.JsonParser
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
    private val onViewSuccess: (List<Merchandise>) -> Unit
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
            val itemResponse = responseJson["items"] as String
            val itemResponseJson = Gson().fromJson(itemResponse, MutableMap::class.java) as MutableMap<String, Any>

            if (!num.equals("0")) {
                for (i in 0 until num.toInt()) {

                    val itemIndex = itemResponseJson["item" + i.toString()] as String
                    println("item0: ${itemResponseJson["item0"]}")
                    val itemJson = Gson().fromJson(itemIndex, MutableMap::class.java) as MutableMap<String, Any>
                    println("itemJson: ${itemJson}")
                    println("itemResponseJson: ${itemResponseJson}")

                    var imageURL = itemJson["uuid"] as String
                    var tempImage = "http://47.99.141.236/img/" + imageURL + ".jpg"
                    var localPath = "src/main/temp/" + imageURL + ".jpg"
                    downloadImageIfNotExists(tempImage, localPath)

                    val item = Merchandise(
                        itemUuid = itemJson["uuid"] as String,
                        itemname = itemJson["name"] as String,
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
        val request = mapOf("" to String)
        nettyClient.sendRequest(request,"shop/enterStore") { response: String ->
            handleEnterShop(response)
        }
    }

    private fun handleEnterShop(response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        println("Response status: ${responseJson["status"]}")

        if (responseJson["status"] == "success") {
            val num = responseJson["length"] as String
            val itemResponse = responseJson["items"] as String
            val itemResponseJson = Gson().fromJson(itemResponse, MutableMap::class.java) as MutableMap<String, Any>

            if (!num.equals("0")) {
                for (i in 0 until num.toInt()) {

                    val itemIndex = itemResponseJson["item" + i.toString()] as String
                    println("item0: ${itemResponseJson["item0"]}")
                    val itemJson = Gson().fromJson(itemIndex, MutableMap::class.java) as MutableMap<String, Any>
                    println("itemJson: ${itemJson}")
                    println("itemResponseJson: ${itemResponseJson}")

                    var imageURL = itemJson["uuid"] as String
                    var tempImage = "http://47.99.141.236/img/" + imageURL + ".jpg"
                    var localPath = "src/main/temp/" + imageURL + ".jpg"
                    downloadImageIfNotExists(tempImage, localPath)

                    val item = Merchandise(
                        itemUuid = itemJson["uuid"] as String,
                        itemname = itemJson["name"] as String,
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
            DialogManager.showDialog(responseJson["message"] as String)
        }
    }

    //_______________________________________________________________________________________
    //添加购物车
    fun addItemToCart(uuid: String, quantity: String) {
        val request = mapOf("userId" to UserSession.userId.toString(), "itemId" to uuid, "quantity" to quantity)
        nettyClient.sendRequest(request,"shop/addItemToCart") { response: String ->
            handleAddItemToCart(response)
        }
    }

    private fun handleAddItemToCart (response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        println("Response status: ${responseJson["status"]}")
        if (responseJson["status"] == "success") {
            DialogManager.showDialog("添加成功")
        } else {
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    }

    //_______________________________________________________________________________________
    //从购物车移除
    fun removeItemFromCart(uuid: String, quantity: String) {
        val request = mapOf("userId" to UserSession.userId.toString(), "itemId" to uuid, "quantity" to quantity)
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
            val itemResponse = responseJson["items"] as String
            val itemResponseJson = Gson().fromJson(itemResponse, MutableMap::class.java) as MutableMap<String, Any>

            if (!num.equals("0")) {
                for (i in 0 until num.toInt()) {

                    val itemIndex = itemResponseJson["item" + i.toString()] as String
                    println("item0: ${itemResponseJson["item0"]}")
                    val itemJson = Gson().fromJson(itemIndex, MutableMap::class.java) as MutableMap<String, Any>
                    println("itemJson: ${itemJson}")
                    println("itemResponseJson: ${itemResponseJson}")

                    var imageURL = itemJson["uuid"] as String
                    var tempImage = "http://47.99.141.236/img/" + imageURL + ".jpg"
                    var localPath = "src/main/temp/" + imageURL + ".jpg"
                    downloadImageIfNotExists(tempImage, localPath)

                    val item = Merchandise(
                        itemUuid = itemJson["uuid"] as String,
                        itemname = itemJson["name"] as String,
                        price = itemJson["price"] as String,
                        imageRes = localPath,
                        barcode = itemJson["barcode"] as String,
                        stock = itemJson["stock"] as String,
                        salesVolume = itemJson["salesVolume"] as String,
                        description = itemJson["description"] as String,
                        quantity = itemJson["quantity"] as String
                    )
                    println("image: ${item.imageRes}")
                    tempItems.add(item)
                }
                onViewSuccess(tempItems)
            }
        } else {
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    }

    //_______________________________________________________________________________________
    //增加商品or修改商品
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
        val request = mapOf("" to String)
        nettyClient.sendRequest(request, "shop/getAllTransactions") { response: String ->
            handleGetAllTransactions(response)
        }
    }

    private fun handleGetAllTransactions (response: String) {
        println("Received response: $response")
        val responseJson = JsonParser.parseString(response).asJsonObject
        val status = responseJson.get("status").asString
        println("Response status: ${status}")

        if (status == "success") {
            val num = responseJson.get("length").asString
            val transactionsJson = responseJson.getAsJsonObject("transactions")
            //val transactionsJson = JsonParser.parseString(transactions).asJsonObject

            if (!num.equals("0")) {
                for (i in 0 until num.toInt()) {

                    val eachTransactionJson = transactionsJson.getAsJsonObject("transaction" + i.toString())

                    val transaction = StoreTransaction(
                        uuid = eachTransactionJson.get("uuid").asString,
                        itemUuid = eachTransactionJson.get("itemUuid").asString,
                        itemName = eachTransactionJson.get("itemName").asString,
                        itemPrice = eachTransactionJson.get("itemPrice").asString,
                        amount = eachTransactionJson.get("amount").asString,
                        cardNumber = eachTransactionJson.get("cardNumber").asString,
                        time = eachTransactionJson.get("time").asString,
                    )
                    //println("image: ${transaction.imageRes}")
                    tempTransactions.add(transaction)
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
        tempTransactions.clear()
        val request = mapOf("cardNumber" to cardNumber)
        nettyClient.sendRequest(request, "shop/getTransactionsByCardNumber") { response: String ->
            handleGetTransactionsByCardNumber(response)
        }
    }

    private fun handleGetTransactionsByCardNumber (response: String) {
        println("Received response: $response")
        val responseJson = JsonParser.parseString(response).asJsonObject
        val status = responseJson.get("status").asString
        println("Response status: ${status}")

        if (status == "success") {
            val num = responseJson.get("length").asString
            val transactions = responseJson.get("transactions").asString
            val transactionsJson = JsonParser.parseString(transactions).asJsonObject

            if (!num.equals("0")) {
                for (i in 0 until num.toInt()) {

                    val eachTransactionJson = transactionsJson.getAsJsonObject("transaction" + i.toString())

                    val transaction = StoreTransaction(
                        uuid = eachTransactionJson.get("uuid").asString,
                        itemUuid = eachTransactionJson.get("itemUuid").asString,
                        itemName = eachTransactionJson.get("itemName").asString,
                        itemPrice = eachTransactionJson.get("itemPrice").asString,
                        amount = eachTransactionJson.get("amount").asString,
                        time = eachTransactionJson.get("time").asString,
                    )
                    //println("image: ${transaction.imageRes}")
                    tempTransactions.add(transaction)
                }
                onGetTransactionsByCardNumberSuccess(tempTransactions)
            }
        } else {
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    }

    //_______________________________________________________________________________________
}

