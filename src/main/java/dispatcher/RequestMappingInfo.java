package dispatcher;

import webserver.Request.HttpMethod;

import java.util.Objects;

public class RequestMappingInfo {

    private final HttpMethod httpMethod;
    private final String uriPath;

    public RequestMappingInfo(HttpMethod httpMethod, String uriPath) {
        this.httpMethod = httpMethod;
        this.uriPath = uriPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestMappingInfo that = (RequestMappingInfo) o;
        return httpMethod == that.httpMethod && Objects.equals(uriPath, that.uriPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(httpMethod, uriPath);
    }
}
