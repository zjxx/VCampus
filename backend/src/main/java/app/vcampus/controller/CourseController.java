package app.vcampus.controller;

import app.vcampus.domain.*;
import app.vcampus.interfaces.CourseSelectRequest;
import app.vcampus.interfaces.EnrollmentShowRequest;
import app.vcampus.interfaces.CourseUnselectRequest;
import app.vcampus.interfaces.CourseSearchRequest;
import app.vcampus.interfaces.CourseTableShowRequest;
import app.vcampus.interfaces.CourseStudentShowRequest;
import app.vcampus.interfaces.CourseAddRequest;
import app.vcampus.interfaces.CourseDeleteRequest;
import app.vcampus.interfaces.VideoDeleteRequest;
import app.vcampus.interfaces.VideoShowRequest;
import app.vcampus.interfaces.VideoWatchRequest;
import app.vcampus.interfaces.VideoListRequest;
import app.vcampus.interfaces.VideoUploadRequest;
import app.vcampus.utils.DataBase;
import app.vcampus.utils.DataBaseManager;



import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.FileOutputStream;
import java.util.*;
import java.text.SimpleDateFormat;

public class CourseController {
    private final Gson gson = new Gson();

    private FileOutputStream fileOutputStream;

    //向学生显示选课列表
    public String showEnrollList(String jsonData) {
        EnrollmentShowRequest request = gson.fromJson(jsonData, EnrollmentShowRequest.class);
        JsonObject data = new JsonObject();
        DataBase db = DataBaseManager.getInstance();
        //用学生ID在student表中查找到对应学生的年级语句：
        List<Student> students = db.getWhere(Student.class, "studentId", request.getStudentId());
        if (students.size() == 0) {
            data.addProperty("status", "failed");
            data.addProperty("reason", "student not found");
            return gson.toJson(data);
        }
        Student student = students.get(0);
        String major = student.getMajor();
        //用年级和教师ID在course表中查找到对应课程的课程ID语句：
        List<Course> allCourses = db.getWhere(Course.class, "validGrade", student.getGrade());
        // 在courses里面筛选出符合major的数据
        List<Course> courses = new ArrayList<>();
        for (Course course : allCourses) {
            if (course.getMajor().equals(major)) {
                courses.add(course);
            }
        }
        //打包返回所有课程数据的json数据
        data.addProperty("number", String.valueOf(courses.size()));
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            JsonObject courseData = new JsonObject();
            courseData.addProperty("courseId", course.getcourseId());
            courseData.addProperty("courseName", course.getcourseName());
            courseData.addProperty("teacher", course.getteacherName());
            courseData.addProperty("credit", String.valueOf(course.getCredit()));
            courseData.addProperty("time", course.getTime());
            courseData.addProperty("location", course.getLocation());
            courseData.addProperty("capacity", String.valueOf(course.getCapacity()));
            courseData.addProperty("property", course.getProperty());
            courseData.addProperty("validCapacity", String.valueOf(course.getvalidCapacity()));
            List<Enrollment> enrollment = db.getWhere(Enrollment.class, "courseid", course.getcourseId());
            Boolean isSelected = false;
            for (Enrollment e : enrollment) {
                if (e.getstudentid().equals(request.getStudentId())) {
                    isSelected = true;
                    break;
                }
            }
            courseData.addProperty("isSelected", isSelected);
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
            data.addProperty("status", "failed");
            data.addProperty("reason", "student not found");
            return gson.toJson(data);
        }

