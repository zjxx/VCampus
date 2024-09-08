package app.vcampus;

import app.vcampus.controller.StoreItemController;
import app.vcampus.controller.StoreTransactionController;
import app.vcampus.controller.CourseController;
import app.vcampus.controller.ScoreController;
import app.vcampus.domain.*;

import app.vcampus.interfaces.*;

import app.vcampus.utils.DataBase;
import app.vcampus.utils.DataBaseManager;
import app.vcampus.utils.server.NettyServer;
import com.google.gson.Gson;


import app.vcampus.controller.LibraryController;

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
//
        //测试LibraryController
//        {
//        LibraryController library = new LibraryController();
//        Gson gson = new Gson();
//        BookSearchingRequest request0 = new BookSearchingRequest("student", "天才");
//        String jsonData0 = gson.toJson(request0);
//        String result0 = library.searchBookInfo(jsonData0);
//        System.out.println(result0);
//
//        //测试borrowBook函数
//        BookBorrowRequest request1 = new BookBorrowRequest("student","112112123","9787549565115");
//        String jsonData1 = gson.toJson(request1);
//        String result1 = library.borrowBook(jsonData1);
//        System.out.println(result1);
//
//        //测试returnBook函数
//        BookReturnRequest request2 = new BookReturnRequest("student","112112123","9787550263932");
//        String jsonData2 = gson.toJson(request2);
//        String result2 = library.returnBook(jsonData2);
//        System.out.println(result2);
//
//        //测试delayReturnBook函数
//        BookBorrowRequest request3 = new BookBorrowRequest("student","112112123","9787550263932");
//        String jsonData3 = gson.toJson(request3);
//        String result3 = library.delayReturnBook(jsonData3);
//        System.out.println(result3);

//        //测试viewBorrowRecord函数
//        BookListRequest request4 = new BookListRequest("student","112112123");
//        String jsonData4 = gson.toJson(request4);
//        String result4 = library.viewBorrowRecord(jsonData4);
//        System.out.println(result4);

//        //测试deleteBook函数
//        BookDeleteRequest request5 = new BookDeleteRequest("student","112112123","9787550263932");
//        String jsonData5 = gson.toJson(request5);
//        String result5 = library.deleteBook(jsonData5);
//        System.out.println(result5);
//        BookDeleteRequest request6 = new BookDeleteRequest("admin","002112123","9787549565115");
//        String jsonData6 = gson.toJson(request6);
//        String result6 = library.deleteBook(jsonData6);
//        System.out.println(result6);
//
//        //测试viewAllBorrowRecord函数
//        BookListRequest request7 = new BookListRequest("admin","002112123");
//        String jsonData7 = gson.toJson(request7);
//        String result7 = library.viewAllBorrowRecord(jsonData7);
//        System.out.println(result7);
//            //测试viewUserBorrowRecord函数
//            BorrowUserSearchRequest request8 = new BorrowUserSearchRequest("admin","123456789","213240000");
//            String jsonData8 = gson.toJson(request8);
//            String result8 = library.viewUserBorrowRecord(jsonData8);
//            System.out.println(result8);
//        }


        //测试CourseController
//        {
//            CourseController courseController = new CourseController();
//            Gson gson = new Gson();

