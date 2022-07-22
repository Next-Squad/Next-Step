package handler;

import service.LoginService;
import webserver.Request.Request;
import webserver.Response.Response;

public class LoginHandler implements Handler{

    private final LoginService loginService;

    public LoginHandler(LoginService loginService) {
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
