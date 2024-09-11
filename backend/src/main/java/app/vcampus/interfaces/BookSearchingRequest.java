package app.vcampus.interfaces;

public class BookSearchingRequest {
    private String role;
    private String bookname;
    private String flag;

    public BookSearchingRequest(String role, String bookName) {
        this.role = role;
        this.bookname = bookName;
    }
    public String getRole() {return role;}

    public void setRole(String role) {this.role = role;}

    public String getBookName() {return bookname;}

    public void setBookName(String bookName) {this.bookname = bookName;}

    public String getFlag() {return flag;}

    public void setFlag(String flag) {this.flag = flag;}

}
