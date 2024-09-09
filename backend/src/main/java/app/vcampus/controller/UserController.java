package app.vcampus.controller;

import app.vcampus.domain.User;
import app.vcampus.utils.EmailService;
import app.vcampus.enums.UserType;
import app.vcampus.interfaces.loginRequest;
import app.vcampus.utils.DataBase;
import app.vcampus.utils.DataBaseManager;
import app.vcampus.utils.PasswordUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Random;

public class UserController {
    private final Gson gson = new Gson();
    private final EmailService emailService = new EmailService();
    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    public String login(String jsonData) {
        // 解析 JSON 数据
        loginRequest request = gson.fromJson(jsonData, loginRequest.class);
        JsonObject data = new JsonObject();
        DataBase db = DataBaseManager.getInstance();
        List<User> users = db.getWhere(User.class, "userId", request.getUsername());
        if (!users.isEmpty()) {
            User user = users.get(0);
            String hashedPassword = PasswordUtils.hashPassword(request.getPassword());
            if (user.getPassword().equals(hashedPassword)) {
                if (user.getEmail() == null || user.getEmail().isEmpty()) {
                    data.addProperty("status", "noemail");
                } else {
                    data.addProperty("status", "success");
                }
                data.addProperty("role", UserType.fromIndex((int) user.getRole()));
                data.addProperty("userId", user.getUserId());
                data.addProperty("username", user.getUsername());
                return gson.toJson(data);
            } else {
                data.addProperty("message", "密码错误");
                data.addProperty("status", "fail");
                return gson.toJson(data);
            }
        }
        data.addProperty("message", "没有找到对应用户");
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
            response.addProperty("message", "没有找到对应用户");
            return gson.toJson(response);
        }

        User user = users.get(0);
        user.setEmail(email);
        db.persist(user);

        response.addProperty("status", "success");
        response.addProperty("message", "成功添加邮箱地址");
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
            response.addProperty("message", "没有找到对应用户");
            return gson.toJson(response);
        }

        User user = users.get(0);
        String hashedOldPassword = PasswordUtils.hashPassword(oldPassword);
        if (!user.getPassword().equals(hashedOldPassword)) {
            response.addProperty("status", "fail");
            response.addProperty("message", "旧密码错误");
            return gson.toJson(response);
        }

        String hashedNewPassword = PasswordUtils.hashPassword(newPassword);
        user.setPassword(hashedNewPassword);
        db.persist(user);

        response.addProperty("status", "success");
        response.addProperty("message", "成功修改密码");
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
            response.addProperty("message", "没有找到对应用户");
            return gson.toJson(response);
        }

        User user = users.get(0);
        if (!user.getEmail().equals(email)) {
            response.addProperty("status", "fail");
            response.addProperty("message", "邮箱地址错误");
            return gson.toJson(response);
        }

        String verificationCode = generateVerificationCode();
        emailService.sendEmail(email, "虚拟校园系统验证码", "重置密码验证码是: " + verificationCode);

        response.addProperty("status", "success");
        response.addProperty("code", verificationCode);
        return gson.toJson(response);
    }

    public String updatePassword(String jsonData) {
        JsonObject request = gson.fromJson(jsonData, JsonObject.class);
        String userId = request.get("userId").getAsString();
        String newPassword = request.get("newPassword").getAsString();

        DataBase db = DataBaseManager.getInstance();
        User user = db.getWhere(User.class, "userId", userId).get(0);
        JsonObject response = new JsonObject();

        if (user == null) {
            response.addProperty("status", "fail");
            response.addProperty("message", "没有找到对应用户");
            return gson.toJson(response);
        }

        String hashedNewPassword = PasswordUtils.hashPassword(newPassword);
        user.setPassword(hashedNewPassword);
        db.persist(user);

        response.addProperty("status", "success");
        response.addProperty("message", "成功修改密码");
        return gson.toJson(response);
    }

    public String updateEmail(String jsonData) {
       try{
           JsonObject request = gson.fromJson(jsonData, JsonObject.class);
           String userId = request.get("userId").getAsString();
           String newEmail = request.get("email").getAsString();

           if(newEmail == null || newEmail.isEmpty()||userId == null || userId.isEmpty()){
               JsonObject response = new JsonObject();
               response.addProperty("status", "fail");
               response.addProperty("message", "输入为空");
               return gson.toJson(response);
           }
           DataBase db = DataBaseManager.getInstance();
           User user = db.getWhere(User.class, "userId", userId).get(0);
           JsonObject response = new JsonObject();

           if (user==null) {
               response.addProperty("status", "fail");
               response.addProperty("message", "没有找到对应用户");
               return gson.toJson(response);
           }

           user.setEmail(newEmail);
           db.persist(user);

           response.addProperty("status", "success");
           response.addProperty("message", "成功修改邮箱地址");
           return gson.toJson(response);
       }
         catch (Exception e){
              JsonObject response = new JsonObject();
              response.addProperty("status", "fail");
              response.addProperty("message", e.getMessage());
              return gson.toJson(response);
         }
    }

}