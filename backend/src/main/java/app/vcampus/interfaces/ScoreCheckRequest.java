package app.vcampus.interfaces;

public class ScoreCheckRequest {
	private String userId;
    private String courseId;

    private String status;

    public ScoreCheckRequest(String userId, String courseId,String status) {
        this.userId = userId;
        this.courseId = courseId;
        this.status = status;
    }
    public String getUserId() {
        return userId;}

    public void setUserId(String userId) {
        this.userId = userId;}

    public String getCourseId() {
        return courseId;}


    public void setCourseId(String courseId) {
        this.courseId = courseId;}

    public String getStatus() {
        return status;}

    public void setStatus(String status) {
        this.status = status;}
}
