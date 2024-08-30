package app.vcampus.utils;

import app.vcampus.controller.StoreController;
import app.vcampus.controller.StoreTransactionController;
import app.vcampus.controller.StudentInfoController;
import app.vcampus.controller.UserController;
import app.vcampus.controller.LibraryController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

public class ControllerManager {
    private final UserController userController = new UserController();
    private final StoreController storeController = new StoreController();
    private final RouteMapping routeMapping = new RouteMapping();
    private final StudentInfoController studentInfoController = new StudentInfoController();
    private final LibraryController libraryController = new LibraryController();
    private final Gson gson = new Gson();

    public ControllerManager() {
        // 注册路由
        routeMapping.registerRoute("login", userController::login);
        routeMapping.registerRoute("searchStudentStatus", studentInfoController::getStudentInfo);
        routeMapping.registerRoute("searchItems", storeController::searchItems);
        routeMapping.registerRoute("purchase", storeController::handlePurchase);
        routeMapping.registerRoute("getAllItems", storeController::getAllItems);
        routeMapping.registerRoute("getTransactions", storeController::getAllTransaction);
        routeMapping.registerRoute("lib/search", libraryController::searchBookInfo);
    }
    public String handleRequest(String jsonData) {
        // 解析 JSON 请求
        Request request = gson.fromJson(jsonData, Request.class);

        // 根据请求类型调用相应的控制器方法
        return routeMapping.handleRequest(request.getType(), jsonData);
    }
}