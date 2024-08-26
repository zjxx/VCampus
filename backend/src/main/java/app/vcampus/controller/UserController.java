package app.vcampus.controller;

import app.vcampus.domain.User;
import app.vcampus.enums.UserType;
import app.vcampus.interfaces.loginRequest;
import app.vcampus.utils.DataBase;
import app.vcampus.utils.DataBaseManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;

public class UserController {
    private final Gson gson = new Gson();

    public String login(String jsonData) {
        // 解析 JSON 数据
        loginRequest request = gson.fromJson(jsonData, loginRequest.class);
        JsonObject data = new JsonObject();
        DataBase db = DataBaseManager.getInstance();
        List<User> users = db.getWhere(User.class,"username",request.getUsername());
        for (User user : users) {
            if(user.getPassword().equals(request.getPassword())){
                data.addProperty("status", "success");
                data.addProperty("role", UserType.fromIndex((int) user.getRole()));
                data.addProperty("userId", user.getUserId());
                return gson.toJson(data);
            }
            else {
                data.addProperty("message", "Wrong password.");
                data.addProperty("status", "fail");
                return gson.toJson(data);
            }
        }
        data.addProperty("message", "User not found.");
        data.addProperty("status", "fail");
        return gson.toJson(data);
    }
}