package handler;

import service.UserService;
import webserver.Request.Request;
import webserver.Request.RequestParams;
import webserver.Response.Response;

@Component
public class UserHandler implements Handler {

    private final UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Response doGet(Request request, Response response) {
        RequestParams params = request.getParams();
        userService.saveUser(params.getParam("userId"),
                params.getParam("password"),
                params.getParam("name"),
                params.getParam("email"));

        response.redirect("/index.html");
        return response;
    }

    @Override
    public Response doPost(Request request, Response response) {
        RequestParams params = request.getParams();
        userService.saveUser(params.getParam("userId"),
                params.getParam("password"),
                params.getParam("name"),
                params.getParam("email"));

        response.redirect("/");
        return response;
    }
}
