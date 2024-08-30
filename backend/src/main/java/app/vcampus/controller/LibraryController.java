package app.vcampus.controller;

import app.vcampus.domain.Book;
import app.vcampus.interfaces.BookSearchingRequest;
import app.vcampus.utils.DataBase;
import app.vcampus.utils.DataBaseManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;

public class LibraryController {
    private final Gson gson = new Gson();

    //前端输入role和bookName，后端返回相应的书籍信息
    public String searchBookInfo(String jsonData) {
        //解析JSON数据
        BookSearchingRequest request = gson.fromJson(jsonData, BookSearchingRequest.class);
        JsonObject data = new JsonObject();
        //判断用户身份,如果是学生，则返回所有同名书籍信息
        if (request.getRole().equals("student")){
            DataBase db = DataBaseManager.getInstance();//获取数据库实例
            //模糊搜索所有包含bookName的书籍
            List<Book> books = db.getLike(Book.class, "BookName", request.getBookName());//模糊搜索
            //List<Book> books = db.getWhere(Book.class, "BookName", request.getBookName());//精确搜索
            //后续可以进行部分匹配的搜索
            if (!books.isEmpty()) {//如果有同名书籍
                //先加入一个number属性，表示同名书籍的数量
                data.addProperty("number", String.valueOf(books.size()) );
                //返回所有同名书籍信息,遍历list里面的每一个book，添加到json对象里,json对象返回一个数组，里面是每一本book的所有信息
                for (int i = 0; i < books.size(); i++) {
                    Book book = books.get(i);
                    JsonObject bookData = new JsonObject();
                    bookData.addProperty("bookName",book.getBookName());
                    bookData.addProperty("author", book.getAuthor());
                    bookData.addProperty("publisher", book.getPublisher());
                    bookData.addProperty("publishDate", book.getLanguage());
                    bookData.addProperty("author", book.getAuthor());
                    bookData.addProperty("ISBN", book.getISBN());
                    bookData.addProperty("description", book.getDescription());
//                  bookData.addProperty("Cover",book.getCover());
                    bookData.addProperty("Kind",book.getKind());
                    bookData.addProperty("quantity", book.getQuantity());
                    bookData.addProperty("Valid_Quantity", book.getValid_Quantity());
                    data.addProperty("b" + i, gson.toJson(bookData));
                }
                data.addProperty("status", "success");
                return gson.toJson(data);

            }
            else {
                data.addProperty("error", "No book found.");
                return gson.toJson(data);
            }
        }
        else //如果不是学生，则不予显示书籍信息，表示没有权限查找书籍
        {
            data.addProperty("error", "You don't have permission to search book.");
        }
        return gson.toJson(data);
    }

//    public String borrowBook(String jsonData) {};
//
//    public String returnBook(String jsonData) {};
//
//    public String addBook(String jsonData) {};
//
//    public String deleteBook(String jsonData) {};
}
