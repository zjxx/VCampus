package app.vcampus.controller;

import app.vcampus.domain.Course;
import app.vcampus.domain.Enrollment;
import app.vcampus.domain.Score;
import app.vcampus.domain.Student;
import app.vcampus.interfaces.*;
import app.vcampus.utils.DataBase;
import app.vcampus.utils.DataBaseManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.stream.Collectors;

public class ScoreController {
    private final Gson gson = new Gson();

    //教师打分
    public String giveScore(String jsonData) {
        ScoreGiveRequest request = gson.fromJson(jsonData, ScoreGiveRequest.class);
        JsonObject data = new JsonObject();
        DataBase db = DataBaseManager.getInstance();

        // 检查教师是否教授该课程
        List<Course> courses = db.getWhere(Course.class, "teacherId", request.getTeacherId());
        if (courses.isEmpty() || courses.stream().noneMatch(course -> course.getcourseId().equals(request.getCourseId()))) {
            data.addProperty("status", "failed");
            data.addProperty("reason", "teacher is not the course's teacher");
            return gson.toJson(data);
        }

        // 检查学生是否参加该课程
        List<Enrollment> enrollments = db.getWhere(Enrollment.class, "studentid", request.getStudentId());
        if (enrollments.isEmpty() || enrollments.stream().noneMatch(enrollment -> enrollment.getcourseid().equals(request.getCourseId()))) {
            data.addProperty("status", "failed");
            data.addProperty("reason", "student is not enrolled in the course");
            return gson.toJson(data);
        }

        try {
            // 获取课程并保存成绩
            Course myCourse = courses.stream().filter(course -> course.getcourseId().equals(request.getCourseId())).findFirst().orElse(null);
            if (myCourse != null) {
                Score score = new Score();
                score.setCourseId(request.getCourseId());
                score.setStudentId(request.getStudentId());
                score.setParticipationScore(Integer.parseInt(request.getParticipationScore()));
                score.setMidtermScore(Integer.parseInt(request.getMidtermScore()));
                score.setFinalScore(Integer.parseInt(request.getFinalScore()));
                score.setScore(Integer.parseInt(request.getScore()));
                score.setCredit(myCourse.getCredit());
                score.setSemester(myCourse.getSemester());
                score.setStatus("未审核");
                db.save(score);
                data.addProperty("status", "success");
            } else {
                data.addProperty("status", "failed");
                data.addProperty("reason", "course not found");
            }
        } catch (NumberFormatException e) {
            data.addProperty("status", "failed");
            data.addProperty("reason", "invalid score format");
        } catch (Exception e) {
            data.addProperty("status", "failed");
            data.addProperty("reason", "unexpected error");
        }

        return gson.toJson(data);
    }

    //学生查看成绩
    public String viewAllScore(String jsonData) {
        ScoreViewAllRequest request = gson.fromJson(jsonData, ScoreViewAllRequest.class);
        JsonObject data = new JsonObject();
        DataBase db = DataBaseManager.getInstance();
        List<Score> scores = db.getWhere(Score.class, "studentId", request.getStudentId());
        if (scores.isEmpty()) {
            data.addProperty("status", "failed");
            data.addProperty("reason", "no score found");
            return gson.toJson(data);
        } else {
            //循环判断每一个成绩的status是否为审核通过
            for (int i = 0; i < scores.size(); i++) {
                Score score = scores.get(i);
                JsonObject scoreData = new JsonObject();
                if (score.getStatus().equals("审核通过")) {
                    scoreData.addProperty("courseId", score.getCourseId());
                    scoreData.addProperty("semester", score.getSemester());
                    scoreData.addProperty("credit", score.getCredit());
                    scoreData.addProperty("participationScore", score.getParticipationScore());
                    scoreData.addProperty("midtermScore", score.getMidtermScore());
                    scoreData.addProperty("finalScore", score.getFinalScore());
                    scoreData.addProperty("score", score.getScore());
                    data.add("score" + i, scoreData);
                } else {
                    //如果审核未通过则显示该课程成绩没出
                    scoreData.addProperty("courseId", score.getCourseId());
                    scoreData.addProperty("semester", score.getSemester());
                    scoreData.addProperty("credit", score.getCredit());
                    scoreData.addProperty("message", "score not out yet");
                    data.add("score" + i, scoreData);
                }
            }
            data.addProperty("status", "success");
            return gson.toJson(data);
        }
    }

