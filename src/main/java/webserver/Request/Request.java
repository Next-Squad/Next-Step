package webserver.Request;

public class Request {

    private final RequestLine requestLine;
    private final Headers headers;

    public Request(RequestLine requestLine, Headers headers) {
        this.requestLine = requestLine;
        this.headers = headers;
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public Headers getHeaders() {
        return headers;
    }
}
