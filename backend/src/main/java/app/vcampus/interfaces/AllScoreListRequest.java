package app.vcampus.interfaces;

public class AllScoreListRequest {
    private String userId;

    public AllScoreListRequest(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;  }


    public void setUserId(String userId) {
        this.userId = userId;}
}
