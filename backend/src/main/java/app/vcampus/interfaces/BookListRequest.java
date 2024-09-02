package app.vcampus.interfaces;

public class BookListRequest {
    String role;
    String reader_id;

    public BookListRequest(String role, String reader_id) {
        this.role = role;
        this.reader_id = reader_id;
    }

    public String getRole() {
        return role;
    }

    public String getReader_id() {
        return reader_id;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setReader_id(String reader_id) {
        this.reader_id = reader_id;
    }
}
