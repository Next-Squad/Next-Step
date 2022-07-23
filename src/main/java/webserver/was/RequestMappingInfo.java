package webserver.was;

import webserver.http.HttpMethod;

import java.util.Objects;

public record RequestMappingInfo(HttpMethod method, String url) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestMappingInfo that)) return false;
        return method() == that.method() && url().equals(that.url());
    }

    @Override
    public int hashCode() {
        return Objects.hash(method(), url());
    }
}
