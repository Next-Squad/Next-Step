package webserver.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpResponseModel {

    private final Map<String, Object> model = new HashMap<>();

    public void put(String key, Object value) {
        model.put(key, value);
    }

    public Object get(String key) {
        return model.get(key);
    }

    public Set<String> keySet() {
        return model.keySet();
    }

    public HttpResponseModel clone() {
        HttpResponseModel modelClone = new HttpResponseModel();
        modelClone.model.putAll(this.model);

        return modelClone;
    }
}
