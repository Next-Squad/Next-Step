package http.response;

import http.request.HttpRequest;
import java.util.HashMap;
import java.util.Map;

public class ResponseWriter {

    private static Map<String, Object> urlMap = new HashMap<>();
    private HelloInterface helloInterface;

    static {
        urlMap.put("/user/create", new);
        urlMap.put("/user/login", "/index.html");
        urlMap.put("/user/create", "/index.html");
    }

    public ResponseWriter(HttpRequest httpRequest) {
        this.helloInterface = something(httpRequest);
    }

    private HelloInterface something(HttpRequest httpRequest) {
        httpRequest.getMethod();
        httpRequest.getUrl();

        if (urlMap.containsKey("/user/create")) {

        }
    }


}
