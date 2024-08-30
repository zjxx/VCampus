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
        routeMapping.registerRoute("searchItems", storeController::searchItems);//搜索商品
        routeMapping.registerRoute("purchase", storeController::handlePurchase);//购买商品
        routeMapping.registerRoute("getAllItems", storeController::getAllItems);//获取所有商品
        routeMapping.registerRoute("getTransactions", storeController::getAllTransaction);//获取所有交易
        routeMapping.registerRoute("enterStore",storeController::enterStore);//进入商店展示商品,返回随机商品列表
        routeMapping.registerRoute("getTransactionsByCardNumber", storeController::getTransactionsByCardNumber);//根据卡号获取交易记录
        routeMapping.registerRoute("addStudentStatus", studentInfoController::addStudentStatus);//
        routeMapping.registerRoute("lib/fetchImageUrl", this::getImage);//
        routeMapping.registerRoute("deleteStudentStatus", studentInfoController::deleteStudentStatus);//
    }
    public String handleRequest(String jsonData) {
        // 解析 JSON 请求
        Request request = gson.fromJson(jsonData, Request.class);

        // 根据请求类型调用相应的控制器方法
        return routeMapping.handleRequest(request.getType(), jsonData);
    }

    private  String getImage(String jsonData){
        return "https://th.bing.com/th/id/R.061dc0f43851e2ef1f114ee33eabf427?rik=H7HNnwjTHfgibg&pid=ImgRaw&r=0";
    }
}