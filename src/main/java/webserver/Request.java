package webserver;

import java.io.BufferedReader;

public class Request {
    private String method;
    private String uri;

    public Request(String msg) {
        String[] msgArr = msg.split(" ");
        method = msgArr[0];
        uri = msgArr[1];
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }
}
