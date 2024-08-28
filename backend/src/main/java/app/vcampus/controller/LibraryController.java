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
        if (request.getRole().equals("student")){//可以改成fromindex函数
            DataBase db = DataBaseManager.getInstance();//获取数据库实例
            List<Book> books = db.getWhere(Book.class, "BookName", request.getBookName());//查询同名书籍
            if (!books.isEmpty()) {//如果有同名书籍
                //返回所有同名书籍信息,遍历list里面的每一个book，添加到json对象里,json对象返回一个数组，里面是每一本book的所有信息
//                for (Book book : books) {
//                    data.addProperty("bookName", book.getBookName());
//                    data.addProperty("author", book.getAuthor());
//                    data.addProperty("publisher", book.getPublisher());
//                    data.addProperty("publishedYear", book.getPublishedYear());
//                    data.addProperty("ISBN", book.getISBN());
//                    data.addProperty("cover", book.getCover());
//                    data.addProperty("kind", book.getKind());
//                    data.addProperty("language", book.getLanguage());
//                }
//                return gson.toJson(data);
                return gson.toJson(books);
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
}
