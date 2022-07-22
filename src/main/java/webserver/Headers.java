package webserver;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Headers {

    private final Map<String, String> headers;

    public Headers(Map<String, String> headers) {
        this.headers = headers;
    }

    public static Headers emptyHeaders() {
        return new Headers(new HashMap<>());
    }

    private String getHeader(String fieldName) {
        return Optional.ofNullable(headers.get(fieldName))
                .orElseThrow(RuntimeException::new);
    }

    public void setHeader(String fieldName, String field) {
        headers.put(fieldName, field);
    }

    public void setContentLength(String contentLength) {
        headers.put("Content-Length", contentLength);
    }

}
