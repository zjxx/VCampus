package view.component

import data.Book
import data.Merchandise

object GlobalState {
    var selectedBook: Book? = null
    var selectedItem: Merchandise? = null
}

var selectedItemList = (listOf<Merchandise>())