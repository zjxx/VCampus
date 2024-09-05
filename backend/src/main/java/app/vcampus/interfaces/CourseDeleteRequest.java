package app.vcampus.interfaces;

public class CourseDeleteRequest {
    String role;
    String userId;
    String courseId;

    public CourseDeleteRequest(String role, String userId, String courseId) {
        this.role = role;
        this.userId = userId;
        this.courseId = courseId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}
