package webserver.Response;

public enum HttpStatus {

    OK("OK", 200), BAD_REQUEST("Bad request", 400), REDIRECT("Found", 302);

    private final String message;
    private final int code;

    HttpStatus(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code + " " + message;
    }
}
