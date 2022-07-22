package dispatcher;

import handler.Handler;
import webserver.Request.Request;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;


public class HandlerMapping {

    private final Map<MappingRegistry, Handler> handlers;

    public HandlerMapping(Map<MappingRegistry, Handler> handlers) {
        this.handlers = handlers;
    }

    public Handler matchHandler(Request request) {
        MappingRegistry mappingRegistry = new MappingRegistry(request.getHttpMethod(), request.getUriPath());

        return Optional.ofNullable(handlers.get(mappingRegistry)).orElseThrow(NoSuchElementException::new);
    }
}
