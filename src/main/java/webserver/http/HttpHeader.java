package webserver.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpHeader {

    public static final String CONTENT_LENGTH = "Content-Length";

    private final Map<String, String> header = new HashMap<>();

    public void add(String key, String value) {
        header.put(key, value);
    }

    public Set<String> keySet() {
        return header.keySet();
    }

    public String get(String key) {
        return header.get(key);
    }

    public HttpHeader clone() {
        HttpHeader httpHeaderClone = new HttpHeader();
        httpHeaderClone.header.putAll(this.header);

        return httpHeaderClone;
    }
}
