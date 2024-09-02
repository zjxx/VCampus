package app.vcampus.interfaces;

public class BookReturnRequest {
    private String ISBN;
    private String id;
    private String role;

    public BookReturnRequest(String role, String id, String ISBN) {
        this.id = id;
        this.role = role;
        this.ISBN = ISBN;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
