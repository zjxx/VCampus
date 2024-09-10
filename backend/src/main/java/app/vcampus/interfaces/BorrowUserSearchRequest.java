package app.vcampus.interfaces;

public class BorrowUserSearchRequest {
    private String role;
    private String userId;
    private String searchId;


    public BorrowUserSearchRequest(String role, String UserId, String SearchId) {
        this.role = role;
        this.userId = UserId;
        this.searchId = SearchId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String UserId) {
        this.userId = UserId;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String SearchId) {
        this.searchId = SearchId;
    }
}
