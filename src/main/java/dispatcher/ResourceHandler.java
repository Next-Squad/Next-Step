package dispatcher;

import util.IOUtils;
import webserver.ContentType;
import webserver.Request.Request;
import webserver.Response.Response;

public class ResourceHandler {

    public Response doService(Request request, Response response) {
        String uriPath = request.getUriPath();
        byte[] bytes = IOUtils.readFile(uriPath);
        response.setResponseBody(bytes, ContentType.getContentType(uriPath));
        return response;
    }


}
