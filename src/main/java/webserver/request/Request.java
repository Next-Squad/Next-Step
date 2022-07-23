package webserver.request;

import java.io.BufferedReader;
import java.util.Map;

public class Request {
    private String method;
    private String uri;
    private Map<String, String> params;

    public Request(String firstline) {
        String[] splitFirstLine = firstline.split(" ");
        method = splitFirstLine[0];
        uri = splitFirstLine[1];
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public Map<String, String> getParams() {
        return params;
    }
}
