package module

import com.google.gson.Gson
import data.Book
import data.UserSession
import utils.NettyClientProvider
import utils.downloadImageIfNotExists
import view.component.DialogManager

/**
 * Module for handling library-related operations.
 *
 * @property onSearchSuccess Callback function to be called on successful search.
 * @property onCheckSuccess Callback function to be called on successful check.
 * @property onImageFetchSuccess Callback function to be called on successful image fetch.
 * @property onAddToListSuccess Callback function to be called on successful addition to list.
 * @property onIdCheckSuccess Callback function to be called on successful ID check.
 * @property onBookModifySuccess Callback function to be called on successful book modification.
 * @property onArticleSearch Callback function to be called on successful article search.
 */
class LibraryModule (
    private val onSearchSuccess: (List<Book>) -> Unit,
    private val onCheckSuccess: (List<Book>) -> Unit,
    private val onImageFetchSuccess: (String) -> Unit,
    private val onAddToListSuccess: (String) -> Unit,
    private val onIdCheckSuccess: (List<Book>) -> Unit,
    private val onBookModifySuccess: (String) -> Unit,
    private val onArticleSearch: (List<Book>) -> Unit,
) {
    private val nettyClient = NettyClientProvider.nettyClient
    var tempBooks = mutableListOf<Book>()
    var searchIdResult = mutableListOf<String>()
    //var filePath by remember { mutableStateOf<String?>(null) }

    //__________________________________________________________________________________________

    /**
     * Searches for books in the library.
     *
     * @param bookname The name of the book to search for.
     * @param flag A flag to distinguish between book name and ISBN.
     */
    fun libSearch(bookname: String, flag: String) {
        tempBooks.clear()
        val request = mapOf("role" to UserSession.role, "bookname" to bookname, "flag" to flag)//加上一个flag区分是否时bookName和ISBN
        nettyClient.sendRequest(request, "lib/search") { response: String ->
            handleResponseSearch(response)
        }
    }

    /**
     * Handles the response for the book search request.
     *
     * @param response The response string from the server.
     */
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
                    var tempImage = "http://47.99.141.236/img/" + imageUrl + ".png"
                    var localPath = "src/main/temp/" + imageUrl + ".png"
                    downloadImageIfNotExists(tempImage, localPath)
                    val temp = Book(
                        coverImage = localPath, // Assuming coverImageRes is not provided in the response
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
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    }

    /**
     * Searches for articles in the library.
     *
     * @param articleName The name of the article to search for.
     */
    fun libArticleSearch(articleName: String) {//文章搜索
        val request = mapOf("role" to UserSession.role, "articleName" to articleName)
        nettyClient.sendRequest(request, "lib/articleSearch") { response: String ->
            handleResponseArticleSearch(response)
        }
    }

    /**
     * Handles the response for the article search request.
     *
     * @param response The response string from the server.
     */
    private fun handleResponseArticleSearch(response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        println("Response status: ${responseJson["status"]}")
        if (responseJson["status"] == "success") {
            val num = responseJson["number"] as String
            if(!num.equals("0")) {
                for (i in 0 until num.toInt()) {
                    val bookindex = responseJson["a" + i.toString()] as String
                    val bookjson = Gson().fromJson(bookindex, MutableMap::class.java) as MutableMap<String, Any>
                    println("Book name: ${bookjson["bookName"]}")
                    println("Bookjson: ${bookjson}")

                    val temp = Book(
                        bookname = bookjson["title"] as String,
                        isbn = bookjson["ISBN"] as String,
                        publisher = bookjson["publisher"] as String,
                        publishDate = bookjson["publishDate"] as String,
                    )
                    println("image: ${temp.coverImage}")
                    tempBooks.add(temp)
                }
                onArticleSearch(tempBooks)
            }
        } else {
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    }

    //______________________________________________________________________________________________
    //check

    /**
     * Checks the library records for a user.
     *
     * @param userId The ID of the user to check records for.
     */
    fun libCheck(userId: String) {
        tempBooks.clear()
        val request = mapOf("role" to UserSession.role, "userId" to userId)
        nettyClient.sendRequest(request, "lib/check") { response: String ->
            handleResponseCheck(response)
        }
    }

    /**
     * Handles the response for the library check request.
     *
     * @param response The response string from the server.
     */
    private fun handleResponseCheck(response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        println("Response status: ${responseJson["status"]}")

        if (responseJson["status"] == "success" ) {

            val borrowingnum = responseJson["borrowing_number"] as String
            val borrowednum = responseJson["haveBorrowed_number"] as String

            if (borrowingnum.toInt() == 0 && borrowednum.toInt() == 0){
                onCheckSuccess(tempBooks)
            }

            if (borrowingnum.toInt() != 0){
                for (i in 0 until borrowingnum.toInt()){
                    val res = responseJson["borrowing"+i.toString()] as String
                    val bookjson = Gson().fromJson(res, MutableMap::class.java) as MutableMap<String, Any>
                    println("Book name: ${bookjson["bookName"]}")

                    val temp1 = Book(
                        condition = "borrowing",
                        bookname = bookjson["bookName"] as String,
                        isbn = bookjson["ISBN"] as String,
                        borrow_date = bookjson["borrow_date"] as String,
                        return_date = bookjson["return_date"] as String
                    )
                    tempBooks.add(temp1)
                }

            }

            if (borrowednum.toInt() != 0){
                for (i in 0 until borrowednum.toInt()){
                    val res = responseJson["haveBorrowed"+(i).toString()] as String
                    val bookjson = Gson().fromJson(res, MutableMap::class.java) as MutableMap<String, Any>
                    println("Book name: ${bookjson["bookName"]}")

                    val temp2 = Book(
                        condition = "haveBorrowed",
                        bookname = bookjson["bookName"] as String,
                        isbn = bookjson["ISBN"] as String,
                        borrow_date = bookjson["borrow_date"] as String,
                        return_date = bookjson["return_date"] as String
                    )
                    tempBooks.add(temp2)
                }
            }

            onCheckSuccess(tempBooks)

        } else {
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    }
    //__________________________________________________________________________________________
    //借书发送信息

    /**
     * Adds a book to the user's list.
     *
     * @param isbn The ISBN of the book to add.
     */
    fun libAddToLits(isbn: String) {//借书
        val request = mapOf("role" to UserSession.role, "userId" to UserSession.userId, "ISBN" to isbn)
        nettyClient.sendRequest(request, "lib/addtolist") { response: String ->
            handleResponseAddToList(response)
        }
    }

    /**
     * Handles the response for the add to list request.
     *
     * @param response The response string from the server.
     */
    //借书信息处理
    private fun handleResponseAddToList(response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        println("Response status: ${responseJson["status"]}")
        if (responseJson["status"] == "success") {
            onAddToListSuccess("借阅成功")
            DialogManager.showDialog("借阅成功")
        } else {
            onAddToListSuccess("借阅失败")
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    }

    //__________________________________________________________________________________________
    //还书发送信息
    /**
     * Returns a book.
     *
     * @param userId The ID of the user returning the book.
     * @param isbn The ISBN of the book to return.
     */
    fun libReturnBook(userId: String, isbn: String) {//还书
        val request = mapOf("role" to UserSession.role, "userId" to UserSession.userId, "ISBN" to isbn)
        nettyClient.sendRequest(request, "lib/returnbook") { response: String ->
            handleResponseReturnBook(response)
        }
    }

    /**
     * Handles the response for the return book request.
     *
     * @param response The response string from the server.
     */
    //还书接收信息
    private fun handleResponseReturnBook(response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        println("Response message: ${responseJson["message"]}")
        println("Response status: ${responseJson["status"]}")
        if (responseJson["status"] == "success") {
            DialogManager.showDialog("归还成功")
        } else {
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    }

    //__________________________________________________________________________________________
    //续借发送信息
    /**
     * Renews a book.
     *
     * @param userId The ID of the user renewing the book.
     * @param isbn The ISBN of the book to renew.
     */
    fun libRenewBook(userId: String, isbn: String) {
        val request = mapOf("role" to UserSession.role, "userId" to UserSession.userId, "ISBN" to isbn)
        nettyClient.sendRequest(request, "lib/renewbook") { response: String ->
            handleResponseRenewBook(response)
        }
    }

    /**
     * Handles the response for the renew book request.
     *
     * @param response The response string from the server.
     */
    //续借接收信息
    private fun handleResponseRenewBook(response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        println("Response message: ${responseJson["message"]}")
        println("Response status: ${responseJson["status"]}")
        if (responseJson["status"] == "success") {
            DialogManager.showDialog("续借成功")
        } else {
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    }

    //__________________________________________________________________________________________
    //id搜索
    /**
     * Checks the library records for a user by ID.
     *
     * @param searchId The ID to search for.
     */
    fun libIdCheck(searchId: String) {
        tempBooks.clear()
        val request = mapOf("role" to UserSession.role, "userId" to UserSession.userId, "searchId" to searchId)
        nettyClient.sendRequest(request, "lib/viewUserBorrowRecord") { response: String ->
            handleResponseIdCheck(response)
        }
    }

    /**
     * Handles the response for the ID check request.
     *
     * @param response The response string from the server.
     */
    fun handleResponseIdCheck(response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        println("Response status: ${responseJson["status"]}")

        if (responseJson["status"] == "success" ) {

            val borrowingnum = responseJson["borrowing_number"] as String
            val borrowednum = responseJson["haveBorrowed_number"] as String

            if (borrowingnum.toInt() == 0 && borrowednum.toInt() == 0){
                onCheckSuccess(tempBooks)
            }

            if (borrowingnum.toInt() != 0){
                for (i in 0 until borrowingnum.toInt()){
                    println("borrowing$i: ${responseJson["borrowing"+i.toString()]}")
                    val res = responseJson["borrowing"+i.toString()] as String
                    val bookjson = Gson().fromJson(res, MutableMap::class.java) as MutableMap<String, Any>
                    println("Book name: ${bookjson["bookName"]}")

                    val temp1 = Book(
                        condition = "borrowing",
                        bookname = bookjson["bookName"] as String,
                        isbn = bookjson["ISBN"] as String,
                        borrow_date = bookjson["borrow_date"] as String,
                        return_date = bookjson["return_date"] as String,
                        userId = bookjson["readerId"] as String
                    )
                    tempBooks.add(temp1)
                }

            }

            if (borrowednum.toInt() != 0){
                for (i in 0 until borrowednum.toInt()){
                    val res = responseJson["haveBorrowed"+(i).toString()] as String
                    val bookjson = Gson().fromJson(res, MutableMap::class.java) as MutableMap<String, Any>
                    println("Book name: ${bookjson["bookName"]}")

                    val temp2 = Book(
                        condition = "haveBorrowed",
                        bookname = bookjson["bookName"] as String,
                        isbn = bookjson["ISBN"] as String,
                        borrow_date = bookjson["borrow_date"] as String,
                        return_date = bookjson["return_date"] as String,
                        userId = bookjson["readerId"] as String
                    )
                    tempBooks.add(temp2)
                }
            }
            onIdCheckSuccess(tempBooks)

        } else {
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    }

    //______________________________________________________________________________________
    //删除请求
    /**
     * Deletes a book from the library.
     *
     * @param isbn The ISBN of the book to delete.
     */
    fun libDeleteBook(isbn: String) {
        val request = mapOf("userId" to (UserSession.userId).toString(), "role" to UserSession.role, "ISBN" to isbn)
        nettyClient.sendRequest(request, "lib/delete") { response: String ->
            handleResponseDelete(response)
        }
    }

    /**
     * Handles the response for the delete book request.
     *
     * @param response The response string from the server.
     */
    private fun handleResponseDelete(response: String) {
        println("Received response: $response")
        val responseJson = Gson().fromJson(response, MutableMap::class.java) as MutableMap<String, Any>
        println("Response status: ${responseJson["status"]}")
        if (responseJson["status"] == "success") {
            DialogManager.showDialog("删除成功")
        } else {
            DialogManager.showDialog(responseJson["reason"] as String)
        }
    }

    //——————————————————————————————————————————————————————————————————————————————————————
    //修改请求
    /**
     * Modifies a book in the library.
     *
     * @param request The request data for the modification.
     * @param type The type of modification.
     * @param filePath The file path for the modification.
     */
    fun bookModify(request: Any, type: String, filePath: String?) {
        filePath?.let {
            nettyClient.sendFile(request, type, it) { response: String ->
                handleBookModifyRespose(response)
            }
        }
    }

    /**
     * Handles the response for the book modification request.
     *
     * @param response The response string from the server.
     */
    fun handleBookModifyRespose(response: String) {
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
        onBookModifySuccess(modifyResult)
    }

//__________________________________________________________________________________________

    /**
     * Fetches the image URL for a book.
     *
     * @param input The input data for fetching the image URL.
     */
    fun fetchImageUrl(input: String) {
        val request = mapOf("action" to "fetchImageUrl", "bookname"  to input)
        nettyClient.sendRequest(request, "lib/fetchImageUrl") { response: String ->
            handleResponseImageFetch(response)
        }
    }
    /**
     * Handles the response for the image fetch request.
     *
     * @param response The response string from the server.
     */
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
                        var image = "http://47.99.141.236/img/" + imageUrl + ".png"
                        onImageFetchSuccess(image)
                    }
                }
        }
    }
}

