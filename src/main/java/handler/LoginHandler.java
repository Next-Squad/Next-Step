package handler;

import model.User;
import service.LoginService;
import service.UserService;
import util.IOUtils;
import webserver.ContentType;
import webserver.Request.Request;
import webserver.Request.RequestParams;
import webserver.Response.HttpStatus;
import webserver.Response.Response;

@Component
public class LoginHandler implements Handler {

    private final LoginService loginService;
    private final UserService userService;

    public LoginHandler(LoginService loginService, UserService userService) {
        this.loginService = loginService;
        this.userService = userService;
    }

    @Override
    public Response doGet(Request request, Response response) {
        return null;
    }

    @Override
    public Response doPost(Request request, Response response) {
        RequestParams params = request.getParams();
        String userId = params.getParam("userId");
        if (userService.matchUser(userId, params.getParam("password"))) {
            User user = userService.findUser(userId);
            String sessionId = loginService.login(user);
            response.setSession(sessionId);
            response.redirect("/index.html");
            return response;
        }

        response.setHttpStatus(HttpStatus.FORBIDDEN);
        response.setResponseBody(IOUtils.readFile("/user/login_failed.html"), ContentType.HTML);
        return response;
    }
}