//            //测试显示课程函数
//            EnrollmentShowRequest request0 = new EnrollmentShowRequest("213220159");
//            String jsonData0 = gson.toJson(request0);
//            String result0 = courseController.showEnrollList(jsonData0);
//            System.out.println(result0);
//            //测试选课函数
//            CourseSelectRequest request01 = new CourseSelectRequest("CS000001", "213220159");
//            String jsonData01 = gson.toJson(request01);
//            String result01 = courseController.selectCourse(jsonData01);
//            System.out.println(result01);
////            //测试退课函数
////            CourseUnselectRequest request2 = new CourseUnselectRequest("CS001020","213220159");
////            String jsonData2 = gson.toJson(request2);
////            String result2 = courseController.unselectCourse(jsonData2);
////            System.out.println(result2);
//            //测试显示课程表函数
//            CourseTableShowRequest request3 = new CourseTableShowRequest("213220159");
//            String jsonData3 = gson.toJson(request3);
//            String result3 = courseController.showCourseTable(jsonData3);
//            System.out.println(result3);
//            //测试课程搜索函数
//            CourseSearchRequest request4 = new CourseSearchRequest("操作","213220159");
//            String jsonData4 = gson.toJson(request4);
//            String result4 = courseController.searchCourse(jsonData4);
//            System.out.println(result4);
//            //测试教师查看选课学生名单函数
//            CourseStudentShowRequest request5= new CourseStudentShowRequest("CS001021","113240000");
//            String jsonData5 = gson.toJson(request5);
//            String result5 = courseController.ShowCourseStudent(jsonData5);
//            System.out.println(result5);
//            //测试管理员添加课程函数
//            CourseAddRequest request6 = new CourseAddRequest("操作系统","CS001023",
//                    "test3","113240004","1-3-5,3-6-7","J6-403",
//                    "4","70","23","9","24-2","必修");
//            String jsonData6 = gson.toJson(request6);
//            String result6 = courseController.addCourse(jsonData6);
//            System.out.println(result6);
//            //测试管理员删除课程函数
//            CourseDeleteRequest request7 = new CourseDeleteRequest("admin","123456789","CS001023");
//            String jsonData7 = gson.toJson(request7);
//            String result7 = courseController.deleteCourse(jsonData7);
//            System.out.println(result7);
//            //测试学生查看课表函数
//            CourseTableShowRequest request8 = new CourseTableShowRequest("213220159");
//            String jsonData8 = gson.toJson(request8);
//            String result8 = courseController.showStudentCourseTable(jsonData8);
//            System.out.println(result8);
//        }
//
//            //测试ScoreController
//            {
                ScoreController scoreController = new ScoreController();
                Gson gson = new Gson();
//
                //测试教师添加成绩函数
                ScoreGiveRequest request0 = new ScoreGiveRequest("113220000",
                        "213220159", "CS000001", "90",
                        "95", "92", "93");
                String jsonData0 = gson.toJson(request0);
                String result0 = scoreController.giveScore(jsonData0);
                System.out.println(result0);

                //测试同学查看成绩函数
                ScoreViewAllRequest request1 = new ScoreViewAllRequest("213220159");
                String jsonData1 = gson.toJson(request1);
                String result1 = scoreController.viewAllScore(jsonData1);
                System.out.println(result1);

                //测试教务查看成绩函数
                AllScoreListRequest request2 = new AllScoreListRequest("123456789");
                String jsonData2 = gson.toJson(request2);
                String result2 = scoreController.viewAllScore(jsonData2);
                System.out.println(result2);

                //测试教务审核成绩函数
                ScoreCheckRequest request3 = new ScoreCheckRequest("123456789", "CS000001", "审核未通过");
                String jsonData3 = gson.toJson(request3);
                String result3 = scoreController.checkScore(jsonData3);
                System.out.println(result3);

                //测试教师查看成绩函数
                MyCourseScoreListRequest request4 = new MyCourseScoreListRequest("113220000");
                String jsonData4 = gson.toJson(request4);
                String result4 = scoreController.ViewMyCourseScore(jsonData4);
                System.out.println(result4);

                //测试教师修改成绩函数
                ScoreModifyRequest request5 = new ScoreModifyRequest("113220000",
                        "213220159", "CS000001", "90",
                        "95", "98", "95");
                String jsonData5 = gson.toJson(request5);
                String result5 = scoreController.modifyScore(jsonData5);
                System.out.println(result5);

                //测试教务审核函数
                ScoreCheckRequest request6 = new ScoreCheckRequest("123456789", "CS000001", "审核通过");
                String jsonData6 = gson.toJson(request6);
                String result6 = scoreController.checkScore(jsonData6);
                System.out.println(result6);

                //测试学生查看成绩函数
                ScoreViewAllRequest request7 = new ScoreViewAllRequest("213220159");
                String jsonData7 = gson.toJson(request7);
                String result7 = scoreController.viewAllScore(jsonData7);
                System.out.println(result7);
//            }
        DataBase db = DataBaseManager.getInstance();
        List<Enrollment> enrollments = db.getWhere(Enrollment.class,"studentid","213240000");


        NettyServer nettyServer = new NettyServer(8066);
            nettyServer.start();
        }
    }

