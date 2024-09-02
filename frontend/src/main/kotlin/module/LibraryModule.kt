package module

import com.google.gson.Gson
import data.Book
import data.UserSession
import network.NettyClientProvider
import view.component.DialogManager

class LibraryModule (
    private val onSearchSuccess: (List<Book>) -> Unit,
    private val onCheckSuccess: (String) -> Unit,
    private val onImageFetchSuccess: (String) -> Unit

) {
    private val nettyClient = NettyClientProvider.nettyClient
    var tempBooks = mutableListOf<Book>()

    fun libSearch(bookname: String, role: String) {
        val request = mapOf("role" to UserSession.role, "bookname" to bookname)
        nettyClient.sendRequest(request, "lib/search") { response: String ->
            handleResponseSearch(response)
        }
    }

    private fun handleResponseSearch(response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        //println("Response message: ${responseJson["message"]}")
        println("Response status: ${responseJson["status"]}")
        if (responseJson["status"] == "success") {
            val num = responseJson["number"] as String
            if(!num.equals("0")) {
                for (i in 0 until num.toInt()) {
                    val bookindex = responseJson["b" + i.toString()] as String
                    val bookjson = Gson().fromJson(bookindex, MutableMap::class.java) as MutableMap<String, Any>
                    println("Book name: ${bookjson["bookName"]}")
                    println("Bookjson: ${bookjson}")

                    // Update tempbook with received data
                    var imageUrl = bookjson["ISBN"] as String
                    imageUrl = imageUrl.replace(Regex("[^0-9]"), "")
                    val temp = Book(
                        coverImage = "http://47.99.141.236/img/" + imageUrl + ".jpg", // Assuming coverImageRes is not provided in the response
                        bookname = bookjson["bookName"] as String,
                        author = bookjson["author"] as String,
                        publisher = bookjson["publisher"] as String,
                        publishDate = bookjson["publishDate"] as String,
                        language = bookjson["language"] as String,
                        isbn = bookjson["ISBN"] as String,
                        description = bookjson["description"] as String,
                        kind = bookjson["Kind"] as String,
                        quantity = (bookjson["quantity"] as Double).toInt(),
                        valid = (bookjson["Valid_Quantity"] as Double).toInt()
                    )
                    println("image: ${temp.coverImage}")
                    tempBooks.add(temp)
                }
                onSearchSuccess(tempBooks)
            }
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
            val booknum=responseJson["number"] as Int
            for (i in 1..booknum){
                val res = responseJson[i.toString()] as String
                val bookjson = Gson().fromJson(res, MutableMap::class.java) as MutableMap<String, Any>
                println("Book name: ${bookjson["bookname"]}")

            }
            onCheckSuccess(responseJson["message"] as String)
        } else {
            DialogManager.showDialog("请求失败")
        }
    }

    fun libAddToLits(userId: String, bookname: String) {//借书
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

    fun libReturnBook(userId: String, bookname: String) {//还书
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

    fun fetchImageUrl(input: String) {
        val request = mapOf("action" to "fetchImageUrl", "bookname"  to input)
        nettyClient.sendRequest(request, "lib/fetchImageUrl") { response: String ->
            handleResponseImageFetch(response)
        }
    }

    private fun handleResponseImageFetch(response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        if (responseJson["status"] == "success") {
                val num = responseJson["number"] as String
                if(!num.equals("0")) {
                    for (i in 0 until num.toInt()) {
                        val bookindex = responseJson["b" + i.toString()] as String
                        val bookjson = Gson().fromJson(bookindex, MutableMap::class.java) as MutableMap<String, Any>
                        println("Book name: ${bookjson["bookname"]}")
                        var imageUrl = bookjson["ISBN"] as String
                        //只保留imageUrl的数字部分
                        imageUrl = imageUrl.replace(Regex("[^0-9]"), "")
                        var image = "http://47.99.141.236/img/" + imageUrl + ".jpg"
                        onImageFetchSuccess(image)
                    }
                }
        }
    }
}