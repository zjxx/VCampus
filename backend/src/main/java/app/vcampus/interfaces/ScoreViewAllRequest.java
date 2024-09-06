package app.vcampus.interfaces;

public class ScoreViewAllRequest {
    String studentId;

    public ScoreViewAllRequest(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentId() {
        return studentId;}

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
