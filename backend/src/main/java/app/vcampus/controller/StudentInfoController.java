package app.vcampus.controller;

import app.vcampus.domain.Student;
import app.vcampus.domain.User;
import app.vcampus.interfaces.studentInfoRequest;
import app.vcampus.utils.DataBase;
import app.vcampus.utils.DataBaseManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;

public class StudentInfoController {
    private final Gson gson = new Gson();


    //获取学生信息
    public String getStudentInfo(String jsonData) {
        studentInfoRequest request = gson.fromJson(jsonData, studentInfoRequest.class);
        DataBase db = DataBaseManager.getInstance();
        Student student = db.getWhere(Student.class, "studentId", request.getUserId()).get(0);
        JsonObject data = new JsonObject();
        data.addProperty("studentId", student.getStudentId());
        data.addProperty("name", student.getUsername());
        data.addProperty("gender", student.getGender()==0?"男":"女");
        data.addProperty("race", student.getRace());
        data.addProperty("major", student.getMajor());
        data.addProperty("academy", student.getAcademy());
        data.addProperty("nativePlace", student.getNativePlace());
        return gson.toJson(data);
    }

    //添加学生信息
    public String addStudentStatus(String jsonData) {
        try {
            Student student = gson.fromJson(jsonData, Student.class);
            DataBase db = DataBaseManager.getInstance();
            db.persist(student);

            // 同时向 User 库添加一条数据
            User user = new User();
            user.setUserId(student.getStudentId());
            user.setUsername(student.getUsername());
            user.setPassword("123456");
            user.setGender(student.getGender());
            user.setRole(0);
            user.setBalance(0);
            db.persist(user);

            JsonObject data = new JsonObject();
            data.addProperty("status", "success");
            return gson.toJson(data);
        } catch (Exception e) {
            JsonObject data = new JsonObject();
            data.addProperty("status", "failed");
            data.addProperty("reason", e.getMessage());
            return gson.toJson(data);
        }
    }

    // 删除学生信息
    public String deleteStudentStatus(String jsonData) {
        try {
            JsonObject request = gson.fromJson(jsonData, JsonObject.class);
            String studentId = request.get("studentId").getAsString();

            DataBase db = DataBaseManager.getInstance();
            Student student = db.getWhere(Student.class, "studentId", studentId).get(0);
            db.remove(student);

            // 同时从 User 库中删除对应的数据
            User user = db.getWhere(User.class, "userId", studentId).get(0);
            db.remove(user);

            JsonObject data = new JsonObject();
            data.addProperty("status", "success");
            return gson.toJson(data);
        } catch (Exception e) {
            JsonObject data = new JsonObject();
            data.addProperty("status", "failed");
            data.addProperty("reason", e.getMessage());
            return gson.toJson(data);
        }
    }
}
