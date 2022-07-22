package webserver;

public enum HttpStatus {

    OK(200, "OK"),
    FOUND(302, "FOUND");

    private final int code;
    private final String status;

    HttpStatus(int code, String status) {
        this.code = code;
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }
}
