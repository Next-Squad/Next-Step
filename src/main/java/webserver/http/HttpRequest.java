package webserver.http;

import util.HttpRequestUtils;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HttpRequest {

    private final HttpMethod method;
    private final HttpHeader header;
    private final HttpRequestBody requestBody;
    private final URI uri;

    private final Map<String, String> cookieMap = new HashMap<>();

    public HttpRequest(HttpMethod method, HttpHeader header, HttpRequestBody requestBody, URI uri) {
        Objects.requireNonNull(method);
        Objects.requireNonNull(header);
        Objects.requireNonNull(requestBody);
        Objects.requireNonNull(uri);

        this.method = method;
        this.header = header.clone();
        this.requestBody = requestBody.clone();
        this.uri = uri;

        if (header.keySet().contains("Cookie")) {
            String cookies = header.get("Cookie");
            cookieMap.putAll(HttpRequestUtils.parseCookies(cookies));
        }
    }

    public static HttpRequest from(HttpRequestMessage requestMessage) {
        return new HttpRequest(requestMessage.method(),
                requestMessage.header(),
                requestMessage.body(),
                requestMessage.uri());
    }

    public HttpMethod getMethod() {
        return method;
    }

    public HttpHeader getHeader() {
        return this.header.clone();
    }

    public HttpRequestBody getRequestBody() {
        return this.requestBody.clone();
    }

    public URI getUri() {
        return uri;
    }

    public String getCookie(String key) {
        return cookieMap.get(key);
    }
}
