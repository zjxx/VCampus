package app.vcampus.utils;

import app.vcampus.controller.StoreTransactionController;
import app.vcampus.controller.StudentInfoController;
import app.vcampus.controller.UserController;
import com.google.gson.Gson;

public class ControllerManager {
    private final UserController userController = new UserController();
    private final StoreTransactionController storeTransactionController = new StoreTransactionController();
    private final RouteMapping routeMapping = new RouteMapping();
    private final StudentInfoController studentInfoController = new StudentInfoController();
    private final Gson gson = new Gson();

    public ControllerManager() {
        // 注册路由
        routeMapping.registerRoute("login", userController::login);

        routeMapping.registerRoute("searchStudentStatus", studentInfoController::getStudentInfo);
        routeMapping.registerRoute("purchase", storeTransactionController::handlePurchase);
        routeMapping.registerRoute("lib/fetchImageUrl", this::getImage);
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