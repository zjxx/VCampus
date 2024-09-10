package app.vcampus.interfaces;

public class VideoShowRequest {
    private String studentId;
    public VideoShowRequest()
    {
        this.studentId = "";
    }
    public String getStudentId() {
        return studentId;
    }
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
