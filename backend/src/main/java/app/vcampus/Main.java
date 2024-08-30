package app.vcampus;

import app.vcampus.controller.StoreItemController;
import app.vcampus.controller.StoreTransactionController;
import app.vcampus.domain.StoreItem;
import app.vcampus.domain.Student;
import app.vcampus.domain.User;
import app.vcampus.domain.Book;
import app.vcampus.interfaces.PurchaseRequest;
import app.vcampus.utils.DataBase;
import app.vcampus.utils.DataBaseManager;
import app.vcampus.utils.server.NettyServer;
import com.google.gson.Gson;

import app.vcampus.interfaces.BookSearchingRequest;
import app.vcampus.controller.LibraryController;
import com.google.gson.Gson;

import java.util.List;
import java.util.UUID;

public class Main {

    public static void main(String[] args) throws Exception {
        DataBase dataBase = DataBaseManager.getInstance();
        List<User> users = dataBase.getWhere(User.class, "username", "test");
        for (User user : users) {
            System.out.println(user.getUsername() + " " + user.getPassword());
        }
        List<Student> students = dataBase.getWhere(Student.class, "studentId", "213240000");


        System.out.println("Server started at port 8066");

//// 新增测试代码
//        StoreTransactionController controller = new StoreTransactionController();
//        StoreItemController storeItemController = new StoreItemController();
//
//        // 创建 StoreItem 对象并设置每个字段的值
//        StoreItem storeItem = new StoreItem();
//        storeItem.setUuid(UUID.fromString("223e4567-e89b-12d3-a456-426614174000")); // 示例 UUID
//        storeItem.setItemName("Test Item");
//        storeItem.setPrice(100);
//        storeItem.setPictureLink("http://example.com/sample.jpg");
//        storeItem.setBarcode("1234567890123");
//        storeItem.setStock(50);
//        storeItem.setSalesVolume(0);
//        storeItem.setDescription("This is a test item.");
//
//        // 将 StoreItem 对象保存到数据库
//        storeItemController.addItem(storeItem);
//
//        //UUID uuid = UUID.fromString("223e4567-e89b-12d3-a456-426614174000");
//
//// 调用 deleteItem 方法并传入 UUID 对象
//        //storeItemController.deleteItem(uuid);
//
//
//
//        // 创建 PurchaseRequest 对象
//        PurchaseRequest request = new PurchaseRequest();
//        request.setItemUuid(storeItem.getUuid().toString());
//        request.setItemName(storeItem.getItemName());
//        request.setAmount(2);
//        request.setCardNumber("1234567890");
//
//        // 将 PurchaseRequest 对象转换为 JSON 字符串
//        Gson gson = new Gson();
//        String jsonData = gson.toJson(request);
//
//        // 调用 handlePurchase 方法并打印结果
//        String response = controller.handlePurchase(jsonData);
//        System.out.println(response);
//        // 新增测试代码
//        StoreItemController storeItemController = new StoreItemController();
//
//        // 创建一个示例 StoreItem 对象
//        StoreItem newItem = new StoreItem();
//        newItem.setUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
//        newItem.setItemName("Sample Item");
//        newItem.setPrice(100);
//        newItem.setPictureLink("http://example.com/sample.jpg");
//        newItem.setBarcode("1234567890123");
//        newItem.setStock(50);
//        newItem.setSalesVolume(0);
//        newItem.setDescription("This is a sample item.");
//
//        // 测试 addItem 函数
//        storeItemController.addItem(newItem);
//        System.out.println("Item added successfully");
//
//        // 测试 getItems 函数
//        List<StoreItem> items = storeItemController.getItems();
//        System.out.println("Items: " + items);
//
//        // 更新示例 StoreItem 对象
//        newItem.setItemName("Updated Item");
//        newItem.setPrice(150);
//        newItem.setDescription("This is an updated sample item.");
//
//        // 测试 updateItem 函数
//        storeItemController.updateItem(newItem);
//        System.out.println("Item updated successfully");
//
//        // 再次测试 getItems 函数
//        items = storeItemController.getItems();
//        System.out.println("Items after update: " + items);
//
//        // 测试 deleteItem 函数
//        storeItemController.deleteItem(newItem.getUuid());
//        System.out.println("Item deleted successfully");
//
//        // 再次测试 getItems 函数
//        items = storeItemController.getItems();
//        System.out.println("Items after deletion: " + items);

        // 测试 Book 类
        Book book = new Book();
        book.setBookName("test");
        book.setAuthor("xxx");
        book.setISBN("9787302485520");
        book.setPublisher("thu");
        book.setPublishedYear(2018);
        book.setLanguage("ch");
        book.setKind("cs");

        LibraryController library = new LibraryController();
        //测试searchBookInfo函数
        Gson gson = new Gson();

        // 准备测试用的BookSearchingRequest对象
        BookSearchingRequest request = new BookSearchingRequest("student", "test");

        // 将BookSearchingRequest对象序列化为JSON字符串
        String jsonData = gson.toJson(request);

        // 调用searchBookInfo函数
        String result = library.searchBookInfo(jsonData);

        // 打印结果
        System.out.println(result);


        NettyServer nettyServer = new NettyServer(8066);
        nettyServer.start();
    }
}