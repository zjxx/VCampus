package app.vcampus.utils;

import app.vcampus.controller.StoreController;
import app.vcampus.controller.StoreTransactionController;
import app.vcampus.controller.StudentInfoController;
import app.vcampus.controller.UserController;
import app.vcampus.controller.LibraryController;
import app.vcampus.controller.CourseController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

public class ControllerManager {
    private final UserController userController = new UserController();
    private final StoreController storeController = new StoreController();
    private final RouteMapping routeMapping = new RouteMapping();
    private final StudentInfoController studentInfoController = new StudentInfoController();
    private final LibraryController libraryController = new LibraryController();
    private final CourseController courseController = new CourseController();
    private final Gson gson = new Gson();

    public ControllerManager() {
        // 注册路由
        routeMapping.registerRoute("login", userController::login);
        routeMapping.registerRoute("arc/view", studentInfoController::getStudentInfo);
        routeMapping.registerRoute("searchItems", storeController::searchItems);
        routeMapping.registerRoute("purchase", storeController::handlePurchase);
        routeMapping.registerRoute("getAllItems", storeController::getAllItems);
        routeMapping.registerRoute("getTransactions", storeController::getAllTransaction);
        routeMapping.registerRoute("lib/search", libraryController::searchBookInfo);//搜索图书
        routeMapping.registerRoute("lib/addtolist", libraryController::borrowBook);//借书
        routeMapping.registerRoute("lib/returnbook", libraryController::returnBook);//还书
        routeMapping.registerRoute("lib/renewbook", libraryController::delayReturnBook);//续借
        routeMapping.registerRoute("searchItems", storeController::searchItems);//搜索商品
        routeMapping.registerRoute("purchase", storeController::handlePurchase);//购买商品
        routeMapping.registerRoute("getAllItems", storeController::getAllItems);//获取所有商品
        routeMapping.registerRoute("getTransactions", storeController::getAllTransaction);//获取所有交易
        routeMapping.registerRoute("enterStore",storeController::enterStore);//进入商店展示商品,返回随机商品列表
        routeMapping.registerRoute("getTransactionsByCardNumber", storeController::getTransactionsByCardNumber);//根据卡号获取交易记录
        routeMapping.registerRoute("arc/add", studentInfoController::addStudentStatus);//
        routeMapping.registerRoute("lib/fetchImageUrl", libraryController::searchBookInfo);//
        routeMapping.registerRoute("arc/delete", studentInfoController::deleteStudentStatus);//
        routeMapping.registerRoute("arc/search", studentInfoController::searchStudent);//
        routeMapping.registerRoute("course/listAll", courseController::showEnrollList);//向登录学生展示方案课程
        routeMapping.registerRoute("course/select", courseController::selectCourse);//学生选择课程
        routeMapping.registerRoute("course/unselect", courseController::unselectCourse);//学生退课
        routeMapping.registerRoute("course/courseTable",courseController::showCourseTable);//学生查看课程表
        routeMapping.registerRoute("course/search", courseController::searchCourse);//学生搜索课程
    }
    public String handleRequest(String jsonData) {
        // 解析 JSON 请求
        Request request = gson.fromJson(jsonData, Request.class);

        // 根据请求类型调用相应的控制器方法
        return routeMapping.handleRequest(request.getType(), jsonData);
    }

    private String getImage(String jsonData){
        return "https://th.bing.com/th/id/R.061dc0f43851e2ef1f114ee33eabf427?rik=H7HNnwjTHfgibg&pid=ImgRaw&r=0";
    }
}