
package data

data class Book(
    val coverImage: String,

    val bookname: String,
    val author: String,

    val publisher: String,
    val publishDate: String,

    val language: String,

    val isbn: String,
    val description: String,

    val kind: String,
    val quantity: Int,
    val valid: Int
) {

}