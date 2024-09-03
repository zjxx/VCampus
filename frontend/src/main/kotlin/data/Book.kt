
package data

data class Book(
    val coverImage: String = "",

    val bookname: String = "",
    val author: String = "",

    val publisher: String = "",
    val publishDate: String = "",

    val language: String = "",

    val isbn: String = "",
    val description: String = "",

    val kind: String = "",
    val quantity: Int = 0,
    val valid: Int = 0,

    val borrow_date: String = "",
    val return_date: String = "",

    val condition: String = ""
) {

}