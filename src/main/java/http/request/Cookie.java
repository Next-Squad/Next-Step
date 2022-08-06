package http.request;

public class Cookie {

    private String isLogined;
    private String path;

    public Cookie(String isLogined, String path) {
        this.isLogined = isLogined; // logined=true; path=/;
        this.path = path;
    }

    @Override
    public String toString() {
        return isLogined + " " + path;
    }

    public String getIsLogined() {
        return isLogined;
    }
}
