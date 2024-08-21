package app.vcampus.controller;

import app.vcampus.utils.Request;
import app.vcampus.utils.Response;
import com.google.gson.Gson;

public class UserController {
    private final Gson gson = new Gson();

    public String login(String jsonData) {
        // 解析 JSON 数据
        Request request = gson.fromJson(jsonData, Request.class);

        // 处理登录逻辑（示例：将消息转换为大写）
        String processedMessage = "Login successful for user: " + request.getMessage();

        // 创建响应
        Response response = new Response(processedMessage);
        return gson.toJson(response);
    }
}