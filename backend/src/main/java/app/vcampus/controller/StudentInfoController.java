package app.vcampus.controller;

import app.vcampus.domain.Student;
import app.vcampus.domain.User;
import app.vcampus.interfaces.studentInfoRequest;
import app.vcampus.utils.DataBase;
import app.vcampus.utils.DataBaseManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
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


            // 同时向 User 库添加一条数据
            User user = new User();
            user.setUserId(student.getStudentId());
            user.setUsername(student.getUsername());
            user.setPassword("123456");
            user.setGender(student.getGender());
            user.setRole(0);
            user.setBalance(0);
            db.persist(user);
            db.persist(student);
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

    //根据姓名搜索学生信息，可以支持模糊搜索
    public String searchStudent(String jsonData) {
        try {
            JsonObject request = gson.fromJson(jsonData, JsonObject.class);
            String keyword = request.get("keyword").getAsString();
            DataBase db = DataBaseManager.getInstance();
            List<Student> students = db.getWhere(Student.class, "username LIKE", "%" + keyword + "%");
            JsonArray studentObjects = new JsonArray();
            for (Student student : students) {
                JsonObject studentObject = new JsonObject();
                studentObject.addProperty("studentId", student.getStudentId());
                studentObject.addProperty("name", student.getUsername());
                studentObject.addProperty("gender", student.getGender() == 0 ? "男" : "女");
                studentObject.addProperty("race", student.getRace());
                studentObject.addProperty("major", student.getMajor());
                studentObject.addProperty("academy", student.getAcademy());
                studentObject.addProperty("nativePlace", student.getNativePlace());
                studentObjects.add(studentObject);
            }
            JsonObject response = new JsonObject();
            response.addProperty("status", "success");
            response.add("students", studentObjects);
            return gson.toJson(response);
        } catch (Exception e) {
            JsonObject response = new JsonObject();
            response.addProperty("status", "failed");
            response.addProperty("reason", e.getMessage());
            return gson.toJson(response);
        }
    }

    // 修改学生信息
    public String updateStudentStatus(String jsonData) {
        try {
            Student updatedStudent = gson.fromJson(jsonData, Student.class);
            DataBase db = DataBaseManager.getInstance();
            Student existingStudent = db.getWhere(Student.class, "studentId", updatedStudent.getStudentId()).get(0);

            existingStudent.setUsername(updatedStudent.getUsername());
            existingStudent.setGender(updatedStudent.getGender());
            existingStudent.setRace(updatedStudent.getRace());
            existingStudent.setMajor(updatedStudent.getMajor());
            existingStudent.setAcademy(updatedStudent.getAcademy());
            existingStudent.setNativePlace(updatedStudent.getNativePlace());

            db.persist(existingStudent);

            JsonObject response = new JsonObject();
            response.addProperty("status", "success");
            return gson.toJson(response);
        } catch (Exception e) {
            JsonObject response = new JsonObject();
            response.addProperty("status", "failed");
            response.addProperty("reason", e.getMessage());
            return gson.toJson(response);
        }
    }
}