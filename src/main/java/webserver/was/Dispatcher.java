package webserver.was;

import handler.Handler;
import webserver.http.HttpMethod;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.net.URI;

public class Dispatcher {

    private final HandlerMapping handlerMapping;
    private final ViewResolver viewResolver;

    public Dispatcher(HandlerMapping handlerMapping, ViewResolver viewResolver) {
        this.handlerMapping = handlerMapping;
        this.viewResolver = viewResolver;
    }

    public HttpResponse handlerRequest(HttpRequest request) {
        HttpMethod method = request.getMethod();
        String path = request.getUri().getPath();

        Handler handler = handlerMapping.findHandler(method, path)
                .orElseGet(() -> (req) -> HttpResponse.NOT_FOUNT_RESPONSE);

        HttpResponse response = handler.handle(request);

        if (response.hasViewName()) {
            byte[] view = viewResolver.resolveView(response.getViewName());

            return new HttpResponse(response.getStatus(), response.getHeader(), view);
        }

        return response;
    }

    public boolean isMapped(HttpMethod method, URI uri) {
        return handlerMapping.findHandler(method, uri.getPath())
                .isPresent();
    }
}