        // 检查是否已经选课
        List<Enrollment> enrollments = db.getWhere(Enrollment.class, "studentid", request.getStudentId());
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getcourseid().substring(0, 7).equals(request.getCourseId().substring(0, 7))) {
                data.addProperty("status", "failed");
                data.addProperty("reason", "student has already enrolled this course");
                return gson.toJson(data);
            }
        }
        //检查课程ID是否存在
        List<Course> courses = db.getWhere(Course.class, "courseId", request.getCourseId());
        if (courses.isEmpty()) {
            data.addProperty("status", "failed");
            data.addProperty("reason", "course not found");
            return gson.toJson(data);
        }
        //检查课程容量是否已满
        Course course = courses.get(0);
        if (course.getvalidCapacity() == 0) {
            data.addProperty("status", "failed");
            data.addProperty("reason", "course capacity is full");
            return gson.toJson(data);
        }
        //检查该课程的时间是否和学生的课程时间冲突
        //时间的保存格式为1-3-5，1代表星期一，3代表第3节课，5代表第5节课，不同的时间段用逗号隔开
        //将课程时间转换为数组
        String[] courseTime = course.getTime().split(",");
        //找出学生选的课程时间段
        List<Enrollment> records = db.getWhere(Enrollment.class, "studentid", request.getStudentId());
        for (int i = 0; i < records.size(); i++) {
            Enrollment record = records.get(i);
            //用Enrollment中的课程id去course表中找到对应的课程时间段
            List<Course> courseList = db.getWhere(Course.class, "courseId", record.getcourseid());
            Course course1 = courseList.get(0);
            //将course1的课程时间转换为数组
            String[] course1Time = course1.getTime().split(",");
            //判断两个时间段是否有交集
            for (int j = 0; j < courseTime.length; j++) {
                for (int k = 0; k < course1Time.length; k++) {
                    if (courseTime[j].equals(course1Time[k])) {
                        data.addProperty("status", "failed");
                        data.addProperty("reason", "course time conflict");
                        return gson.toJson(data);
                    }
                }
            }

        }

        // 插入新的选课记录
        Enrollment enrollment = new Enrollment();
        enrollment.setcourseid(request.getCourseId());
        enrollment.setstudentid(request.getStudentId());
        enrollment.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        //在Course表中对应课程的Valid_capacity-1
        course.setvalidCapacity(course.getvalidCapacity() - 1);
        //evict掉records，否则会报错
        for (int i = 0; i < records.size(); i++) {
            db.evict(records.get(i));
        }
        db.save(enrollment);
        db.update(course);
        data.addProperty("status", "success");
        return gson.toJson(data);
    }

    //学生退课函数
    public String unselectCourse(String jsonData) {
        CourseUnselectRequest request = gson.fromJson(jsonData, CourseUnselectRequest.class);
        JsonObject data = new JsonObject();
        DataBase db = DataBaseManager.getInstance();
        //检查学生ID是否存在
        List<Student> students = db.getWhere(Student.class, "studentId", request.getStudentId());
        if (students.isEmpty()) {
            data.addProperty("status", "failed");
            data.addProperty("reason", "student not found");
            return gson.toJson(data);
        }
        //用学生ID在选课记录里查找对应课程的选课记录语句：
        List<Enrollment> enrollments = db.getWhere(Enrollment.class, "studentid", request.getStudentId());
        //for循环查找enrollments中对应课程ID的记录并删除
        for (int i = 0; i < enrollments.size(); i++) {
            Enrollment enrollment = enrollments.get(i);
            if (enrollment.getcourseid().equals(request.getCourseId())) {
                List<Course> courses = db.getWhere(Course.class, "courseId", request.getCourseId());
                Course course = courses.get(0);
                course.setvalidCapacity(course.getvalidCapacity() + 1);
                db.update(course);
                db.delete(enrollment);
                data.addProperty("status", "success");
                return gson.toJson(data);
            }
        }
        data.addProperty("status", "failed");
        data.addProperty("reason", "student has not enrolled this course");
        return gson.toJson(data);
    }


    //学生搜索课程
    public String searchCourse(String jsonData) {
        CourseSearchRequest request = gson.fromJson(jsonData, CourseSearchRequest.class);
        JsonObject data = new JsonObject();
        DataBase db = DataBaseManager.getInstance();
        //模糊搜索request中的课程名
        List<Course> courses = db.getLike(Course.class, "courseName", request.getCourseName());
        List<Student> students = db.getWhere(Student.class, "studentId", request.getStudentId());
        Student student = students.get(0);
        String grade = student.getGrade();
        String major = student.getMajor();
        int num = 0;
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            if (course.getMajor().equals(major) && course.getvalidGrade().equals(grade)) {
                JsonObject courseData = new JsonObject();
                courseData.addProperty("courseId", course.getcourseId());
                courseData.addProperty("courseName", course.getcourseName());
                courseData.addProperty("teacher", course.getteacherName());
                courseData.addProperty("credit", String.valueOf(course.getCredit()));
                courseData.addProperty("time", course.getTime());
                courseData.addProperty("location", course.getLocation());
                courseData.addProperty("capacity", String.valueOf(course.getCapacity()));
                courseData.addProperty("property", course.getProperty());
                courseData.addProperty("validCapacity", String.valueOf(course.getvalidCapacity()));
                data.add("course" + num, courseData);
                num += 1;
            }
        }
        if (num == 0) {
            data.addProperty("status", "failed");
            data.addProperty("reason", "No such course found");
            return gson.toJson(data);
        }
        data.addProperty("status", "success");
        return gson.toJson(data);
    }

    //教师导出课程参与学生名单
    public String ShowCourseStudent(String jsonData) {
        CourseStudentShowRequest request = gson.fromJson(jsonData, CourseStudentShowRequest.class);
        JsonObject data = new JsonObject();
        DataBase db = DataBaseManager.getInstance();
        List<Course> courses = db.getWhere(Course.class, "courseId", request.getCourseId());
        Course course = courses.get(0);
        if (!course.getteacherId().equals(request.getTeacherId())) {
            data.addProperty("status", "failed");
            data.addProperty("reason", "teacher is not authorized to check the information of this course.");
            return gson.toJson(data);
        }
        //在选课记录中查询所有满足课程号的记录
        List<Enrollment> records = db.getWhere(Enrollment.class, "courseid", request.getCourseId());
        if (records.isEmpty()) {
            data.addProperty("status", "failed");
            data.addProperty("reason", "course not found");
            return gson.toJson(data);
        } else {
            data.addProperty("number", records.size());
            for (int i = 0; i < records.size(); i++) {
                Enrollment record = records.get(i);
                JsonObject studentData = new JsonObject();
                List<Student> students = db.getWhere(Student.class, "studentId", record.getstudentid());
                Student student = students.get(0);
                studentData.addProperty("studentTd", record.getstudentid());
                studentData.addProperty("name", student.getUsername());
                studentData.addProperty("gender", student.getGender());
                data.add("stu" + i, studentData);
            }
            return gson.toJson(data);
        }
    }

    //教师查看授课表
    //！教师申请临时调课函数
    //！管理员审批调课函数，审批通过后，前端发送公告给学生
    //管理员导入课程函数
    public String addCourse(String jsonData) {
        CourseAddRequest request = gson.fromJson(jsonData, CourseAddRequest.class);
        JsonObject data = new JsonObject();
        DataBase db = DataBaseManager.getInstance();
        //用course_id的前七位提取出一批courses
        List<Course> courses = db.getLike(Course.class, "courseId", request.getCourseId().substring(0, 7));
        //判断有没有重复课程
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            if (course.getcourseId().equals(request.getCourseId()) || course.getteacherId().equals(request.getTeacherId())) {
                data.addProperty("status", "failed");
                data.addProperty("reason", "course already exists");
                return gson.toJson(data);
            }
        }
        //判断有没有同时间同地点同学期的课程
        List<Course> sameTimeCourses = db.getWhere(Course.class, "time", request.getTime());
        for (int i = 0; i < sameTimeCourses.size(); i++) {
            Course course = sameTimeCourses.get(i);
            if (course.getLocation().equals(request.getLocation()) && course.getSemester().equals(request.getSemester())) {
                data.addProperty("status", "failed");
                data.addProperty("reason", "course already exists at the same time and location");
                return gson.toJson(data);
            }
        }

        Course course = new Course();
        course.setcourseId(request.getCourseId());
        course.setcourseName(request.getCourseName());
        course.setteacherId(request.getTeacherId());
        course.setteacherName(request.getTeacherName());
        course.setCredit(Integer.valueOf(request.getCredit()));
        course.setTime(request.getTime());
        course.setLocation(request.getLocation());
        course.setCapacity(Integer.valueOf(request.getCapacity()));
        course.setMajor(request.getMajor());
        course.setvalidGrade(request.getGrade());
        course.setProperty(request.getProperty());
        course.setvalidCapacity(course.getCapacity());
        course.setSemester(course.getSemester());
        db.save(course);
        data.addProperty("status", "success");
        return gson.toJson(data);
    }


    //管理员删除课程函数
    public String deleteCourse(String jsonData) {
        CourseDeleteRequest request = gson.fromJson(jsonData, CourseDeleteRequest.class);
        JsonObject data = new JsonObject();
        DataBase db = DataBaseManager.getInstance();
        List<Course> courses = db.getWhere(Course.class, "courseId", request.getCourseId());
        if (courses.isEmpty()) {
            data.addProperty("status", "failed");
            data.addProperty("reason", "course not found");
        } else {
            Course course = courses.get(0);

            List<Enrollment> records = db.getWhere(Enrollment.class, "courseid", request.getCourseId());
            for (int i = 0; i < records.size(); i++) {
                Enrollment record = records.get(i);
                db.delete(record);
            }
            List<Score> scores = db.getWhere(Score.class, "courseId", request.getCourseId());
            for (int i = 0; i < scores.size(); i++) {
                Score score = scores.get(i);
                db.delete(score);
            }
            db.delete(course);
            data.addProperty("status", "success");
        }
        return gson.toJson(data);
    }


    //删除课程视频
    public String deleteVideo(String jsonData){
        JsonObject request = gson.fromJson(jsonData, JsonObject.class);
        JsonObject data = new JsonObject();
        DataBase db=DataBaseManager.getInstance();
        List<Video> videos = db.getWhere(Video.class,"videoId",request.get("videoId").getAsString());
        if(videos.isEmpty()) {
            data.addProperty("status", "failed");
            data.addProperty("reason", "video not found");
        }
        else {
            Video video = videos.get(0);

            db.delete(video);
            data.addProperty("status", "success");
        }
        return gson.toJson(data);
    }

    //教务查看所有课
    public String showAdminList(String jsonData) {
        EnrollmentShowRequest request = gson.fromJson(jsonData, EnrollmentShowRequest.class);
        JsonObject data = new JsonObject();
        DataBase db = DataBaseManager.getInstance();

        // 在courses里面筛选出符合major的数据
        List<Course> courses = db.getLike(Course.class, "courseId", "");

        //打包返回所有课程数据的json数据
        data.addProperty("number", String.valueOf(courses.size()));
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            JsonObject courseData = new JsonObject();
            courseData.addProperty("courseId", course.getcourseId());
            courseData.addProperty("courseName", course.getcourseName());
            courseData.addProperty("teacherName", course.getteacherName());
            courseData.addProperty("teacherId", course.getteacherId());
            courseData.addProperty("semester", course.getSemester());
            courseData.addProperty("credit", String.valueOf(course.getCredit()));
            courseData.addProperty("time", course.getTime());
            courseData.addProperty("location", course.getLocation());
            courseData.addProperty("capacity", String.valueOf(course.getCapacity()));
            courseData.addProperty("property", course.getProperty());
            courseData.addProperty("validCapacity", String.valueOf(course.getvalidCapacity()));
            courseData.addProperty("major", course.getMajor());
            courseData.addProperty("validGrade", course.getvalidGrade());
            data.add("course" + i, courseData);
        }
        data.addProperty("status", "success");
        return gson.toJson(data);
    }

    //学生查看课表
    public String showStudentCourseTable(String jsonData) {
        CourseTableShowRequest request = gson.fromJson(jsonData, CourseTableShowRequest.class);
        JsonObject data = new JsonObject();
        DataBase db = DataBaseManager.getInstance();
        //在授课记录中查找到所有该学生的选课记录
        List<Enrollment> enrollments = db.getWhere(Enrollment.class, "studentid", request.getStudentId());
        if (enrollments.isEmpty()) {
            data.addProperty("status", "failed");
            data.addProperty("reason", "no course found");
            return gson.toJson(data);
        }
        data.addProperty("number", String.valueOf(enrollments.size()));
        for (int i = 0; i < enrollments.size(); i++) {
            Enrollment enrollment = enrollments.get(i);
            JsonObject courseData = new JsonObject();
            List<Course> courses = db.getWhere(Course.class, "courseId", enrollment.getcourseid());
            if (courses.isEmpty()) {
                data.addProperty("status", "failed");
                courseData.addProperty("reason", "course" + enrollment.getcourseid() + " not found");
            } else {
                Course course = courses.get(0);
                courseData.addProperty("courseName", course.getcourseName());
                courseData.addProperty("time", course.getTime());
                courseData.addProperty("location", course.getLocation());
                courseData.addProperty("semester", course.getSemester());
                courseData.addProperty("teacherName", course.getteacherName());
            }
            data.add("course" + i, courseData);
        }
        data.addProperty("status", "success");
        return gson.toJson(data);
    }

    public String modifyCourse(String jsonData) {
        CourseAddRequest request = gson.fromJson(jsonData, CourseAddRequest.class);
        JsonObject data = new JsonObject();
        DataBase db = DataBaseManager.getInstance();
        List<Course> courses = db.getWhere(Course.class, "courseId", request.getCourseId());
        if (courses.isEmpty()) {
            data.addProperty("status", "failed");
            data.addProperty("reason", "course not found");
        } else {
            Course course = courses.get(0);
            course.setcourseName(request.getCourseName());
            course.setteacherId(request.getTeacherId());
            course.setteacherName(request.getTeacherName());
            course.setCredit(Integer.valueOf(request.getCredit()));
            course.setTime(request.getTime());
            course.setLocation(request.getLocation());
            course.setCapacity(Integer.valueOf(request.getCapacity()));
            course.setMajor(request.getMajor());
            course.setvalidGrade(request.getGrade());
            course.setProperty(request.getProperty());
            course.setvalidCapacity(course.getCapacity());
            course.setSemester(course.getSemester());
            db.update(course);
            data.addProperty("status", "success");
        }
        return gson.toJson(data);
    }

    // 根据老师ID查询该老师的所有课程，并返回对应课程的学生信息
    public String getCoursesByTeacherId(String jsonData) {
        JsonObject request = gson.fromJson(jsonData, JsonObject.class);
        String teacherId = request.get("teacherId").getAsString();
        JsonObject data = new JsonObject();
        DataBase db = DataBaseManager.getInstance();

        // 查询该老师的所有课程
        List<Course> courses = db.getWhere(Course.class, "teacherId", teacherId);
        if (courses.isEmpty()) {
            data.addProperty("status", "failed");
            data.addProperty("reason", "no courses found for this teacher");
            return gson.toJson(data);
        }

        // 构建返回数据
        data.addProperty("number", String.valueOf(courses.size()));
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            JsonObject courseData = new JsonObject();
            courseData.addProperty("courseName", course.getcourseName());
            courseData.addProperty("courseId", course.getcourseId());
            courseData.addProperty("time", course.getTime());
            courseData.addProperty("location", course.getLocation());
            // 查询选修该课程的所有学生
            List<Enrollment> enrollments = db.getWhere(Enrollment.class, "courseid", course.getcourseId());
            JsonObject studentsData = new JsonObject();
            studentsData.addProperty("number", String.valueOf(enrollments.size()));
            for (int j = 0; j < enrollments.size(); j++) {
                Enrollment enrollment = enrollments.get(j);
                JsonObject studentData = new JsonObject();
                List<Student> students = db.getWhere(Student.class, "studentId", enrollment.getstudentid());
                if (!students.isEmpty()) {
                    Student student = students.get(0);
                    studentData.addProperty("studentId", student.getStudentId());
                    studentData.addProperty("name", student.getUsername());
                    studentData.addProperty("gender", String.valueOf(student.getGender()));
                    List<Score> scores = db.getWhere(Score.class, "studentId", student.getStudentId());
                    String isScored = "false";
                    String scoreStr = "";
                    String participationScoreStr = "";
                    String midtermScoreStr = "";
                    String finalScoreStr = "";
                    for (Score score : scores) {
                        if (score.getCourseId().equals(course.getcourseId())) {
                            scoreStr = String.valueOf(score.getScore());
                            participationScoreStr = String.valueOf(score.getParticipationScore());
                            midtermScoreStr = String.valueOf(score.getMidtermScore());
                            finalScoreStr = String.valueOf(score.getFinalScore());
                            isScored = "true";
                            break;
                        }
                    }
                    studentData.addProperty("score", scoreStr);
                    studentData.addProperty("ParticipationScore", participationScoreStr);
                    studentData.addProperty("MidtermScore", midtermScoreStr);
                    studentData.addProperty("FinalScore", finalScoreStr);

                    studentData.addProperty("isScored", isScored);

                }
                studentsData.add("student" + j, studentData);
            }
            courseData.add("students", studentsData);
            List<Score> scoreclass = db.getWhere(Score.class, "courseId", course.getcourseId());
            String classStatus = "";
            if (scoreclass.isEmpty()) {
                classStatus = "未提交";
            } else {
                classStatus = scoreclass.get(0).getStatus();
            }
            courseData.addProperty("classStatus", classStatus);
            data.add("course" + i, courseData);
        }
        data.addProperty("status", "success");
        return gson.toJson(data);
    }

    public String getRecordCourses(String jsonData) {
        JsonObject request = gson.fromJson(jsonData, JsonObject.class);
        String teacherId = request.get("teacherId").getAsString();
        JsonObject data = new JsonObject();
        DataBase db = DataBaseManager.getInstance();

        // 查询该老师的所有课程
        List<Course> courses = db.getWhere(Course.class, "teacherId", teacherId);
        if (courses.isEmpty()) {
            data.addProperty("status", "failed");
            data.addProperty("reason", "no courses found for this teacher");
            return gson.toJson(data);
        }

        // 构建返回数据
        data.addProperty("number", String.valueOf(courses.size()));
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            JsonObject courseData = new JsonObject();
            courseData.addProperty("courseName", course.getcourseName());
            courseData.addProperty("courseId", course.getcourseId());
            courseData.addProperty("time", course.getTime());
            courseData.addProperty("location", course.getLocation());
            // 查询选修该课程的所有video
            List<Video> videos = db.getWhere(Video.class, "courseId", course.getcourseId());
            JsonObject videosData = new JsonObject();
            videosData.addProperty("number", String.valueOf(videos.size()));
            for (int j = 0; j < videos.size(); j++) {
                Video video = videos.get(j);
                JsonObject videoData = new JsonObject();
                videoData.addProperty("videoId", video.getVideoId().toString());
                videoData.addProperty("videoName", video.getVideoName());
                videoData.addProperty("upload_Date", video.getUpload_Date().toString());

                videosData.add("video" + j, videoData);
            }
            courseData.add("videos", videosData);
            data.add("course" + i, courseData);
        }
        data.addProperty("status", "success");
        return gson.toJson(data);
    }

    public String getRecordCoursesByStudent(String jsonData) {
        JsonObject request = gson.fromJson(jsonData, JsonObject.class);
        String studentId = request.get("studentId").getAsString();
        JsonObject data = new JsonObject();
        DataBase db = DataBaseManager.getInstance();

        List<Enrollment> enrollments = db.getWhere(Enrollment.class, "studentid", studentId);

        // 查询该老师的所有课程
        List<Course> courses = new ArrayList<>();
        for (Enrollment enrollment : enrollments) {
            List<Course> thiscourse = db.getWhere(Course.class, "courseId", enrollment.getcourseid());
            courses.addAll(thiscourse);
        }

        if (courses.isEmpty()) {
            data.addProperty("status", "failed");
            data.addProperty("reason", "no courses found for this teacher");
            return gson.toJson(data);
        }

        // 构建返回数据
        data.addProperty("number", String.valueOf(courses.size()));
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            JsonObject courseData = new JsonObject();
            courseData.addProperty("courseName", course.getcourseName());
            courseData.addProperty("courseId", course.getcourseId());
            courseData.addProperty("time", course.getTime());
            courseData.addProperty("location", course.getLocation());
            // 查询选修该课程的所有video
            List<Video> videos = db.getWhere(Video.class, "courseId", course.getcourseId());
            JsonObject videosData = new JsonObject();
            videosData.addProperty("number", String.valueOf(videos.size()));
            for (int j = 0; j < videos.size(); j++) {
                Video video = videos.get(j);
                JsonObject videoData = new JsonObject();
                videoData.addProperty("videoId", video.getVideoId().toString());
                videoData.addProperty("videoName", video.getVideoName());
                videoData.addProperty("upload_Date", video.getUpload_Date().toString());

                videosData.add("video" + j, videoData);
            }
            courseData.add("videos", videosData);
            data.add("course" + i, courseData);
        }
        data.addProperty("status", "success");
        return gson.toJson(data);
    }

    public String videoUpload(String jsonData, String additionalParam) {
        JsonObject request = gson.fromJson(jsonData, JsonObject.class);
        Video video = new Video();
        List<Video> videos = DataBaseManager.getInstance().getWhere(Video.class, "courseId", request.get("courseId").getAsString());
        String videoId = UUID.randomUUID().toString();
        video.setCourseId(request.get("courseId").getAsString());
        video.setVideoName(request.get("videoName").getAsString());
        video.setVideoId(videoId);
        video.setUpload_Date(new Date());
        //筛选出一个string的纯数字部分
        String filepath = "C:\\Users\\Administrator\\Desktop\\server\\img\\" + videoId + ".mp4";
        try {
            fileOutputStream = new FileOutputStream(filepath);//指定保持路径
            byte[] bytes = Base64.getDecoder().decode(additionalParam);
            fileOutputStream.write(bytes);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DataBase db = DataBaseManager.getInstance();
        db.save(video);
        JsonObject data = new JsonObject();
        data.addProperty("status", "success");
        return gson.toJson(data);
    }
}

