package webserver.Request;


public class RequestLine {

    private final HttpMethod httpMethod;
    private final Uri uri;
    private final String protocol;

    public RequestLine(HttpMethod httpMethod, Uri uri, String protocol) {
        this.httpMethod = httpMethod;
        this.uri = uri;
        this.protocol = protocol;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public Uri getUri() {
        return uri;
    }

    public String getProtocol() {
        return protocol;
    }
}
