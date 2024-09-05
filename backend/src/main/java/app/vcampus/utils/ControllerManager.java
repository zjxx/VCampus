package app.vcampus.utils;

import app.vcampus.controller.StoreController;
import app.vcampus.controller.StoreTransactionController;
import app.vcampus.controller.StudentInfoController;
import app.vcampus.controller.UserController;
import app.vcampus.controller.LibraryController;
import app.vcampus.controller.CourseController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

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

        routeMapping.registerRoute("lib/search", libraryController::searchBookInfo);
        routeMapping.registerRoute("lib/addtolist", libraryController::borrowBook);
        routeMapping.registerRoute("lib/returnbook", libraryController::returnBook);
        routeMapping.registerRoute("lib/renewbook", libraryController::delayReturnBook);
        routeMapping.registerRoute("lib/fetchImageUrl", libraryController::searchBookInfo);
        routeMapping.registerRoute("lib/search", libraryController::searchBookInfo);
        routeMapping.registerRoute("lib/check", libraryController::viewBorrowRecord);
        routeMapping.registerRouteWithParams("lib/file_upload", libraryController::addBook);


        routeMapping.registerRoute(("shop/addtolist"), storeController::addItem);//添加商品
        routeMapping.registerRoute("searchItems", storeController::searchItems);
        routeMapping.registerRoute("purchase", storeController::handlePurchase);
        routeMapping.registerRoute("getAllItems", storeController::getAllItems);
        routeMapping.registerRoute("getTransactions", storeController::getAllTransaction);
        routeMapping.registerRoute("searchItems", storeController::searchItems);//搜索商品
        routeMapping.registerRoute("purchase", storeController::handlePurchase);//购买商品
        routeMapping.registerRoute("shop/search", storeController::searchItems);//搜索商品
        routeMapping.registerRoute("shop/buy", storeController::handlePurchase);//购买商品
        routeMapping.registerRoute("getAllItems", storeController::getAllItems);//获取所有商品
        routeMapping.registerRoute("getTransactions", storeController::getAllTransaction);//获取所有交易
        routeMapping.registerRoute("enterStore",storeController::enterStore);//进入商店展示商品,返回随机商品列表
        routeMapping.registerRoute("getTransactionsByCardNumber", storeController::getTransactionsByCardNumber);//根据卡号获取交易记录
        routeMapping.registerRoute("arc/add", studentInfoController::addStudentStatus);//
        routeMapping.registerRoute("arc/view", studentInfoController::getStudentInfo);
        routeMapping.registerRoute("arc/delete", studentInfoController::deleteStudentStatus);//
        routeMapping.registerRoute("arc/search", studentInfoController::searchStudent);//
        routeMapping.registerRoute("arc/modify", studentInfoController::updateStudentStatus);//
        routeMapping.registerRoute(("arc/modifyPassword"), userController::modifyPassword);//修改密码);

        routeMapping.registerRoute("course/listAll", courseController::showEnrollList);//向登录学生展示方案课程
        routeMapping.registerRoute("course/select", courseController::selectCourse);//学生选择课程
        routeMapping.registerRoute("course/unselect", courseController::unselectCourse);//学生退课
        routeMapping.registerRoute("course/courseTable",courseController::showCourseTable);//学生查看课程表
        routeMapping.registerRoute("course/search",courseController::searchCourse);//学生搜索课程
        routeMapping.registerRoute("course/listStudent",courseController::ShowCourseStudent);//老师导出学生选课信息

    }
    public String handleRequest(String jsonData) {
        // Parse JSON request
        Request request = gson.fromJson(jsonData, Request.class);

        // Call the appropriate controller method based on the request type
        return routeMapping.handleRequest(request.getType(), jsonData);
    }

    public String handleRequestWithParams(String jsonData, String additionalParam) {
        // Parse JSON request
        Request request = gson.fromJson(jsonData, Request.class);

        // Call the appropriate controller method based on the request type
        return routeMapping.handleRequestWithParams(request.getType(), jsonData, additionalParam);
    }
}