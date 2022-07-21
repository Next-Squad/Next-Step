package webserver.Request;


public class RequestLine {

    private final HttpMethod httpMethod;
    private final String uri;
    private final String protocol;

    public RequestLine(HttpMethod httpMethod, String uri, String protocol) {
        this.httpMethod = httpMethod;
        this.uri = uri;
        this.protocol = protocol;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getUri() {
        return uri;
    }

    public String getProtocol() {
        return protocol;
    }
}
