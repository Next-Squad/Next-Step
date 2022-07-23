package webserver.response;

public enum StatusCode {
    OK(200),
    REDIRECT(302);


    private int codeNumber;

    private StatusCode(int codeNumber) {
        this.codeNumber = codeNumber;
    }

    public int getCodeNumber() {
        return codeNumber;
    }


}
