package app.vcampus.controller;

import app.vcampus.domain.User;
import app.vcampus.utils.EmailService;
import app.vcampus.enums.UserType;
import app.vcampus.interfaces.loginRequest;
import app.vcampus.utils.DataBase;
import app.vcampus.utils.DataBaseManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Random;

public class UserController {
    private final Gson gson = new Gson();
    private final EmailService emailService = new EmailService();

    public String login(String jsonData) {
        // 解析 JSON 数据
        loginRequest request = gson.fromJson(jsonData, loginRequest.class);
        JsonObject data = new JsonObject();
        DataBase db = DataBaseManager.getInstance();
        List<User> users = db.getWhere(User.class,"userId",request.getUsername());
        if (!users.isEmpty()) {
            User user = users.get(0);
            if (user.getPassword().equals(request.getPassword())) {
                if (user.getEmail() == null || user.getEmail().isEmpty()) {
                    data.addProperty("status", "noemail");
                } else {
                    data.addProperty("status", "success");
                }
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

    // 添加邮箱
    public String addEmail(String jsonData) {
        JsonObject request = gson.fromJson(jsonData, JsonObject.class);
        String userId = request.get("userId").getAsString();
        String email = request.get("email").getAsString();

        DataBase db = DataBaseManager.getInstance();
        List<User> users = db.getWhere(User.class, "userId", userId);
        JsonObject response = new JsonObject();

        if (users.isEmpty()) {
            response.addProperty("status", "fail");
            response.addProperty("message", "User not found.");
            return gson.toJson(response);
        }

        User user = users.get(0);
        user.setEmail(email);
        db.persist(user);

        response.addProperty("status", "success");
        response.addProperty("message", "Email added successfully.");
        return gson.toJson(response);
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

    public String sendVerificationCode(String jsonData) {
        JsonObject request = gson.fromJson(jsonData, JsonObject.class);
        String userId = request.get("userId").getAsString();
        String email = request.get("email").getAsString();
        if(email == null || email.isEmpty()||userId == null || userId.isEmpty()){
            JsonObject response = new JsonObject();
            response.addProperty("status", "fail");
            response.addProperty("message", "输入为空");
            return gson.toJson(response);
        }

        DataBase db = DataBaseManager.getInstance();
        List<User> users = db.getWhere(User.class, "userId", userId);
        JsonObject response = new JsonObject();

        if (users.isEmpty()) {
            response.addProperty("status", "fail");
            response.addProperty("message", "User not found.");
            return gson.toJson(response);
        }

        User user = users.get(0);
        if (!user.getEmail().equals(email)) {
            response.addProperty("status", "fail");
            response.addProperty("message", "Email does not match.");
            return gson.toJson(response);
        }

        String verificationCode = generateVerificationCode();
        emailService.sendEmail(email, "虚拟校园系统验证码", "重置密码验证码是: " + verificationCode);

        response.addProperty("status", "success");
        response.addProperty("code", verificationCode);
        return gson.toJson(response);
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}