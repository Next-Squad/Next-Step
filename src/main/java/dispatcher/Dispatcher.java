package dispatcher;

import webserver.Request.Request;
import webserver.Response.Response;

public class Dispatcher {

    private final FrontController frontController;
    private final ResourceHandler resourceHandler;

    public Dispatcher(FrontController frontController, ResourceHandler resourceHandler) {
        this.frontController = frontController;
        this.resourceHandler = resourceHandler;
    }

    public Response dispatch(Request request, Response response) {
        if (request.isResourceRequest()) {
            return resourceHandler.doService(request, response);
        }
        return frontController.doService(request, response);
    }




}
