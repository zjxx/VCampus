package app.vcampus.controller;

import app.vcampus.interfaces.loginRequest;
import app.vcampus.utils.Response;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class UserController {
    private final Gson gson = new Gson();

    public String login(String jsonData) {
        // 解析 JSON 数据
        loginRequest request = gson.fromJson(jsonData, loginRequest.class);

        // 处理登录逻辑，应查询数据库验证用户名和密码，这边只做示例
        String processedMessage = "Login successful for user: "+request.getUsername();

        JsonObject data = new JsonObject();
        data.addProperty("message", processedMessage);
        data.addProperty("status", "success");//假设登录成功
        data.addProperty("role", "student");//假设查询发现是学生

        return gson.toJson(data);
    }
}