package next.web;

import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Objects;

public class HandlerKey {

    private final String path;
    private final RequestMethod requestMethod;

    public HandlerKey(String path, RequestMethod requestMethod) {
        this.path = path;
        this.requestMethod = requestMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HandlerKey that = (HandlerKey) o;
        return path.equals(that.path) && requestMethod == that.requestMethod;
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, requestMethod);
    }
}
