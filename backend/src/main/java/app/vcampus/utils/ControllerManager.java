package app.vcampus.utils;

import app.vcampus.controller.UserController;
import com.google.gson.Gson;

public class ControllerManager {
    private final UserController userController = new UserController();
    private final RouteMapping routeMapping = new RouteMapping();
    private final Gson gson = new Gson();

    public ControllerManager() {
        // 注册路由
        routeMapping.registerRoute("login", userController::login);
    }

    public String handleRequest(String jsonData) {
        // 解析 JSON 请求
        Request request = gson.fromJson(jsonData, Request.class);

        // 根据请求类型调用相应的控制器方法
        return routeMapping.handleRequest(request.getType(), jsonData);
    }
}