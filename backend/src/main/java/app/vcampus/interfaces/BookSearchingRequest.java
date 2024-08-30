package app.vcampus.interfaces;

public class BookSearchingRequest {
    private String role;
    private String bookName;

    public BookSearchingRequest(String role, String bookName) {
        this.role = role;
        this.bookName = bookName;
    }
    public String getRole() {return role;}

    public void setRole(String role) {this.role = role;}

    public String getBookName() {return bookName;}

    public void setBookName(String bookName) {this.bookName = bookName;}

}
