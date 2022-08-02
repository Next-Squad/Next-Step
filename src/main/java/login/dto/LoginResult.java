package login.dto;

public class LoginResult {

    private String url;
    private boolean logined;

    public LoginResult(String url, boolean logined) {
        this.url = url;
        this.logined = logined;
    }

    public String getUrl() {
        return url;
    }

    public boolean isLogined() {
        return logined;
    }
}
