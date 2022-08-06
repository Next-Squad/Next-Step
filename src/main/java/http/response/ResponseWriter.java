package http.response;

import http.request.Controller;
import http.request.HttpRequest;
import java.util.HashMap;
import java.util.Map;

public class ResponseWriter {

    private static Map<String, Object> urlMap = new HashMap<>();
    private Controller responseAdapter;



    public ResponseWriter(HttpRequest httpRequest) {
        this.helloInterface = something(httpRequest);
    }

    private Controller something(HttpRequest httpRequest) {
        httpRequest.getMethod();
        httpRequest.getUrl();

        if (urlMap.containsKey("/user/create")) {

        }
    }


}
