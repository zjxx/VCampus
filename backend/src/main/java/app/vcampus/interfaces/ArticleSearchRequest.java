package app.vcampus.interfaces;

public class ArticleSearchRequest {
    private String role;
    private String articleName;

    public ArticleSearchRequest(String role, String articleName) {
        this.role = role;
        this.articleName = articleName;
    }

    public String getRole() {
        return role;
    }
    public String getArticleName() {
        return articleName;
    }
}
