package app.vcampus.controller;

import app.vcampus.domain.*;
import app.vcampus.enums.AcademyType;
import app.vcampus.interfaces.studentInfoRequest;
import app.vcampus.utils.DataBase;
import app.vcampus.utils.DataBaseManager;
import app.vcampus.utils.PasswordUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class StudentInfoController {
    private final Gson gson = new Gson();


    //获取学生信息
    /**
     * Retrieves student information based on the provided JSON data.
     *
     * @param jsonData The JSON data containing the student ID.
     * @return A JSON string containing the student information.
     */
    public String getStudentInfo(String jsonData) {
        studentInfoRequest request = gson.fromJson(jsonData, studentInfoRequest.class);
        DataBase db = DataBaseManager.getInstance();
        Student student = db.getWhere(Student.class, "studentId", request.getUserId()).get(0);
        User user = db.getWhere(User.class, "userId", request.getUserId()).get(0);
        JsonObject data = new JsonObject();
        data.addProperty("studentId", student.getStudentId());
        data.addProperty("name", student.getUsername());
        data.addProperty("gender", student.getGender()==0?"男":"女");
        data.addProperty("race", student.getRace());
        data.addProperty("major", student.getMajor());        //将MajorType转换为String类型
        data.addProperty("academy", student.getAcademy());
        data.addProperty("nativePlace", student.getNativePlace());
        data.addProperty("email", user.getEmail()==null?"":user.getEmail());
        return gson.toJson(data);
    }

    //添加学生信息
    /**
     * Adds a new student status based on the provided JSON data.
     *
     * @param jsonData The JSON data containing the student information.
     * @return A JSON string containing the status of the add operation.
     */
    public String addStudentStatus(String jsonData) {
        try {

            Student student = gson.fromJson(jsonData, Student.class);
            if(!checkvalidAcademy(student.getAcademy())){
                JsonObject data = new JsonObject();
                data.addProperty("status", "failed");
                data.addProperty("reason", "学院输入错误");
                return gson.toJson(data);
            }
            if (student.getStudentId() == null || student.getStudentId().isEmpty() ||
                    student.getUsername() == null || student.getUsername().isEmpty() ||
                    student.getRace() == null || student.getRace().isEmpty() ||
                    student.getMajor() == null ||
                    student.getAcademy() == null || student.getAcademy().isEmpty() ||
                    student.getNativePlace() == null || student.getNativePlace().isEmpty()) {
                JsonObject data = new JsonObject();
                data.addProperty("status", "failed");
                data.addProperty("reason", "字段不能为空");
                return gson.toJson(data);
            }
            DataBase db = DataBaseManager.getInstance();

            // 同时向 User 库添加一条数据
            User user = new User();
            user.setUserId(student.getStudentId());
            user.setUsername(student.getUsername());
            String password = PasswordUtils.hashPassword("123456");
            user.setPassword(password);
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
    /**
     * Deletes a student status based on the provided JSON data.
     *
     * @param jsonData The JSON data containing the student ID.
     * @return A JSON string containing the status of the delete operation.
     */
    public String deleteStudentStatus(String jsonData) {
        try {
            JsonObject request = gson.fromJson(jsonData, JsonObject.class);
            String studentId = request.get("studentId").getAsString();

            DataBase db = DataBaseManager.getInstance();
            Student student = db.getWhere(Student.class, "studentId", studentId).get(0);
            db.remove(student);

            // 同时从 User 库中删除对应的数据
            User user = db.getWhere(User.class, "userId", studentId).get(0);
            List<ShoppingCartItem> shoppingCartItems = db.getWhere(ShoppingCartItem.class, "userId", studentId);
            for (ShoppingCartItem item : shoppingCartItems) {
                db.remove(item);
            }
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
    /**
     * Searches for student information based on the provided JSON data.
     * Supports fuzzy search by student name.
     *
     * @param jsonData The JSON data containing the search keyword.
     * @return A JSON string containing the search results.
     */
    public String searchStudent(String jsonData) {
        try {
            JsonObject request = gson.fromJson(jsonData, JsonObject.class);
            String keyword = request.get("keyword").getAsString();
            DataBase db = DataBaseManager.getInstance();
            List<Student> students = db.getLike(Student.class, "username", keyword);
            JsonObject response = new JsonObject();
            if(students.isEmpty()) {
                response.addProperty("status", "fail");
                response.addProperty("reason", "未找到相关学生");
                return gson.toJson(response);
            }
            response.addProperty("status", "success");
            response.addProperty("number", String.valueOf(students.size()));
            for (int i = 0; i < students.size(); i++) {
                Student student = students.get(i);
                JsonObject studentObject = new JsonObject();
                studentObject.addProperty("studentId", student.getStudentId());
                studentObject.addProperty("name", student.getUsername());
                studentObject.addProperty("gender", student.getGender() == 0 ? "男" : "女");
                studentObject.addProperty("race", student.getRace());
                studentObject.addProperty("major", student.getMajor());
                studentObject.addProperty("academy", student.getAcademy());
                studentObject.addProperty("nativePlace", student.getNativePlace());
                response.addProperty("s"+i,gson.toJson(studentObject));
            }
            return gson.toJson(response);
        } catch (Exception e) {
            JsonObject response = new JsonObject();
            response.addProperty("status", "failed");
            response.addProperty("reason", e.getMessage());
            return gson.toJson(response);
        }
    }

    // 修改学生信息
    /**
     * Updates student information based on the provided JSON data.
     *
     * @param jsonData The JSON data containing the updated student information.
     * @return A JSON string containing the status of the update operation.
     */
    public String updateStudentStatus(String jsonData) {
        try {
            Student updatedStudent = gson.fromJson(jsonData, Student.class);
            if(!checkvalidAcademy(updatedStudent.getAcademy())){
                JsonObject response = new JsonObject();
                response.addProperty("status", "failed");
                response.addProperty("reason", "学院输入错误");
                return gson.toJson(response);
            }
            if (updatedStudent.getStudentId() == null || updatedStudent.getStudentId().isEmpty() ||
                    updatedStudent.getUsername() == null || updatedStudent.getUsername().isEmpty() ||
                    updatedStudent.getGender() < 0 || // 检查 gender 是否为无效值
                    updatedStudent.getRace() == null || updatedStudent.getRace().isEmpty() ||
                    updatedStudent.getMajor() == null ||
                    updatedStudent.getAcademy() == null || updatedStudent.getAcademy().isEmpty() ||
                    updatedStudent.getNativePlace() == null || updatedStudent.getNativePlace().isEmpty()) {
                JsonObject response = new JsonObject();
                response.addProperty("status", "failed");
                response.addProperty("reason", "字段不能为空");
                return gson.toJson(response);
            }
            DataBase db = DataBaseManager.getInstance();
            Student existingStudent = db.getWhere(Student.class, "studentId", updatedStudent.getStudentId()).get(0);
            User existingUser = db.getWhere(User.class, "userId", updatedStudent.getStudentId()).get(0);
            existingStudent.setUsername(updatedStudent.getUsername());
            existingStudent.setGender(updatedStudent.getGender());
            existingStudent.setRace(updatedStudent.getRace());
            existingStudent.setMajor(updatedStudent.getMajor());
            existingStudent.setAcademy(updatedStudent.getAcademy());
            existingStudent.setNativePlace(updatedStudent.getNativePlace());

            existingUser.setUserId(existingStudent.getStudentId());
            existingUser.setUsername(existingStudent.getUsername());
            existingUser.setGender(updatedStudent.getGender());
            db.disableForeignKeyChecks();
            db.remove(existingStudent);

            db.persist(existingUser);
            db.persist(existingStudent);
            db.enableForeignKeyChecks();

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

    /**
     * Checks if the provided major is valid by comparing it with the list of academy types.
     *
     * @param major The major to be checked.
     * @return True if the major is valid, false otherwise.
     */
    private Boolean checkvalidAcademy(String major){
        for (AcademyType academyType : AcademyType.values()) {
            if (academyType.getMajor().equals(major)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Archives student information based on the provided JSON data.
     *
     * @param jsonData The JSON data containing the student information.
     * @return A JSON string containing the status of the archive operation.
     */
    public String arcFile(String jsonData) {
        try {
            JsonObject request = gson.fromJson(jsonData, JsonObject.class);
            int length = request.get("length").getAsInt();

            // 解析 items 字段为 JsonArray
            String itemsString = request.get("items").getAsString();
            JsonArray itemsArray = gson.fromJson(itemsString, JsonArray.class);
        for (int i = 0; i < length; i++) {
            Student student = gson.fromJson(itemsArray.get(i), Student.class);
            if (!checkvalidAcademy(student.getAcademy())) {
                JsonObject data = new JsonObject();
                data.addProperty("status", "failed");
                data.addProperty("reason", "学院输入错误");
                return gson.toJson(data);
            }
            if (student.getStudentId() == null || student.getStudentId().isEmpty() ||
                    student.getUsername() == null || student.getUsername().isEmpty() ||
                    student.getRace() == null || student.getRace().isEmpty() ||
                    student.getMajor() == null ||
                    student.getAcademy() == null || student.getAcademy().isEmpty() ||
                    student.getNativePlace() == null || student.getNativePlace().isEmpty()) {
                JsonObject data = new JsonObject();
                data.addProperty("status", "failed");
                data.addProperty("reason", "字段不能为空");
                return gson.toJson(data);
            }
            DataBase db = DataBaseManager.getInstance();

            // 同时向 User 库添加一条数据
            User user = new User();
            user.setUserId(student.getStudentId());
            user.setUsername(student.getUsername());
            String password = PasswordUtils.hashPassword("123456");
            user.setPassword(password);
            user.setGender(student.getGender());
            user.setRole(0);
            user.setBalance(0);
            db.persist(user);
            db.persist(student);
        }
            JsonObject data = new JsonObject();
            data.addProperty("status", "success");
            return gson.toJson(data);
        } catch (Exception e) {
            JsonObject response = new JsonObject();
            response.addProperty("status", "failed");
            response.addProperty("reason", e.getMessage());
            return gson.toJson(response);
        }
    }
}
