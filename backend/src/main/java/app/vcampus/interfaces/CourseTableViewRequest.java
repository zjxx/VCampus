package app.vcampus.interfaces;

public class CourseTableViewRequest {
    private String role;
    private String UserId;

    public CourseTableViewRequest(String role, String UserId) {
        this.role = role;
        this.UserId = UserId;
    }

    public String getRole() {
        return role;
    }

    public String getUserId() {
        return UserId;
    }

}
