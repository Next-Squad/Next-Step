package dispatcher;

import webserver.Request.Request;
import webserver.Response.Response;

public class Dispatcher {

    private final FrontControllerV2 frontControllerV2;
    private final ResourceHandler resourceHandler;

    public Dispatcher(FrontControllerV2 frontControllerV2, ResourceHandler resourceHandler) {
        this.frontControllerV2 = frontControllerV2;
        this.resourceHandler = resourceHandler;
    }

    public Response dispatch(Request request, Response response) {
        if (request.isResourceRequest()) {
            return resourceHandler.doService(request, response);
        }
        return frontControllerV2.doService(request, response);
    }




}
