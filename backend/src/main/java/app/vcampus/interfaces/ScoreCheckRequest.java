package app.vcampus.interfaces;

public class ScoreCheckRequest {
	private String userId;
    private String courseId;

    private String classStatus;

    public ScoreCheckRequest(String userId, String courseId,String classStatus) {
        this.userId = userId;
        this.courseId = courseId;
        this.classStatus = classStatus;
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
        return classStatus;}

    public void setStatus(String classStatus) {
        this.classStatus = classStatus;}
}
