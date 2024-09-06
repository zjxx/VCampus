package app.vcampus.interfaces;

public class BookDeleteRequest {
    private String ISBN;
    private String userId;
    private String role;

    public BookDeleteRequest(String role, String userId, String ISBN) {
        this.userId = userId;
        this.role = role;
        this.ISBN = ISBN;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getuserId() {
        return userId;
    }

    public void setuserId(String id) {
        this.userId = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}