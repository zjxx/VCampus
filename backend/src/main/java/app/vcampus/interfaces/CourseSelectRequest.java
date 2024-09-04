package app.vcampus.interfaces;

public class CourseSelectRequest {
    //选课请求
    private String courseId;
    private String studentId;

    public CourseSelectRequest(String courseId, String studentId) {
        this.courseId = courseId;
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String Id) {
        this.studentId = studentId;
    }

}
