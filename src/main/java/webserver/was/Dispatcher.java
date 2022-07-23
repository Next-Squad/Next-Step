package webserver.was;

import handler.Handler;
import webserver.http.HttpMethod;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.net.URI;

public class Dispatcher {

    private final HandlerMapping handlerMapping;

    public Dispatcher(HandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    public HttpResponse handlerRequest(HttpRequest request) {
        HttpMethod method = request.getMethod();
        String path = request.getUri().getPath();

        Handler handler = handlerMapping.findHandler(method, path)
                .orElseGet(() -> (req) -> HttpResponse.NOT_FOUNT_RESPONSE);

        return handler.handle(request);
    }

    public boolean isMapped(HttpMethod method, URI uri) {
        return handlerMapping.findHandler(method, uri.getPath())
                .isPresent();
    }
}
