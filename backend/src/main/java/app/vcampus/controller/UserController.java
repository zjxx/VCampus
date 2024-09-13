package app.vcampus.controller;

import app.vcampus.domain.Course;
import app.vcampus.domain.Enrollment;
import app.vcampus.domain.User;
import app.vcampus.utils.EmailService;
import app.vcampus.enums.UserType;
import app.vcampus.interfaces.loginRequest;
import app.vcampus.utils.DataBase;
import app.vcampus.utils.DataBaseManager;
import app.vcampus.utils.PasswordUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Date;
import java.util.List;
import java.util.Random;

public class UserController {
    private final Gson gson = new Gson();
    private final EmailService emailService = new EmailService();
    /**
     * Generates a verification code.
     *
     * @return A string representing a 6-digit verification code.
     */
    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    /**
     * Handles user login.
     *
     * @param jsonData The JSON data containing the login details.
     * @return A JSON string containing the login status and user information.
     */
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
                               //获取今天是星期几的string
                Date date = new Date();
                int day = date.getDay();
                if(day==0){
                    day=7;
                }

                if(user.getRole()==0) {
                    List<Course> coursetoday = new java.util.ArrayList<>();
                    List<Enrollment> enrollments = db.getWhere(Enrollment.class, "studentid", user.getUserId());

                    for (Enrollment enroll : enrollments) {
                        List<Course> courses = db.getWhere(Course.class, "courseId", enroll.getcourseid());
                        for (Course course : courses) {
                            String[] time = course.getTime().split(";");
                            for (int i = 0; i < time.length; i++) {
                                if (time[i].startsWith(String.valueOf(day))) {
                                    Course course1 = new Course();
                                    course1.setcourseId(course.getcourseId());
                                    course1.setcourseName(course.getcourseName());
                                    course1.setteacherName(course.getteacherName());
                                    course1.setLocation(course.getLocation().split(";")[i]);
                                    course1.setTime(time[i]);
                                    coursetoday.add(course1);
                                    break;
                                }
                            }
                        }
                    }
                    data.addProperty("number", String.valueOf(coursetoday.size()));
                    for (int i = 0; i < coursetoday.size(); i++) {
                        JsonObject course = new JsonObject();
                        course.addProperty("courseName", coursetoday.get(i).getcourseName());
                        course.addProperty("location", coursetoday.get(i).getLocation());
                        course.addProperty("time", coursetoday.get(i).getTime());
                        data.add("course" + i, course);
                    }
                }
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
    /**
     * Adds an email address to a user.
     *
     * @param jsonData The JSON data containing the user ID and email address.
     * @return A JSON string containing the status of the add email request.
     */
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
    /**
     * Modifies the password of a user.
     *
     * @param jsonData The JSON data containing the user ID, old password, and new password.
     * @return A JSON string containing the status of the modify password request.
     */
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

    /**
     * Sends a verification code to the user's email address.
     *
     * @param jsonData The JSON data containing the user ID and email address.
     * @return A JSON string containing the status of the send verification code request.
     */
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

    /**
     * Updates the password of a user.
     *
     * @param jsonData The JSON data containing the user ID and new password.
     * @return A JSON string containing the status of the update password request.
     */
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

    /**
     * Updates the email address of a user.
     *
     * @param jsonData The JSON data containing the user ID and new email address.
     * @return A JSON string containing the status of the update email request.
     */
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