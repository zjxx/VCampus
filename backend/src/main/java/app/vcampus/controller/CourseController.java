package app.vcampus.controller;

import app.vcampus.domain.Course;
import app.vcampus.domain.Enrollment;
import app.vcampus.domain.Student;
import app.vcampus.domain.Score;
import app.vcampus.interfaces.CourseSelectRequest;
import app.vcampus.interfaces.EnrollmentShowRequest;
import app.vcampus.interfaces.CourseUnselectRequest;
import app.vcampus.interfaces.CourseTableShowRequest;
import app.vcampus.utils.DataBase;
import app.vcampus.utils.DataBaseManager;


import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

public class CourseController {
    private final Gson gson = new Gson();

    //向学生显示选课列表
    public String showEnrollList(String jsonData) {
        EnrollmentShowRequest request = gson.fromJson(jsonData, EnrollmentShowRequest.class);
        JsonObject data = new JsonObject();
        DataBase db = DataBaseManager.getInstance();
        //用学生ID在student表中查找到对应学生的年级语句：
        List<Student> students = db.getWhere(Student.class, "studentId", request.getStudentId());
        if(students.size() == 0) {
            data.addProperty("error", "student not found");
            return gson.toJson(data);
        }
        Student student = students.get(0);
        String grade = student.getGrade();
        Integer major =student.getMajor();
        //用年级和教师ID在course表中查找到对应课程的课程ID语句：
        List<Course> allCourses = db.getWhere(Course.class,"valid_grade",student.getGrade());
        // 在courses里面筛选出符合major的数据
        List<Course> courses = new ArrayList<>();
        for (Course course : allCourses) {
            if (course.getMajor().equals(major)) {
                courses.add(course);
            }
        }
        //打包返回所有课程数据的json数据
        data.addProperty("number", courses.size());
        for(int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            JsonObject courseData = new JsonObject();
            courseData.addProperty("courseId", course.getCourse_id());
            courseData.addProperty("courseName", course.getCourse_name());
            courseData.addProperty("teacher", course.getTeacher_name());
            courseData.addProperty("credit", String.valueOf(course.getCredit()));
            courseData.addProperty("time", course.getTime());
            courseData.addProperty("location", course.getLocation());
            courseData.addProperty("capacity", String.valueOf(course.getCapacity()));
            courseData.addProperty("property",course.getProperty());
            courseData.addProperty("valid_capacity", String.valueOf(course.getValid_capacity()));
            data.add("course" + i, courseData);
        }
        data.addProperty("status", "success");
        return gson.toJson(data);
    }

    //学生选课函数
    public String selectCourse(String jsonData) {
        CourseSelectRequest request = gson.fromJson(jsonData, CourseSelectRequest.class);
        JsonObject data = new JsonObject();
        DataBase db = DataBaseManager.getInstance();

        // 检查学生ID是否存在
        List<Student> students = db.getWhere(Student.class, "studentId", request.getStudentId());
        if (students.isEmpty()) {
            data.addProperty("error", "student not found");
            return gson.toJson(data);
        }

        // 检查是否已经选课
        List<Enrollment> enrollments = db.getWhere(Enrollment.class, "student_id", request.getStudentId());
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getCourse_id().substring(0, 7).equals(request.getCourseId().substring(0, 7))) {
                data.addProperty("error", "student has already enrolled this course");
                return gson.toJson(data);
            }
        }

        // 插入新的选课记录
        Enrollment enrollment = new Enrollment();
        enrollment.setCourse_id(request.getCourseId());
        enrollment.setStudent_id(request.getStudentId());
        enrollment.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        //在Course表中对应课程的Valid_capacity-1
        List<Course> courses=db.getWhere(Course.class,"course_id",request.getCourseId());
        Course course=courses.get(0);
        course.setValid_capacity(course.getValid_capacity()-1);
        db.save(enrollment);
        db.update(course);
        data.addProperty("status", "success");
        return gson.toJson(data);
    }

    //学生退课函数
    public String unselectCourse(String jsonData){
        CourseUnselectRequest request = gson.fromJson(jsonData,CourseUnselectRequest.class);
        JsonObject data = new JsonObject();
        DataBase db = DataBaseManager.getInstance();
        //检查学生ID是否存在
        List<Student> students = db.getWhere(Student.class, "studentId", request.getStudentId());
        if(students.isEmpty()) {
            data.addProperty("error", "student not found");
            return gson.toJson(data);
        }
        //用学生ID在选课记录里查找对应课程的选课记录语句：
        List<Enrollment> enrollments = db.getWhere(Enrollment.class,"student_id",request.getStudentId());
        //for循环查找enrollments中对应课程ID的记录并删除
        for(int i = 0; i < enrollments.size(); i++) {
            Enrollment enrollment = enrollments.get(i);
            if(enrollment.getCourse_id().equals(request.getCourseId())) {
                List<Course> courses=db.getWhere(Course.class,"course_id",request.getCourseId());
                Course course=courses.get(0);
                course.setValid_capacity(course.getValid_capacity()+1);
                db.update(course);
                db.delete(enrollment);
                data.addProperty("status", "success");
                return gson.toJson(data);
            }
        }
        data.addProperty("error", "student has not enrolled this course");
        return gson.toJson(data);
    }
    //学生查看课表函数
    public String showCourseTable(String jsonData) {
        CourseTableShowRequest request = gson.fromJson(jsonData, CourseTableShowRequest.class);
        JsonObject data = new JsonObject();
        DataBase db = DataBaseManager.getInstance();
        //在授课记录中查找到所有该学生的选课记录
        List<Enrollment> enrollments = db.getWhere(Enrollment.class,"student_id",request.getStudentId());
        for(int i = 0; i < enrollments.size(); i++) {
            Enrollment enrollment=enrollments.get(i);
            JsonObject courseData=new JsonObject();
            List<Course> courses = db.getWhere(Course.class,"course_id",enrollment.getCourse_id());
            if(courses.isEmpty()) {
                courseData.addProperty("error", "course"+ enrollment.getCourse_id() + " not found");
            }
            else{
                Course course = courses.get(0);
                courseData.addProperty("courseId", course.getCourse_id());
                courseData.addProperty("courseName", course.getCourse_name());
                courseData.addProperty("teacher", course.getTeacher_name());
                courseData.addProperty("credit", course.getCredit());
                courseData.addProperty("time", course.getTime());
                courseData.addProperty("location", course.getLocation());
                courseData.addProperty("capacity", course.getCapacity());
                courseData.addProperty("valid_capacity", course.getValid_capacity());
            }
            data.add("course" + i, courseData);
        }
        data.addProperty("status", "success");
        return gson.toJson(data);
    }
    //教师导出课程参与学生名单
    //教师查看授课表
    //教师打分函数
    //！教师申请临时调课函数
    //！管理员审批调课函数，审批通过后，前端发送公告给学生
    //管理员导入课程函数
    //管理员审核成绩函数
    //管理员删除课程函数


}
