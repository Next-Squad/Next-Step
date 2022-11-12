package dispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.Request.Request;
import webserver.Response.Response;

import java.lang.reflect.InvocationTargetException;

public class FrontControllerV2 {
    private static final Logger log = LoggerFactory.getLogger(FrontControllerV2.class);
    private final HandlerMappingV2 handlerMappingV2;

    public FrontControllerV2(HandlerMappingV2 handlerMappingV2) {
        this.handlerMappingV2 = handlerMappingV2;
    }

    public Response doService(Request request, Response response) {
        RequestMappingInfo requestMappingInfo = new RequestMappingInfo(request.getHttpMethod(), request.getUriPath());
        HandlerMethod handler = handlerMappingV2.getHandler(requestMappingInfo);
        log.debug("{},{}", handler.getBean(), handler.getMethod().getName());
        try {
            return handler.invoke(request, response);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }


}
