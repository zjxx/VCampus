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
        List<User> users = db.getWhere(User.class,"userId",request.getUsername());
        if(!users.isEmpty()) {
            User user = users.get(0);
            if (user.getPassword().equals(request.getPassword())) {
                data.addProperty("status", "success");
                data.addProperty("role", UserType.fromIndex((int) user.getRole()));
                data.addProperty("userId", user.getUserId());
                return gson.toJson(data);
            } else {
                data.addProperty("message", "Wrong password.");
                data.addProperty("status", "fail");
                return gson.toJson(data);
            }
        }
        data.addProperty("message", "User not found.");
        data.addProperty("status", "fail");
        return gson.toJson(data);
    }

    // 修改密码
    public String modifyPassword(String jsonData) {
        JsonObject request = gson.fromJson(jsonData, JsonObject.class);
        String userId = request.get("userId").getAsString();
        String oldPassword = request.get("oldPassword").getAsString();
        String newPassword = request.get("newPassword").getAsString();

        DataBase db = DataBaseManager.getInstance();
        List<User> users = db.getWhere(User.class, "userId", userId);
        JsonObject response = new JsonObject();

        if (users.isEmpty()) {
            response.addProperty("status", "fail");
            response.addProperty("message", "User not found.");
            return gson.toJson(response);
        }

        User user = users.get(0);
        if (!user.getPassword().equals(oldPassword)) {
            response.addProperty("status", "fail");
            response.addProperty("message", "Old password is incorrect.");
            return gson.toJson(response);
        }

        user.setPassword(newPassword);
        db.persist(user);

        response.addProperty("status", "success");
        response.addProperty("message", "Password changed successfully.");
        return gson.toJson(response);
    }
}