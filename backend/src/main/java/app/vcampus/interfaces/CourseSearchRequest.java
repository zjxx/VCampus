package app.vcampus.interfaces;

public class CourseSearchRequest {
    private String courseName;
    private String studentId;

    public CourseSearchRequest(String courseName, String studentId) {
        this.courseName = courseName;
        this.studentId = studentId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
