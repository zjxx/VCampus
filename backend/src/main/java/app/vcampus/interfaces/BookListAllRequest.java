package app.vcampus.interfaces;

public class BookListAllRequest {
    String role;
    String userId;

    public BookListAllRequest(String role,String id)
    {
        this.role=role;
        this.userId=id;
    }

    public String getRole() {
        return role;}

    public String getuserId() {
        return userId;}

    public void setRole(String role) {
        this.role = role;}

    public void setuserId(String id) {
        this.userId = id;}
}
