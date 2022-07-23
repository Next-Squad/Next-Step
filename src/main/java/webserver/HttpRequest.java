package webserver;

import java.net.URI;
import java.util.Objects;

public class HttpRequest {

    private final HttpMethod method;
    private final HttpHeader header;
    private final HttpRequestBody requestBody;
    private final URI uri;

    public HttpRequest(HttpMethod method, HttpHeader header, HttpRequestBody requestBody, URI uri) {
        Objects.requireNonNull(method);
        Objects.requireNonNull(header);
        Objects.requireNonNull(requestBody);
        Objects.requireNonNull(uri);

        this.method = method;
        this.header = header.clone();
        this.requestBody = requestBody.clone();
        this.uri = uri;
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
}
