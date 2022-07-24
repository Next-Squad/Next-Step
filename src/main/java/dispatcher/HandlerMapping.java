package dispatcher;

import handler.Handler;
import webserver.Request.Request;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;


public class HandlerMapping {

    private final Map<RequestMappingInfo, Handler> handlers;

    public HandlerMapping(Map<RequestMappingInfo, Handler> handlers) {
        this.handlers = handlers;
    }

    public Handler matchHandler(Request request) {
        RequestMappingInfo mappingRegistry = new RequestMappingInfo(request.getHttpMethod(), request.getUriPath());

        return Optional.ofNullable(handlers.get(mappingRegistry)).orElseThrow(NoSuchElementException::new);
    }
}
