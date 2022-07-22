package dispatcher;

import webserver.Request.Request;
import webserver.Response.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ResourceHandler {

    private static final String RESOURCE_PATH = "./webapp";

    public Response doService(Request request, Response response) {
        String uriPath = request.getUriPath();
        try {
            byte[] bytes = Files.readAllBytes(new File(RESOURCE_PATH + uriPath).toPath());

            response.setResponseBody(bytes);
        } catch (IOException e) {
            // TODO 예외처리
            e.printStackTrace();
        }
        return response;
    }


}