//    public String videoDelete(String jsonData) {
//        VideoDeleteRequest request = gson.fromJson(jsonData, VideoDeleteRequest.class);
//        JsonObject data = new JsonObject();
//        DataBase db = DataBaseManager.getInstance();
//        List<Video> videos = db.getWhere(Video.class, "videoId", request.getVideoId());
//        if (videos.isEmpty()) {
//            data.addProperty("status", "failed");
//            data.addProperty("reason", "video not found");
//        } else {
//            Video video = videos.get(0);
//            db.delete(video);
//            data.addProperty("status", "success");
//        }
//        return gson.toJson(data);
//    }
//
//    //向学生展示视频
//    public String ShowVideo(String jsonData) {
//        VideoShowRequest request = gson.fromJson(jsonData, VideoShowRequest.class);
//        JsonObject data = new JsonObject();
//        DataBase db = DataBaseManager.getInstance();
//        //查找学生的选课记录，展示所有课程对应的视频
//        List<Enrollment> enrollments = db.getWhere(Enrollment.class, "studentid", request.getStudentId());
//        if (enrollments.isEmpty()) {
//            data.addProperty("status", "failed");
//            data.addProperty("reason", "no course found");
//            return gson.toJson(data);
//        } else {
//            //按照课程传课程视频
//            for (int i = 0; i < enrollments.size(); i++) {
//                Enrollment enrollment = enrollments.get(i);
//                JsonObject courseData = new JsonObject();
//                List<Course> courses = db.getWhere(Course.class, "courseId", enrollment.getcourseid());
//                Course course = courses.get(0);
//                courseData.addProperty("courseName", course.getcourseName());
//                courseData.addProperty("teacherName", course.getteacherName());
//                //筛选出该课程的所有视频
//                List<Video> videos = db.getWhere(Video.class, "courseId", enrollment.getcourseid());
//                if (videos.isEmpty()) {
//                    courseData.addProperty("number", "0");
//                } else {
//                    courseData.addProperty("number", String.valueOf(videos.size()));
//                    for (int j = 0; j < videos.size(); j++) {
//                        Video video = videos.get(j);
//                        JsonObject videoData = new JsonObject();
//                        videoData.addProperty("videoId", String.valueOf(video.getVideoId()));
//                        videoData.addProperty("videoName", video.getVideoName());
//                        courseData.add("video" + j, videoData);
//                    }
//                }
//                data.add("course" + i, courseData);
//            }
//            data.addProperty("status", "success");
//            return gson.toJson(data);
//        }
//    }
//
//
//    //管理员查看所有视频
//    public String showVideoList(String jsonData) {
//        VideoListRequest request = gson.fromJson(jsonData, VideoListRequest.class);
//        JsonObject data = new JsonObject();
//        DataBase db = DataBaseManager.getInstance();
//        //按课程分组展示所有视频
//        List<Video> videos = db.getLike(Video.class, "videoId", "");
//        if (videos.isEmpty()) {
//            data.addProperty("status", "failed");
//            data.addProperty("reason", "no video found");
//            return gson.toJson(data);
//        }
//        //将视频按照课程号排序
//        videos.sort((o1, o2) -> o1.getCourseId().compareTo(o2.getCourseId()));
//        String tempCourseId = "";
//        int cnt = 0;
//        JsonObject videoData = new JsonObject();
//        for (int i = 0; i < videos.size(); i++) {
//            Video video = videos.get(i);
//            if (tempCourseId.equals(video.getCourseId())) {
//
//            } else {
//                if (!tempCourseId.equals("")) {
//                    data.add("course" + cnt, videoData);
//                    cnt++;
//                    tempCourseId = video.getCourseId();
//                }
//            }
//            videoData.addProperty("videoId", String.valueOf(video.getVideoId()));
//            videoData.addProperty("videoName", video.getVideoName());
//            videoData.addProperty("number", cnt);
//            data.add("video" + i, videoData);
//        }
//
//}
