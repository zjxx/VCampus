package app.vcampus.interfaces;

public class MyCourseScoreListRequest {
    private String teacherId;

    public MyCourseScoreListRequest(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherId() {
        return teacherId;}

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }
}
