package app.vcampus.controller;

import app.vcampus.domain.Book;
import app.vcampus.domain.Reader2Book;
import app.vcampus.domain.User;
import app.vcampus.interfaces.*;
import app.vcampus.utils.DataBase;
import app.vcampus.utils.DataBaseManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.xml.crypto.Data;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LibraryController {
    private final Gson gson = new Gson();
    private FileOutputStream fileOutputStream;
    //前端输入role和bookName，后端返回相应的书籍信息
    /**
     * Searches for book information based on the provided JSON data.
     *
     * @param jsonData The JSON data containing the search criteria.
     * @return A JSON string containing the status and the list of books matching the search criteria.
     */
    public String searchBookInfo(String jsonData) {
        //解析JSON数据
        BookSearchingRequest request = gson.fromJson(jsonData, BookSearchingRequest.class);
        JsonObject data = new JsonObject();
        DataBase db = DataBaseManager.getInstance();//获取数据库实例
        List<Book> books = new ArrayList<>();
        if (request.getFlag().equals("bookName")) {
            //模糊搜索所有包含bookName的书籍
            books = db.getLike(Book.class, "BookName", request.getBookName());//模糊搜索
        } else if (request.getFlag().equals("ISBN")) {
            books = db.getWhere(Book.class, "ISBN", request.getBookName());//精确搜索
        }
        //后续可以进行部分匹配的搜索
        if (!books.isEmpty()) {//如果有同名书籍
            //先加入一个number属性，表示同名书籍的数量

            int j=0;
            //返回所有同名书籍信息,遍历list里面的每一个book，添加到json对象里,json对象返回一个数组，里面是每一本book的所有信息
                for (int i = 0; i < books.size(); i++) {
                    Book book = books.get(i);
                    if (book.getMaterialType().equals("book")) {
                        JsonObject bookData = new JsonObject();
                        bookData.addProperty("bookName", book.getBookName());
                        bookData.addProperty("author", book.getAuthor());
                        bookData.addProperty("publisher", book.getPublisher());
                        bookData.addProperty("publishDate", book.getPublishedYear().toString());
                        bookData.addProperty("language", book.getLanguage());
                        bookData.addProperty("author", book.getAuthor());
                        bookData.addProperty("ISBN", book.getISBN());
                        bookData.addProperty("description", book.getDescription());
//                  bookData.addProperty("Cover",book.getCover());
                        bookData.addProperty("Kind", book.getKind());
                        bookData.addProperty("quantity", book.getQuantity());
                        bookData.addProperty("Valid_Quantity", book.getValid_Quantity());
                        data.addProperty("b" + j, gson.toJson(bookData));
                        j+=1;
                    }
                    else {
                        continue;
                    }
                }
                data.addProperty("number", String.valueOf(j));
                data.addProperty("status", "success");
        }
        else
        {
            data.addProperty("status", "failed");
            data.addProperty("reason", "No book found.");
        }
        return gson.toJson(data);
    }

    /**
     * Searches for articles based on the provided JSON data.
     *
     * @param jsonData The JSON data containing the search criteria.
     * @return A JSON string containing the status and the list of articles matching the search criteria.
     */
    public String articleSearch(String jsonData) {
        ArticleSearchRequest request = gson.fromJson(jsonData, ArticleSearchRequest.class);
        JsonObject data = new JsonObject();
        DataBase db = DataBaseManager.getInstance();
        List<Book> books = db.getLike(Book.class, "BookName", request.getArticleName());
        int numofarticle=0;
        data.addProperty("number", String.valueOf(books.size()));
        if(!books.isEmpty()) {
            data.addProperty("status", "success");
            for(int i=0;i<books.size();i++) {
                Book book = books.get(i);
                JsonObject bookData = new JsonObject();
                if (book.getMaterialType().equals("article")) {

                    bookData.addProperty("title", book.getBookName());
                    bookData.addProperty("publisher", book.getPublisher());
                    bookData.addProperty("ISBN", book.getISBN());
                    bookData.addProperty("publishDate", book.getPublishedYear().toString());
                    data.addProperty("a" + numofarticle, gson.toJson(bookData));
                    numofarticle+=1;
                }
        }
        } else {
            data.addProperty("status", "failed");
            data.addProperty("reason", "No book found.");
        }
        data.addProperty("number", String.valueOf(numofarticle));
        return gson.toJson(data);
    }

    //借书功能
    /**
     * Handles the request to borrow a book.
     *
     * @param jsonData The JSON data containing the borrow request details.
     * @return A JSON string containing the status of the borrow request.
     */
    public String borrowBook(String jsonData) {
        // 解析JSON数据
        BookBorrowRequest request = gson.fromJson(jsonData, BookBorrowRequest.class);
        JsonObject data = new JsonObject();

        // 判断用户身份，如果是学生或教师，则借阅书籍
        if (!"student".equals(request.getRole()) && !"teacher".equals(request.getRole())) {
            data.addProperty("status", "failed");
            data.addProperty("reason", "You don't have permission to borrow book.");
            return gson.toJson(data);
        }

        DataBase db = DataBaseManager.getInstance();

        // 先判断书籍是否存在
        List<Book> books = db.getWhere(Book.class, "ISBN", request.getISBN());
        if (books.isEmpty()) {
            data.addProperty("status", "failed");
            data.addProperty("reason", "No book found.");
            return gson.toJson(data);
        }

        Book book = books.get(0);

        // 判断书籍是否有余量
        if (book.getValid_Quantity() <= 0) {
            data.addProperty("status", "failed");
            data.addProperty("reason", "The book is not available.");
            return gson.toJson(data);
        }

        // 在借阅记录中查询该ISBN书的借阅记录
        List<Reader2Book> borrowedBooks = db.getWhere(Reader2Book.class, "Book_ISBN", request.getISBN());

        // 判断该用户是否已经借阅了该书籍
        for (Reader2Book borrowedBook : borrowedBooks) {
            if (borrowedBook.getReader_ID().equals(request.getuserId()) && borrowedBook.isBook_State()) {
                data.addProperty("error", "You have borrowed the book before.");
                return gson.toJson(data);
            }
        }

        // 创建借阅记录
        Reader2Book reader2Book = new Reader2Book();
        reader2Book.setReader_ID(request.getuserId());
        reader2Book.setBook_ISBN(request.getISBN());

        // 设置借阅日期为处理请求的日期
        Date currentDate = new Date();
        reader2Book.setBorrow_Date(currentDate);

        // 设置还书日期为30天后
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_YEAR, 30);
        reader2Book.setReturn_Date(calendar.getTime());

        reader2Book.setBorrow_State(true); // 借阅状态为true，表示借阅在期限内
        reader2Book.setBook_State(true);   // 书籍状态为true，表示书籍被借阅

        // 更新书籍的借阅量
        book.setValid_Quantity(book.getValid_Quantity() - 1);

        // 更新数据库
        db.save(reader2Book);
        db.update(book);

        data.addProperty("status", "success");
        return gson.toJson(data);
    }


    //还书功能
    /**
     * Handles the request to return a borrowed book.
     *
     * @param jsonData The JSON data containing the return request details.
     * @return A JSON string containing the status of the return request.
     */
    public String returnBook(String jsonData) {
        //解析JSON数据
        BookReturnRequest request = gson.fromJson(jsonData, BookReturnRequest.class);
        JsonObject data = new JsonObject();
        //判断用户身份,如果是学生or教师，则还书
        if (request.getRole().equals("student") || request.getRole().equals("teacher")) {
            DataBase db = DataBaseManager.getInstance();
            //先判断书籍是否存在
            List<Book> books = db.getWhere(Book.class, "ISBN", request.getISBN());
            if (!books.isEmpty()) {
                Book book = books.get(0);
                //在借阅记录中查询该ISBN书的借阅记录
                List<Reader2Book> borrowedBooks = db.getWhere(Reader2Book.class, "Book_ISBN", request.getISBN());
                //如果有借阅记录，则判断该用户是否是该书的借阅者，如果是，则还书
                if (!borrowedBooks.isEmpty()) {
                    for (Reader2Book borrowedBook : borrowedBooks) {
                        if (borrowedBook.getReader_ID().equals(request.getuserId())) {
                            //判断是否超过30天还书期限,如果超期，按照每天1.8元的罚款进行罚款
                            Date currentDate = new Date();
                            //将还书时间保存到借阅记录中
                            borrowedBook.setReturn_Date(currentDate);
                            //判断是否超过30天还书期限
                            if (currentDate.after(borrowedBook.getReturn_Date())) {
                                //计算罚款金额
                                int fine = (int) ((currentDate.getTime() - borrowedBook.getReturn_Date().getTime()) / (1000 * 60 * 60 * 24)) * 18;   //每天1.8元的罚款
                                //返回罚款金额
                                data.addProperty("fine", fine);
                            } else {
                                data.addProperty("fine", 0);
                            }
                            //更新借阅记录
                            borrowedBook.setBorrow_State(false);//借阅状态为false,表示借阅已过期
                            borrowedBook.setBook_State(false);//书籍状态为false,表示书籍已归还
                            //更新书籍的借阅量
                            book.setValid_Quantity(book.getValid_Quantity() + 1);
                            //更新数据库
                            db.update(borrowedBook);
                            db.update(book);
                            data.addProperty("status", "success");
                        }
                    }
                } else {
                    data.addProperty("status","failed");
                    data.addProperty("reason", "You haven't borrowed the book before.");
                }
            } else {
                data.addProperty("status","failed");
                data.addProperty("reason", "No book found.");
            }
        } else
        {
            data.addProperty("status","failed");
            data.addProperty("reason", "You don't have permission to return book.");
        }
        return gson.toJson(data);
    }

    //延期还书功能
    /**
     * Handles the request to delay the return of a borrowed book.
     *
     * @param jsonData The JSON data containing the delay return request details.
     * @return A JSON string containing the status of the delay return request.
     */
    public String delayReturnBook(String jsonData) {
        //解析JSON数据
        BookDelayReturnRequest request = gson.fromJson(jsonData, BookDelayReturnRequest.class);
        JsonObject data = new JsonObject();
        //判断用户身份,如果是学生or教师，则允许延期还书
        if (request.getRole().equals("student") || request.getRole().equals("teacher")) {
            DataBase db = DataBaseManager.getInstance();
            //先判断书籍是否存在
            List<Book> books = db.getWhere(Book.class, "ISBN", request.getISBN());
            if (!books.isEmpty()) {
                Book book = books.get(0);
                //在借阅记录中查询该ISBN书的借阅记录
                List<Reader2Book> borrowedBooks = db.getWhere(Reader2Book.class, "Book_ISBN", request.getISBN());
                //如果有借阅记录，则判断该用户是否是该书的借阅者，如果是，则延期还书
                if (!borrowedBooks.isEmpty()) {
                    for (Reader2Book borrowedBook : borrowedBooks) {
                        if (borrowedBook.getReader_ID().equals(request.getuserId())) {
                            //检查书籍是否已经被还书，如果已经被还书，则提示用户已经还书了
                            if (!borrowedBook.isBook_State()) {
                                data.addProperty("status","failed");
                                data.addProperty("reason", "The book has been returned.");
                                return gson.toJson(data);
                            }
                            //判断是否超过30天还书期限,如果超期，不允许延期，提示用户还书
                            Date currentDate = new Date();
                            //判断是否超过30天还书期限
                            if (currentDate.after(borrowedBook.getReturn_Date())) {
                                data.addProperty("status","failed");
                                data.addProperty("reason", "You cannot delay return the book after 30 days.");
                            } else {
                                //否则允许延期还书
                                //判断借阅记录中的延期次数，是否小于3次，如果小于3次，则延期还书，否则提示用户不能延期
                                if (borrowedBook.getDelay_Times() < 3) {
                                    //延期还书，延期时间为借阅日期加上延期30天
                                    borrowedBook.setReturn_Date(borrowedBook.getBorrow_Date());
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(borrowedBook.getReturn_Date());
                                    calendar.add(Calendar.DAY_OF_YEAR, 30);
                                    borrowedBook.setReturn_Date(calendar.getTime());    //更新延期还书日期
                                    //返回json文件，提示下次还书的时间
                                    data.addProperty("return_date", borrowedBook.getReturn_Date().toString());
                                    borrowedBook.setDelay_Times(borrowedBook.getDelay_Times() + 1);   //更新延期次数
                                    //返回json数据，提示还可以延期的次数
                                    data.addProperty("delay_times", 3 - borrowedBook.getDelay_Times());
                                    //更新数据库
                                    db.update(borrowedBook);
                                    data.addProperty("status", "success");
                                } else {
                                    data.addProperty("status","failed");
                                    data.addProperty("reason", "You cannot delay return the book more than 3 times.");
                                }
                            }
                        } else {
                            data.addProperty("status","failed");
                            data.addProperty("reason", "You haven't borrowed the book before.");
                        }
                    }
                } else {
                    data.addProperty("status","failed");
                    data.addProperty("reason", "No book found.");
                }
            } else {
                data.addProperty("status","failed");
                data.addProperty("reason", "You don't have permission to delay return book.");
            }
        }
        return gson.toJson(data);
    }

    //普通用户查看借书记录功能（当前借阅和历史借阅）
    /**
     * Handles the request to view the borrow record of a user.
     *
     * @param jsonData The JSON data containing the request details.
     * @return A JSON string containing the status and the borrow record of the user.
     */
    public String viewBorrowRecord(String jsonData) {
        //解析JSON数据
        BookListRequest request = gson.fromJson(jsonData, BookListRequest.class);
        JsonObject data = new JsonObject();
        //判断用户身份,如果是学生or教师，则查看借阅记录
        if (request.getRole().equals("student") || request.getRole().equals("teacher")) {
            //在借阅记录中查找该用户的所有借阅记录
            DataBase db = DataBaseManager.getInstance();
            List<Reader2Book> borrowedBooks = db.getWhere(Reader2Book.class, "Reader_ID", request.getuserId());
            if (!borrowedBooks.isEmpty()) {
                int borrowingi=0;
                int haveBorrowedi=0;
                //遍历借阅记录，将借阅信息添加到json对象中,分为两种，正在借阅（未还）和历史借阅（已还）
                for (int i = 0; i < borrowedBooks.size(); i++) {
                    Reader2Book borrowedBook = borrowedBooks.get(i);
                    //在book表中查找书籍信息
                    List<Book> books = db.getWhere(Book.class, "ISBN", borrowedBook.getBook_ISBN());
                    String bookName = " ";
                    if (!books.isEmpty()) {
                        Book book = books.get(0);
                        bookName = book.getBookName();
                    }
                    //如果书籍状态为true，表示书籍未归还，则为正在借阅
                    if (borrowedBook.isBook_State()) {
                        JsonObject bookData = new JsonObject();
                        bookData.addProperty("bookName", bookName);
                        bookData.addProperty("ISBN",borrowedBook.getBook_ISBN());
                        bookData.addProperty("borrow_date", borrowedBook.getBorrow_Date().toString());
                        //应还日期
                        bookData.addProperty("return_date", borrowedBook.getReturn_Date().toString());
                        data.addProperty("borrowing" + borrowingi, gson.toJson(bookData));
                        borrowingi++;
                    }
                    //如果书籍状态为false，表示书籍已归还，则为历史借阅
                    else {
                        JsonObject bookData = new JsonObject();
                        bookData.addProperty("bookName", bookName);
                        bookData.addProperty("ISBN",borrowedBook.getBook_ISBN());
                        bookData.addProperty("borrow_date", borrowedBook.getBorrow_Date().toString());
                        bookData.addProperty("return_date", borrowedBook.getReturn_Date().toString());
                        data.addProperty("haveBorrowed" + haveBorrowedi, gson.toJson(bookData));
                        haveBorrowedi++;
                    }
                }
                data.addProperty("status", "success");
                data.addProperty("borrowing_number", String.valueOf(borrowingi));
                data.addProperty("haveBorrowed_number", String.valueOf(haveBorrowedi));
            } else {
                data.addProperty("status", "failed");
                data.addProperty("reason", "You haven't borrowed any book.");
            }
        } else {
            data.addProperty("status", "failed");
            data.addProperty("reason", "You don't have permission to view the borrow record.");
        }
        return gson.toJson(data);
    }

    //管理员删除藏书
    /**
     * Deletes a book from the library.
     *
     * @param jsonData The JSON data containing the book deletion request details.
     * @return A JSON string containing the status of the deletion request.
     */
    public String deleteBook(String jsonData) {
        //解析JSON数据
        BookDeleteRequest request = gson.fromJson(jsonData, BookDeleteRequest.class);
        JsonObject data = new JsonObject();
        //判断用户身份,如果是管理员，则删除藏书
        if (request.getRole().equals("admin")) {
            DataBase db = DataBaseManager.getInstance();
            //先判断书籍是否存在
            List<Book> books = db.getWhere(Book.class, "ISBN", request.getISBN());
            if (!books.isEmpty()) {
                Book book = books.get(0);
                //删除书籍
                db.delete(book);
                //删除该书籍有关的所有借阅记录
                List<Reader2Book> borrowedBooks = db.getWhere(Reader2Book.class, "Book_ISBN", request.getISBN());
                for (Reader2Book borrowedBook : borrowedBooks) {
                    db.delete(borrowedBook);                }
                data.addProperty("status", "success");
            } else {
                data.addProperty("status", "failed");
                data.addProperty("reason", "No book found.");
            }
        } else {
            data.addProperty("status", "failed");
            data.addProperty("reason", "You don't have permission to delete book.");
        }
        return gson.toJson(data);
    }


    //管理员查看借阅记录功能（所有借阅记录）
    /**
     * Views all borrow records.
     *
     * @param jsonData The JSON data containing the request details.
     * @return A JSON string containing the status and the list of all borrow records.
     */
    public String viewAllBorrowRecord(String jsonData) {
        //解析JSON数据
        BookListAllRequest request = gson.fromJson(jsonData, BookListAllRequest.class);
        JsonObject data = new JsonObject();
        //判断用户身份,如果是管理员，则显示所有借阅记录

        if (request.getRole().equals("admin")) {
            DataBase db = DataBaseManager.getInstance();
            List<Reader2Book> borrowedBooks = db.getAll(Reader2Book.class);
            if (!borrowedBooks.isEmpty()) {
                //遍历借阅记录，将借阅信息添加到json对象中
                for (int i = 0; i < borrowedBooks.size(); i++) {
                    Reader2Book borrowedBook = borrowedBooks.get(i);
                    //在book表中查找书籍信息
                    List<Book> books = db.getWhere(Book.class, "ISBN", borrowedBook.getBook_ISBN());
                    String bookName = " ";
                    if (!books.isEmpty()) {
                        Book book = books.get(0);
                        bookName = book.getBookName();
                    }
                    //如果书籍状态为true，表示书籍未归还，则为正在借阅
                    if (borrowedBook.isBook_State()) {
                        JsonObject bookData = new JsonObject();
                        bookData.addProperty("bookName", bookName);
                        bookData.addProperty("ISBN",borrowedBook.getBook_ISBN());
                        bookData.addProperty("borrow_date", borrowedBook.getBorrow_Date().toString());
                        //应还日期
                        bookData.addProperty("return_date", borrowedBook.getReturn_Date().toString());
                        data.addProperty("borrowing" + i, gson.toJson(bookData));
                    }
                    //如果书籍状态为false，表示书籍已归还，则为历史借阅
                    else {
                        JsonObject bookData = new JsonObject();
                        bookData.addProperty("bookName", bookName);
                        bookData.addProperty("ISBN",borrowedBook.getBook_ISBN());
                        bookData.addProperty("borrow_date", borrowedBook.getBorrow_Date().toString());
                        bookData.addProperty("return_date", borrowedBook.getReturn_Date().toString());
                        data.addProperty("haveBorrowed" + i, gson.toJson(bookData));
                    }
                }
                data.addProperty("status", "success");
            }else{
                data.addProperty("status", "failed");
                data.addProperty("reason", "No borrow record found.");
            }
        }
        else{
            data.addProperty("status", "failed");
            data.addProperty("reason", "You don't have permission to view the borrow record.");
        }
        return gson.toJson(data);
    }

    //管理员增加藏书功能
    /**
     * Adds a new book to the library.
     *
     * @param jsonData The JSON data containing the book details.
     * @param additionalParam The additional parameter containing the book cover image data encoded in Base64.
     * @return A JSON string containing the status of the add book request.
     */
    public String addBook(String jsonData,String additionalParam){
        //解析JSON数据
        JsonObject data = new JsonObject();
        JsonObject request = gson.fromJson(jsonData, JsonObject.class);
        try{
            DataBase db = DataBaseManager.getInstance();
            List<Book> books = db.getWhere(Book.class, "ISBN", request.get("ISBN").getAsString());
            if(!books.isEmpty()){
                data.addProperty("status", "failed");
                data.addProperty("reason", "The book already exists.");
                return gson.toJson(data);
            }
            String filepath="C:\\Users\\Administrator\\Desktop\\server\\img\\"+ request.get("ISBN").getAsString() + ".png";
            fileOutputStream = new FileOutputStream(filepath);//指定保持路径
            byte[] bytes = java.util.Base64.getDecoder().decode(additionalParam);
            fileOutputStream.write(bytes);
            fileOutputStream.close();
            Book book = new Book();
            book.setISBN(request.get("ISBN").getAsString());
            book.setBookName(request.get("bookName").getAsString());
            book.setAuthor(request.get("author").getAsString());
            book.setPublisher(request.get("publisher").getAsString());
            book.setPublishedYear(request.get("publishDate").getAsInt());
            book.setLanguage(request.get("language").getAsString());
            book.setDescription(request.get("description").getAsString());
            book.setKind(request.get("kind").getAsString());
            book.setQuantity(request.get("quantity").getAsInt());
            book.setValid_Quantity(request.get("Valid_Quantity").getAsInt());
            book.setMaterialType("book");

            db.save(book);
            data.addProperty("status", "success");
        }
        catch (Exception e){
            System.out.println("Error: " + e);
            data.addProperty("error", "Failed to add book.");
        }

        return gson.toJson(data);
    };

    //管理员查看用户借书记录
    /**
     * Handles the request to view the borrow record of a user.
     *
     * @param jsonData The JSON data containing the request details.
     * @return A JSON string containing the status and the borrow record of the user.
     */
    public String viewUserBorrowRecord(String jsonData) {
        BorrowUserSearchRequest request = gson.fromJson(jsonData, BorrowUserSearchRequest.class);
        JsonObject data = new JsonObject();
        DataBase db = DataBaseManager.getInstance();
        //判断用户身份,如果是管理员,则显示用户借阅记录
        if (request.getRole().equals("admin")) {
            //如果request.getSearchId()为空，则显示所有借阅记录
            //判断用户存不存在
            List<User> readers = db.getWhere(User.class, "userId", request.getUserId());
            if (!readers.isEmpty()) {
                //在借阅记录中查找该用户的所有借阅记录
                List<Reader2Book> borrowedBooks = db.getLike(Reader2Book.class, "Reader_ID", request.getSearchId());
                int borrowingnumber=0;
                int haveBorrowednumber=0;
                if (!borrowedBooks.isEmpty()) {
                    //遍历借阅记录，将借阅信息添加到json对象中
                    for (int i = 0; i < borrowedBooks.size(); i++) {
                        Reader2Book borrowedBook = borrowedBooks.get(i);
                        //在book表中查找书籍信息
                        List<Book> books = db.getWhere(Book.class, "ISBN", borrowedBook.getBook_ISBN());
                        String bookName = " ";
                        if (!books.isEmpty()) {
                            Book book = books.get(0);
                            bookName = book.getBookName();
                        }
                        //如果书籍状态为true，表示书籍未归还，则为正在借阅
                        if (borrowedBook.isBook_State()) {

                            JsonObject bookData = new JsonObject();
                            bookData.addProperty("readerId", borrowedBook.getReader_ID());
                            bookData.addProperty("bookName", bookName);
                            bookData.addProperty("ISBN", borrowedBook.getBook_ISBN());
                            bookData.addProperty("borrow_date", borrowedBook.getBorrow_Date().toString());
                            //应还日期
                            bookData.addProperty("return_date", borrowedBook.getReturn_Date().toString());
                            data.addProperty("borrowing" + borrowingnumber, gson.toJson(bookData));
                            borrowingnumber++;
                        }
                        //如果书籍状态为false，表示书籍已归还，则为历史借阅
                        else
                        {

                            JsonObject bookData = new JsonObject();
                            bookData.addProperty("readerId", borrowedBook.getReader_ID());
                            bookData.addProperty("bookName", bookName);
                            bookData.addProperty("ISBN", borrowedBook.getBook_ISBN());
                            bookData.addProperty("borrow_date", borrowedBook.getBorrow_Date().toString());
                            bookData.addProperty("return_date", borrowedBook.getReturn_Date().toString());
                            data.addProperty("haveBorrowed" + haveBorrowednumber, gson.toJson(bookData));
                            haveBorrowednumber++;
                        }
                    }
                    data.addProperty("haveBorrowed_number", String.valueOf(haveBorrowednumber));
                    data.addProperty("borrowing_number", String.valueOf(borrowingnumber));
                    data.addProperty("status", "success");
                }
                else
                {
                    data.addProperty("status", "failed");
                    data.addProperty("reason", "No borrow record found.");
                }
            }
            else
            {
                data.addProperty("status", "failed");
                data.addProperty("reason", "No user found.");
            }
        }
        else
        {
            data.addProperty("status", "failed");
            data.addProperty("reason", "You don't have permission to view the borrow record.");
        }
        return gson.toJson(data);
    }

    //修改藏书信息
    /**
     * Updates the information of a book in the library.
     *
     * @param jsonData The JSON data containing the updated book details.
     * @param additionalParam The additional parameter containing the book cover image data encoded in Base64.
     * @return A JSON string containing the status of the update request.
     */
    public String updateBook(String jsonData, String additionalParam) {
        // 解析JSON数据
        JsonObject data = new JsonObject();
        JsonObject request = gson.fromJson(jsonData, JsonObject.class);
        DataBase db = DataBaseManager.getInstance();

        // 删除旧的图书信息
        List<Book> books = db.getWhere(Book.class, "ISBN", request.get("ISBN").getAsString());
        if (!books.isEmpty()) {
            Book oldBook = books.get(0);
            db.remove(oldBook);
        } else {
            data.addProperty("status", "failed");
            data.addProperty("reason", "No book found to delete.");
            return gson.toJson(data);
        }

        // 添加新的图书信息
        try {
            String filepath = "C:\\Users\\Administrator\\Desktop\\server\\img\\" + request.get("ISBN").getAsString() + ".png";
            fileOutputStream = new FileOutputStream(filepath);
            byte[] bytes = java.util.Base64.getDecoder().decode(additionalParam);
            fileOutputStream.write(bytes);
            fileOutputStream.close();

            Book newBook = new Book();
            newBook.setISBN(request.get("ISBN").getAsString());
            newBook.setBookName(request.get("bookName").getAsString());
            newBook.setAuthor(request.get("author").getAsString());
            newBook.setPublisher(request.get("publisher").getAsString());
            newBook.setPublishedYear(request.get("publishDate").getAsInt());
            newBook.setLanguage(request.get("language").getAsString());
            newBook.setDescription(request.get("description").getAsString());
            newBook.setKind(request.get("kind").getAsString());
            newBook.setQuantity(request.get("quantity").getAsInt());
            newBook.setValid_Quantity(request.get("Valid_Quantity").getAsInt());
            newBook.setMaterialType("book");

            db.save(newBook);
            data.addProperty("status", "success");
        } catch (Exception e) {
            System.out.println("Error: " + e);
            data.addProperty("status", "failed");
            data.addProperty("reason", "Failed to add the new book.");
        }

        return gson.toJson(data);
    }

}




