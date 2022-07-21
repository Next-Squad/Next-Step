package webserver.Request;


import java.util.Map;
import java.util.Optional;

public class Headers {

    private final Map<String, String> headers;

    public Headers(Map<String, String> headers) {
        this.headers = headers;
    }

    private String getHeader(String fieldName) {
        return Optional.ofNullable(headers.get(fieldName))
                .orElseThrow(RuntimeException::new);
    }

}
