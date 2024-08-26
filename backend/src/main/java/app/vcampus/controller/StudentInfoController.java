package app.vcampus.controller;

import app.vcampus.domain.Student;
import app.vcampus.interfaces.studentInfoRequest;
import app.vcampus.utils.DataBase;
import app.vcampus.utils.DataBaseManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;

public class StudentInfoController {
    private final Gson gson = new Gson();
    public String getStudentInfo(String jsonData) {
        studentInfoRequest request = gson.fromJson(jsonData, studentInfoRequest.class);
        DataBase db = DataBaseManager.getInstance();
        Student student = db.getWhere(Student.class, "userId", request.getUserId()).get(0);
        JsonObject data = new JsonObject();
        data.addProperty("studentId", student.getStudentId());
        data.addProperty("name", student.getUsername());
        data.addProperty("gender", student.getGender()==0?"男":"女");
        data.addProperty("race", student.getRace());
        data.addProperty("major", student.getMajor());
        data.addProperty("academy", student.getAcademy());
        return null;
    }
}