    //教务查看未审核成绩
    public String listAllScore(String jsonData) {
        AllScoreListRequest request = gson.fromJson(jsonData, AllScoreListRequest.class);
        JsonObject data = new JsonObject();
        DataBase db = DataBaseManager.getInstance();
        List<Score> scores = db.getWhere(Score.class, "status", "未审核");
        if (scores.isEmpty()) {
            data.addProperty("status", "failed");
            data.addProperty("reason", "no score found");
            return gson.toJson(data);
        } else {
            //按课程号对成绩排序
            scores.sort((score1, score2) -> score1.getCourseId().compareTo(score2.getCourseId()));
            String tempCourseId = "";
            int cnt = 0;
            JsonObject courseScoreData = new JsonObject();
            for (int i = 0; i < scores.size(); i++) {
                Score score = scores.get(i);
                if (!score.getCourseId().equals(tempCourseId)) {
                    data.add("course" + i, courseScoreData);
                    courseScoreData = new JsonObject();
                    JsonObject studentScore = new JsonObject();
                    studentScore.addProperty("courseId", score.getCourseId());
                    studentScore.addProperty("studentId", score.getStudentId());
                    //学生姓名
                    List<Student> students = db.getWhere(Student.class, "studentId", score.getStudentId());
                    Student student = students.get(0);
                    studentScore.addProperty("studentName", student.getUsername());
                    studentScore.addProperty("participationScore", score.getParticipationScore());
                    studentScore.addProperty("midtermScore", score.getMidtermScore());
                    studentScore.addProperty("finalScore", score.getFinalScore());
                    studentScore.addProperty("score", score.getScore());
                    studentScore.addProperty("credit", score.getCredit());
                    studentScore.addProperty("semester", score.getSemester());
                    studentScore.addProperty("status", score.getStatus());
                    tempCourseId = score.getCourseId();
                    cnt = 1;
                    courseScoreData.add("student" + cnt, studentScore);
                } else {
                    JsonObject studentScore = new JsonObject();
                    studentScore.addProperty("courseId", score.getCourseId());
                    studentScore.addProperty("studentId", score.getStudentId());
                    //学生姓名
                    List<Student> students = db.getWhere(Student.class, "studentId", score.getStudentId());
                    Student student = students.get(0);
                    studentScore.addProperty("studentName", student.getUsername());
                    studentScore.addProperty("participationScore", score.getParticipationScore());
                    studentScore.addProperty("midtermScore", score.getMidtermScore());
                    studentScore.addProperty("finalScore", score.getFinalScore());
                    studentScore.addProperty("score", score.getScore());
                    studentScore.addProperty("credit", score.getCredit());
                    studentScore.addProperty("semester", score.getSemester());
                    studentScore.addProperty("status", score.getStatus());
                    tempCourseId = score.getCourseId();
                    cnt += 1;
                    courseScoreData.add("student" + cnt, studentScore);
                }
            }
            //添加最后一个课程的成绩
            data.add("course" + scores.size(), courseScoreData);
            data.addProperty("status", "success");
            return gson.toJson(data);
        }
    }

    //教务审核成绩
    public String checkScore(String jsonData) {
        ScoreCheckRequest request = gson.fromJson(jsonData, ScoreCheckRequest.class);
        JsonObject data = new JsonObject();
        DataBase db = DataBaseManager.getInstance();
        List<Score> scores = db.getWhere(Score.class, "courseId", request.getCourseId());
        if (scores.isEmpty()) {
            data.addProperty("status", "failed");
            data.addProperty("reason", "no score found");
            return gson.toJson(data);
        } else {
            //如果request的status为"审核通过"，则修改成绩的status为"审核通过"
            if (request.getStatus().equals("审核通过")) {
                for (Score score : scores) {
                    score.setStatus("审核通过");
                    db.save(score);
                }
                data.addProperty("status", "success");
                data.addProperty("message", "score is passed");
                return gson.toJson(data);
            }
            //如果request的status为"未通过"，则修改成绩的status为"未通过"
            else if (request.getStatus().equals("审核未通过")) {
                for (Score score : scores) {
                    score.setStatus("审核未通过");
                    db.save(score);
                }
                data.addProperty("status", "success");
                data.addProperty("message", "score is not passed");
                return gson.toJson(data);
            } else {
                data.addProperty("status", "failed");
                data.addProperty("reason", "invalid status");
                return gson.toJson(data);
            }
        }
    }

