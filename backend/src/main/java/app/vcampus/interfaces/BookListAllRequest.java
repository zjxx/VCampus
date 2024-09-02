package app.vcampus.interfaces;

public class BookListAllRequest {
    String role;
    String id;

    public BookListAllRequest(String role,String id)
    {
        this.role=role;
        this.id=id;
    }

    public String getRole() {
        return role;}

    public String getId() {
        return id;}

    public void setRole(String role) {
        this.role = role;}

    public void setId(String id) {
        this.id = id;}
}
