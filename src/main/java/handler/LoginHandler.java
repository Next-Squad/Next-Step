package handler;

import service.UserService;
import webserver.Request.Request;
import webserver.Response.Response;

public class LoginHandler implements Handler{

    private final UserService loginService;

    public LoginHandler(UserService loginService) {
        this.loginService = loginService;
    }

    @Override
    public Response doGet(Request request, Response response) {
        return null;
    }

    @Override
    public Response doPost(Request request, Response response) {
        return null;
    }
}
