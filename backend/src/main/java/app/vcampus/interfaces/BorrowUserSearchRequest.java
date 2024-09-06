package app.vcampus.interfaces;

public class BorrowUserSearchRequest {
    private String role;
    private String UserId;
    private String SearchId;


    public BorrowUserSearchRequest(String role, String UserId, String SearchId) {
        this.role = role;
        this.UserId = UserId;
        this.SearchId = SearchId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getSearchId() {
        return SearchId;
    }

    public void setSearchId(String SearchId) {
        this.SearchId = SearchId;
    }
}
