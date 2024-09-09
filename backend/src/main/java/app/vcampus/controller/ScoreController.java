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
        //查看该学生是否已经提交过该课程的成绩
        List<Score> scores = db.getWhere(Score.class, "courseId", request.getCourseId());
        if (scores.isEmpty() || scores.stream().noneMatch(score -> score.getStudentId().equals(request.getStudentId()))) {
            try {
                // 获取课程并保存成绩
                Course myCourse = courses.stream().filter(course -> course.getcourseId().equals(request.getCourseId())).findFirst().orElse(null);//找到课程
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
                throw new RuntimeException(e);
            }
        }
        //如果该学生已经提交过该课程的成绩，检查成绩的status是不是审核未通过，如果是，则更新成绩，否则不更新
        else if(scores.stream().anyMatch(score -> score.getStudentId().equals(request.getStudentId()) && score.getStatus().equals("审核未通过")))
        {
            // 获取课程并更新成绩
            Course myCourse = courses.stream().filter(course -> course.getcourseId().equals(request.getCourseId())).findFirst().orElse(null);//找到课程
            if (myCourse != null) {
                Score score = scores.stream().filter(score1 -> score1.getStudentId().equals(request.getStudentId())).findFirst().orElse(null);
                if (score != null) {
                    score.setParticipationScore(Integer.parseInt(request.getParticipationScore()));
                    score.setMidtermScore(Integer.parseInt(request.getMidtermScore()));
                    score.setFinalScore(Integer.parseInt(request.getFinalScore()));
                    score.setScore(Integer.parseInt(request.getScore()));
                    score.setCredit(myCourse.getCredit());
                    score.setSemester(myCourse.getSemester());
                    score.setStatus("未审核");
                    db.update(score);
                    data.addProperty("status", "success");
                } else {
                    data.addProperty("status", "failed");
                    data.addProperty("reason", "score not found");
                }
            } else {
                data.addProperty("status", "failed");
                data.addProperty("reason", "course not found");
            }
        }
        else {
            data.addProperty("status", "failed");
            data.addProperty("reason", "student has already submitted the score");
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
            int sum=0;
            for (int i = 0; i < scores.size(); i++) {
                if(scores.get(i).getStatus()!="审核通过"){
                    sum++;
                }
            }
            data.addProperty("number", String.valueOf(sum));
            sum=0;
            //循环判断每一个成绩的status是否为审核通过
            for (int i = 0; i < scores.size(); i++) {

                Score score = scores.get(i);
                if (score.getStatus().equals("审核通过")) {
                    JsonObject scoreData = new JsonObject();
                    String partScore = "";
                    String midScore = "";
                    String finalScore = "";
                    String scoreStr = "";
                    partScore = String.valueOf(score.getParticipationScore());
                    midScore = String.valueOf(score.getMidtermScore());
                    finalScore = String.valueOf(score.getFinalScore());
                    scoreStr = String.valueOf(score.getScore());
                    List<Course> courses = db.getWhere(Course.class, "courseId", score.getCourseId());
                    scoreData.addProperty("courseName", courses.get(0).getcourseName());
                    scoreData.addProperty("courseId", score.getCourseId());
                    scoreData.addProperty("semester", score.getSemester());
                    scoreData.addProperty("credit", String.valueOf(score.getCredit()));
                    scoreData.addProperty("participationScore", partScore);
                    scoreData.addProperty("midtermScore", midScore);
                    scoreData.addProperty("finalScore", finalScore);
                    scoreData.addProperty("score", scoreStr);
                    scoreData.addProperty("status", score.getStatus());
                    data.add("score" + sum, scoreData);
                    sum++;
                }
            }
            data.addProperty("status", "success");
            return gson.toJson(data);
        }
    }

    //教务查看未审核成绩
    public String listAllScore(String jsonData) {
        JsonObject request = gson.fromJson(jsonData, JsonObject.class);
        JsonObject data = new JsonObject();
        DataBase db = DataBaseManager.getInstance();

        // 查询该老师的所有课程
        List<Course> courses = db.getLike(Course.class, "teacherId", "");
        if (courses.isEmpty()) {
            data.addProperty("status", "failed");
            data.addProperty("reason", "no courses found for this teacher");
            return gson.toJson(data);
        }

        // 构建返回数据
        int number=0;
        for (Course course : courses) {
            List<Score> scores = db.getWhere(Score.class, "courseId", course.getcourseId());
            if(scores.isEmpty()){
                continue;
            }
            if(!scores.get(0).getStatus().equals("未审核")){
                continue;
            }
            number++;
        }

        data.addProperty("number", String.valueOf(number));
        number=0;
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            JsonObject courseData = new JsonObject();
            courseData.addProperty("courseName", course.getcourseName());
            courseData.addProperty("courseId", course.getcourseId());
            courseData.addProperty("time", course.getTime());
            courseData.addProperty("location", course.getLocation());
            List<Score> scoresofclass = db.getWhere(Score.class, "courseId", course.getcourseId());
            if(scoresofclass.isEmpty()){
                continue;
            }
            if(!scoresofclass.get(0).getStatus().equals("未审核")){
                continue;
            }
            number++;
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
            data.add("course" + (number-1), courseData);
        }
        data.addProperty("status", "success");

        return gson.toJson(data);
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
                //保存修改
                db.save(score);
                data.addProperty("status", "success");
            }
        }
        return gson.toJson(data);
    }
}




