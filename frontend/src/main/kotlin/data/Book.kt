
package data

data class Book(
    val coverImageRes: Int,

    val title: String,
    val author: String,

    val kind: String,
    val language: String,

    val publisher: String,
    val publishDate: String,

    val description: String,
    val borrowInfo: String,

    val isbn: String,
    val bookId: String
) {

}