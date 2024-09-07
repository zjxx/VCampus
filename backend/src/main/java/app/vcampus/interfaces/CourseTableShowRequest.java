package app.vcampus.interfaces;

public class CourseTableShowRequest {
    private String studentId;



    public CourseTableShowRequest(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

}
