package webserver;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Header {

    private final Map<String, String> headers;

    public Header(Map<String, String> headers) {
        this.headers = headers;
    }

    public static Header emptyHeaders() {
        return new Header(new HashMap<>());
    }

    private String getHeader(String fieldName) {
        return Optional.ofNullable(headers.get(fieldName))
                .orElseThrow(RuntimeException::new);
    }

    public void setHeader(String fieldName, String field) {
        headers.put(fieldName, field);
    }

    public void setContentLength(String contentLength) {
        setHeader("Content-Length", contentLength);
    }

    public void setContentType(ContentType contentType) {
        String type = contentType.getType();

        if (contentType.equals(ContentType.HTML)) {
            setHeader("Content-Type", type + ";" + "charset=utf-8");
        } else {
            setHeader("Content-Type", type);
        }

    }

    public void setLocation(String location) {
        setHeader("Location", location);
    }

    public String getStringHeaders() {
        StringBuilder stringBuilder = new StringBuilder();

        for (String key : headers.keySet()) {
            stringBuilder.append(key).append(": ").append(headers.get(key)).append("\r\n");
        }

        return stringBuilder.toString();
    }



}
