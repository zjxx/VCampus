package app.vcampus.interfaces;

public class EnrollmentShowRequest {
    String studentId;

    public EnrollmentShowRequest(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
