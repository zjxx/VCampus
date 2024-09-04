package app.vcampus.interfaces;

public class BookListRequest {
    String role;
    String userId;

    public BookListRequest(String role, String userId) {
        this.role = role;
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public String getuserId() {
        return userId;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setuserId(String userId) {
        this.userId = userId;
    }
}
