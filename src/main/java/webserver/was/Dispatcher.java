package webserver.was;

import handler.Handler;
import webserver.http.HttpMethod;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;

import java.net.URI;

public class Dispatcher {

    private static final Handler NOT_FOUND_HANDLER = request -> HttpResponse.builder()
            .setStatus(HttpStatus.NOT_FOUND)
            .build();

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
                .orElse(NOT_FOUND_HANDLER);

        HttpResponse response = handler.handle(request);

        if (response.hasViewName()) {
            byte[] view = viewResolver.resolveView(response.getViewName(), response.getModel());

            return new HttpResponse(response.getStatus(), response.getHeader(), view);
        }

        return response;
    }

    public boolean isMapped(HttpMethod method, URI uri) {
        return handlerMapping.findHandler(method, uri.getPath())
                .isPresent();
    }
}
