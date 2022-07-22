package webserver.Request;

import webserver.Headers;

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

    public HttpMethod getHttpMethod() {
        return requestLine.getHttpMethod();
    }

    public String getUriPath() {
        return requestLine.getUri().getPath();
    }

    public String getProtocol() {
        return requestLine.getProtocol();
    }

    public boolean isResourceRequest() {
        return requestLine.getUri().isResourcePath();
    }


}
