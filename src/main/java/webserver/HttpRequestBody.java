package webserver;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpRequestBody {
    private final Map<String, String> body = new HashMap<>();

    public void add(String key, String value) {
        this.body.put(key, value);
    }

    public void addAll(Map<String, String> header) {
        this.body.putAll(header);
    }

    public Set<String> keySet() {
        return this.body.keySet();
    }

    public String get(String key) {
        return this.body.get(key);
    }

    public HttpRequestBody clone() {
        HttpRequestBody requestBodyClone = new HttpRequestBody();
        requestBodyClone.body.putAll(this.body);

        return requestBodyClone;
    }
}
