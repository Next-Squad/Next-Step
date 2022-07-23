package webserver.Request;

import util.HttpRequestUtils;

import java.util.Map;

public class RequestParams {

    private Map<String, String> params;

    public RequestParams(String query) {
        this.params = HttpRequestUtils.parseQueryString(query);
    }

    public String getParam(String key) {
        return params.getOrDefault(key, null);
    }
}
