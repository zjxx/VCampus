package app.vcampus.controller;

import app.vcampus.domain.User;
import app.vcampus.enums.UserType;
import app.vcampus.interfaces.loginRequest;
import app.vcampus.utils.DataBase;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;

public class UserController {
    private final Gson gson = new Gson();

    public String login(String jsonData) {
        // 解析 JSON 数据
        loginRequest request = gson.fromJson(jsonData, loginRequest.class);

        DataBase db = new DataBase();
        db.init();
        List<User> users = db.getWhere(User.class,"username",request.getUsername());
        db.close();
        for (User user : users) {
            if(user.getPassword().equals(request.getPassword())){
                JsonObject data = new JsonObject();
                data.addProperty("status", "success");
                data.addProperty("role", UserType.fromIndex((int) user.getRole()));
                return gson.toJson(data);
            }
        }
        JsonObject data = new JsonObject();
        data.addProperty("status", "fail");
        return gson.toJson(data);
    }
}