package dispatcher;

import handler.Handler;
import webserver.Request.HttpMethod;
import webserver.Request.Request;
import webserver.Response.Response;


public class FrontController {

    private final HandlerMapping handlerMapping;

    public FrontController(HandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    public Response doService(Request request, Response response) {
        Handler handler = getHandler(request);
        if (request.getHttpMethod().equals(HttpMethod.GET)) {
            return handler.doGet(request, response);
        }
        return handler.doPost(request, response);
    }

    private Handler getHandler(Request request) {

        return handlerMapping.matchHandler(request);
    }
}
