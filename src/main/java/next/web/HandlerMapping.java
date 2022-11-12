package next.web;

import java.lang.reflect.Method;
import java.util.Map;

public class HandlerMapping {

    private final Map<HandlerKey, HandlerMethod> handlers;

    public HandlerMapping(Map<HandlerKey, HandlerMethod> handlers) {
        this.handlers = handlers;
    }

    public HandlerMethod getHandlerMethod(HandlerKey handlerKey) {
        return handlers.get(handlerKey);
    }
}
