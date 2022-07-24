package handler;

import util.IOUtils;
import webserver.ContentType;
import webserver.Request.Request;
import webserver.Response.Response;

@Component
public class HomeHandler implements Handler{

    @Override
    public Response doGet(Request request, Response response) {
        byte[] bytes = IOUtils.readFile("/index.html");
        response.setResponseBody(bytes, ContentType.HTML);
        return response;
    }

    @Override
    public Response doPost(Request request, Response response) {
        return null;
    }
}
