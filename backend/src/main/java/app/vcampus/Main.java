package app.vcampus;

import app.vcampus.controller.StoreItemController;
import app.vcampus.domain.StoreItem;
import app.vcampus.domain.Student;
import app.vcampus.domain.User;
import app.vcampus.utils.DataBase;
import app.vcampus.utils.DataBaseManager;
import app.vcampus.utils.server.NettyServer;

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
        dataBase.close();

        System.out.println("Server started at port 8066");



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

        NettyServer nettyServer = new NettyServer(8066);
        nettyServer.start();
    }
}