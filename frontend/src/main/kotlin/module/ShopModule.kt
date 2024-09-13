package module

import com.google.gson.Gson
import com.google.gson.JsonParser
import data.Merchandise
import data.StoreTransaction
import data.UserSession
import utils.NettyClientProvider
import utils.downloadImageIfNotExists
import view.component.DialogManager

/**
 * Module for managing shop-related operations.
 *
 * @property onSearchSuccess Callback function to be called on successful search.
 * @property onBuySuccess Callback function to be called on successful purchase.
 * @property onEnterSuccess Callback function to be called on successful shop entry.
 * @property onAddItemToCartSuccess Callback function to be called on successful addition to cart.
 * @property onRemoveItemFromCartSuccess Callback function to be called on successful removal from cart.
 * @property onShopAddToListSuccess Callback function to be called on successful addition or modification of shop item.
 * @property onGetAllTransactionsSuccess Callback function to be called on successful retrieval of all transactions.
 * @property onGetTransactionsByCardNumberSuccess Callback function to be called on successful retrieval of transactions by card number.
 * @property onViewSuccess Callback function to be called on successful view of items.
 */

class ShopModule (
    private val onSearchSuccess: (List<Merchandise>) -> Unit,
    private val onBuySuccess: (String) -> Unit,
    private val onEnterSuccess: (List<Merchandise>) -> Unit,
    private val onAddItemToCartSuccess: (String) -> Unit,
    private val onRemoveItemFromCartSuccesss: (String) -> Unit,
    private val onShopAddToListSuccess: (String) -> Unit,
    private val onGetAllTransactionsSuccess: (List<StoreTransaction>) -> Unit,
    private val onGetTransactionsByCardNumberSuccess: (List<StoreTransaction>) -> Unit,
    private val onViewSuccess: (List<Merchandise>) -> Unit,
    //private val onViewCartComplete: () -> Unit
    ){
    private val nettyClient = NettyClientProvider.nettyClient
    var tempItems = mutableListOf<Merchandise>()
    var tempTransactions = mutableListOf<StoreTransaction>()

//______________________________________________________________________________________
   //搜索商品

    /**
     * Searches for items in the shop.
     *
     * @param itemName The name of the item to search for.
     */
    fun shopSearch(itemName: String) {
        //tempItems.clear()
        val request = mapOf("itemname" to itemName)
        nettyClient.sendRequest(request, "shop/search") { response: String ->
            handleShopSearch(response)
        }
    }

    /**
     * Handles the response for shop search.
     *
     * @param response The response string from the server.
     */

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
    /**
     * Buys an item from the shop.
     *
     * @param uuid The UUID of the item.
     * @param amount The amount of the item to buy.
     * @param itemName The name of the item.
     */
    fun shopBuy(uuid: String, amount: String, itemName: String) {
        val request = mapOf("itemUuid" to uuid, "amount" to amount, "cardNumber" to UserSession.userId.toString(), "itemName" to itemName)
        nettyClient.sendRequest(request, "shop/buy") { response: String ->
            handleShopBuy(response)
        }
    }

    /**
     * Handles the response for buying an item.
     *
     * @param response The response string from the server.
     */

    private fun handleShopBuy(response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        println("Response status: ${responseJson["status"]}")
        val balance = responseJson["balance"] as String

        if (responseJson["status"] == "success") {
            onBuySuccess(balance.toString())
            DialogManager.showDialog("购买成功，校园卡余额: $balance")
        }
        else {
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    }

    //_______________________________________________________________________________________
    //QRCode支付
    /**
     * Buys an item from the shop using QR code.
     *
     * @param uuid The UUID of the item.
     * @param amount The amount of the item to buy.
     * @param itemName The name of the item.
     */

    fun shopQRBuy(uuid: String, amount: String, itemName: String) {
        val request = mapOf("itemUuid" to uuid, "amount" to amount, "cardNumber" to UserSession.userId.toString(), "itemName" to itemName)
        nettyClient.sendRequest(request, "shop/QRbuy") { response: String ->
            handleShopQRBuy(response)
        }
    }

    /**
     * Handles the response for buying an item using QR code.
     *
     * @param response The response string from the server.
     */
    private fun handleShopQRBuy(response: String) {
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
    //购物车购买
    /**
     * Buys items from the cart.
     *
     * @param itemList The list of items to buy.
     */
    fun shopCartBuy (itemList: List<Merchandise>) {
        val request = mapOf("userId" to UserSession.userId.toString(), "length" to (itemList.size).toString(), "items" to Gson().toJson(itemList))
        nettyClient.sendRequest(request, "shop/cartBuy") { response: String ->
            handleShopCartBuy(response)
        }
    }

    /**
     * Handles the response for buying items from the cart.
     *
     * @param response The response string from the server.
     */
    private fun handleShopCartBuy (response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        println("Response status: ${responseJson["status"]}")

        if (responseJson["status"] == "success") {
            DialogManager.showDialog("购买成功")
        } else {
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    }

    //_______________________________________________________________________________________
    //获取随机商品
    /**
     * Enters the shop and retrieves random items.
     */
    fun enterShop() {
        val request = mapOf("" to String)
        nettyClient.sendRequest(request,"shop/enterStore") { response: String ->
            handleEnterShop(response)
        }
    }


    /**
     * Handles the response for entering the shop.
     *
     * @param response The response string from the server.
     */
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

    /**
     * Adds an item to the cart.
     *
     * @param uuid The UUID of the item.
     * @param quantity The quantity of the item to add.
     */
    fun addItemToCart(uuid: String, quantity: String) {
        val request = mapOf("userId" to UserSession.userId.toString(), "itemId" to uuid, "quantity" to quantity)
        nettyClient.sendRequest(request,"shop/addItemToCart") { response: String ->
            handleAddItemToCart(response)
        }
    }
    /**
     * Handles the response for adding an item to the cart.
     *
     * @param response The response string from the server.
     */
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
    /**
     * Removes an item from the cart.
     *
     * @param uuid The UUID of the item.
     * @param quantity The quantity of the item to remove.
     */
    fun removeItemFromCart(uuid: String, quantity: String) {
        val request = mapOf("userId" to UserSession.userId.toString(), "itemId" to uuid, "quantity" to quantity)
        nettyClient.sendRequest(request,"shop/removeItemFromCart") { response: String ->
            handleRemoveItemFromCart(response)
        }
    }

    /**
     * Handles the response for removing an item from the cart.
     *
     * @param response The response string from the server.
     */
    private fun handleRemoveItemFromCart (response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        println("Response status: ${responseJson["status"]}")
        if (responseJson["status"] == "success") {
            onRemoveItemFromCartSuccesss
            DialogManager.showDialog("已移除")
        } else {
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    }

    //_______________________________________________________________________________________
    //查看购物车
    /**
     * Views the items in the cart.
     *
     * @param onViewCartComplete Callback function to be called on completion of viewing the cart.
     */
    fun viewCart(onViewCartComplete: () -> Unit) {
        tempItems.clear()
        val request = mapOf("userId" to UserSession.userId.toString())
        nettyClient.sendRequest(request,"shop/viewCart") { response: String ->
            handleViewCart(response)
            onViewCartComplete()
        }
    }

    /**
     * Handles the response for viewing the cart.
     *
     * @param response The response string from the server.
     */
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
    /**
     * Adds or modifies a shop item.
     *
     * @param request The request data.
     * @param type The type of the request.
     * @param filePath The file path of the item image.
     */
    fun shopAddToList (request: Any, type: String, filePath: String?) {
        filePath?.let {
            nettyClient.sendFile(request, type, it) { response: String ->
                handleShopAddToList(response)
            }
        }
    }

    /**
     * Handles the response for adding or modifying a shop item.
     *
     * @param response The response string from the server.
     */
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
    /**
     * Deletes a shop item.
     *
     * @param uuid The UUID of the item to delete.
     */
    fun shopDeleteItem (uuid: String) {
        val request = mapOf("uuid" to uuid)
        nettyClient.sendRequest(request,"shop/deleteItem") { response: String ->
            handleShopDeleteItem(response)
        }
    }

    /**
     * Handles the response for deleting a shop item.
     *
     * @param response The response string from the server.
     */
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
    /**
     * Retrieves all transactions (Admin).
     */
    fun getAllTransactions () {
        tempItems.clear()
        val request = mapOf("" to String)
        nettyClient.sendRequest(request, "shop/getAllTransactions") { response: String ->
            handleGetAllTransactions(response)
        }
    }

    /**
     * Handles the response for retrieving all transactions.
     *
     * @param response The response string from the server.
     */
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
            val reason = responseJson.get("reason").asString
            DialogManager.showDialog(reason)
        }
    }

    //_______________________________________________________________________________________
    //获取订单记录:User
    /**
     * Retrieves transactions by card number (User).
     *
     * @param cardNumber The card number to filter transactions.
     */
    fun getTransactionsByCardNumber (cardNumber: String) {
        tempTransactions.clear()
        val request = mapOf("cardNumber" to cardNumber)
        nettyClient.sendRequest(request, "shop/getTransactionsByCardNumber") { response: String ->
            handleGetTransactionsByCardNumber(response)
        }
    }

    /**
     * Handles the response for retrieving transactions by card number.
     *
     * @param response The response string from the server.
     */
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