    //教师登录后显示所有自己的课程成绩，未审核和审核分开显示
    public String ViewMyCourseScore(String jsonData) {
        MyCourseScoreListRequest request = gson.fromJson(jsonData, MyCourseScoreListRequest.class);
        JsonObject data = new JsonObject();
        DataBase db = DataBaseManager.getInstance();
        JsonObject unpassedData = new JsonObject();
        JsonObject passedData = new JsonObject();
        List<Score> scores = db.getWhere(Score.class, "status", "审核未通过");
        if (scores.isEmpty()) {
            unpassedData.addProperty("status", "failed");
            unpassedData.addProperty("reason", "no unpassed score found");
            data.add("unpassed", unpassedData);
        } else {
            //选出自己授课的课程成绩
            List<Score> myScores = scores.stream().filter(score -> score.getTeacherId().equals(request.getTeacherId())).collect(Collectors.toList());
            if (myScores.isEmpty()) {
                unpassedData.addProperty("status", "failed");
                unpassedData.addProperty("reason", "no unpassed score found in your course");
            } else {
                //按课程号对成绩排序
                myScores.sort((score1, score2) -> score1.getCourseId().compareTo(score2.getCourseId()));
                String tempCourseId = "";
                int cnt = 0;
                JsonObject courseScoreData = new JsonObject();
                for (int i = 0; i < myScores.size(); i++) {
                    Score score = myScores.get(i);
                    if (!score.getCourseId().equals(tempCourseId)) {
                        unpassedData.add("course" + i, courseScoreData);
                        courseScoreData = new JsonObject();
                        JsonObject studentScore = new JsonObject();
                        studentScore.addProperty("courseId", score.getCourseId());
                        studentScore.addProperty("studentId", score.getStudentId());
                        //学生姓名
                        List<Student> students = db.getWhere(Student.class, "studentId", score.getStudentId());
                        Student student = students.get(0);
                        studentScore.addProperty("studentName", student.getUsername());
                        studentScore.addProperty("participationScore", score.getParticipationScore());
                        studentScore.addProperty("midtermScore", score.getMidtermScore());
                        studentScore.addProperty("finalScore", score.getFinalScore());
                        studentScore.addProperty("score", score.getScore());
                        studentScore.addProperty("credit", score.getCredit());
                        studentScore.addProperty("semester", score.getSemester());
                        studentScore.addProperty("status", score.getStatus());
                        tempCourseId = score.getCourseId();
                        cnt = 1;
                        courseScoreData.add("student" + cnt, studentScore);
                    } else {
                        JsonObject studentScore = new JsonObject();
                        studentScore.addProperty("courseId", score.getCourseId());
                        studentScore.addProperty("studentId", score.getStudentId());
                        //学生姓名
                        List<Student> students = db.getWhere(Student.class, "studentId", score.getStudentId());
                        Student student = students.get(0);
                        studentScore.addProperty("studentName", student.getUsername());
                        studentScore.addProperty("participationScore", score.getParticipationScore());
                        studentScore.addProperty("midtermScore", score.getMidtermScore());
                        studentScore.addProperty("finalScore", score.getFinalScore());
                        studentScore.addProperty("score", score.getScore());
                        studentScore.addProperty("credit", score.getCredit());
                        studentScore.addProperty("semester", score.getSemester());
                        studentScore.addProperty("status", score.getStatus());
                        tempCourseId = score.getCourseId();
                        cnt += 1;
                        courseScoreData.add("student" + cnt, studentScore);
                    }
                }
                //添加最后一个课程的成绩
                unpassedData.add("course" + myScores.size(), courseScoreData);
                unpassedData.addProperty("status", "success");
            }
        }
        scores = db.getWhere(Score.class, "status", "审核通过");
        if (scores.isEmpty()) {
            unpassedData.addProperty("status", "failed");
            unpassedData.addProperty("reason", "no unpassed score found");
            data.add("passed", passedData);
        } else {
            //选出自己授课的课程成绩
            List<Score> myScores = scores.stream().filter(score -> score.getTeacherId().equals(request.getTeacherId())).collect(Collectors.toList());
            if (myScores.isEmpty()) {
                unpassedData.addProperty("status", "failed");
                unpassedData.addProperty("reason", "no unpassed score found in your course");
            } else {
                //按课程号对成绩排序
                myScores.sort((score1, score2) -> score1.getCourseId().compareTo(score2.getCourseId()));
                String tempCourseId = "";
                int cnt = 0;
                JsonObject courseScoreData = new JsonObject();
                for (int i = 0; i < myScores.size(); i++) {
                    Score score = myScores.get(i);
                    if (!score.getCourseId().equals(tempCourseId)) {
                        unpassedData.add("course" + i, courseScoreData);
                        courseScoreData = new JsonObject();
                        JsonObject studentScore = new JsonObject();
                        studentScore.addProperty("courseId", score.getCourseId());
                        studentScore.addProperty("studentId", score.getStudentId());
                        //学生姓名
                        List<Student> students = db.getWhere(Student.class, "studentId", score.getStudentId());
                        Student student = students.get(0);
                        studentScore.addProperty("studentName", student.getUsername());
                        studentScore.addProperty("participationScore", score.getParticipationScore());
                        studentScore.addProperty("midtermScore", score.getMidtermScore());
                        studentScore.addProperty("finalScore", score.getFinalScore());
                        studentScore.addProperty("score", score.getScore());
                        studentScore.addProperty("credit", score.getCredit());
                        studentScore.addProperty("semester", score.getSemester());
                        studentScore.addProperty("status", score.getStatus());
                        tempCourseId = score.getCourseId();
                        cnt = 1;
                        courseScoreData.add("student" + cnt, studentScore);
                    } else {
                        JsonObject studentScore = new JsonObject();
                        studentScore.addProperty("courseId", score.getCourseId());
                        studentScore.addProperty("studentId", score.getStudentId());
                        //学生姓名
                        List<Student> students = db.getWhere(Student.class, "studentId", score.getStudentId());
                        Student student = students.get(0);
                        studentScore.addProperty("studentName", student.getUsername());
                        studentScore.addProperty("participationScore", score.getParticipationScore());
                        studentScore.addProperty("midtermScore", score.getMidtermScore());
                        studentScore.addProperty("finalScore", score.getFinalScore());
                        studentScore.addProperty("score", score.getScore());
                        studentScore.addProperty("credit", score.getCredit());
                        studentScore.addProperty("semester", score.getSemester());
                        studentScore.addProperty("status", score.getStatus());
                        tempCourseId = score.getCourseId();
                        cnt += 1;
                        courseScoreData.add("student" + cnt, studentScore);
                    }
                }
                //添加最后一个课程的成绩
                unpassedData.add("course" + myScores.size(), courseScoreData);
                unpassedData.addProperty("status", "success");
            }
        }
        return gson.toJson(data);
    }

    //教师修正成绩
    public String modifyScore(String jsonData) {
        ScoreModifyRequest request = gson.fromJson(jsonData, ScoreModifyRequest.class);
        JsonObject data = new JsonObject();
        DataBase db = DataBaseManager.getInstance();
        List<Score> scores = db.getWhere(Score.class, "courseId", request.getCourseId());
        if (scores.isEmpty()) {
            data.addProperty("status", "failed");
            data.addProperty("reason", "no score found");
        } else {
            //在scores中找到request的学生的成绩
            Score score = scores.stream().filter(s -> s.getStudentId().equals(request.getStudentId())).findFirst().orElse(null);
            if (score == null) {
                data.addProperty("status", "failed");
                data.addProperty("reason", "student not found in course");
            } else {
                //修改成绩
                score.setParticipationScore(Integer.parseInt(request.getParticipationScore()));
                score.setMidtermScore(Integer.parseInt(request.getMidtermScore()));
                score.setFinalScore(Integer.parseInt(request.getFinalScore()));
                score.setScore(Integer.parseInt(request.getScore()));
                score.setStatus("未审核");
                db.save(score);
                data.addProperty("status", "success");
            }
        }
        return gson.toJson(data);
    }
}




