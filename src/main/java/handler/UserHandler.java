package handler;

import db.DataBase;
import model.User;
import webserver.http.HttpHeader;
import webserver.http.HttpMethod;
import webserver.http.HttpRequestBody;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.was.RequestMapping;

public class UserHandler {

    @RequestMapping(method = HttpMethod.POST, url = "/user/create")
    public final Handler CREATE_USER = (request) -> {
        HttpRequestBody requestBody = request.getRequestBody();
        User user = new User(
                requestBody.get("userId"),
                requestBody.get("password"),
                requestBody.get("name"),
                requestBody.get("email"));
        DataBase.addUser(user);

        HttpHeader header = new HttpHeader();
        header.add("Location", "/index.html");

        return new HttpResponse(HttpStatus.FOUND, header);
    };

    @RequestMapping(method = HttpMethod.POST, url = "/user/login")
    public final Handler LOGIN = request -> {
        HttpRequestBody requestBody = request.getRequestBody();
        String userId = requestBody.get("userId");
        String password = requestBody.get("password");

        User user = DataBase.findUserById(userId);

        String loginFailedViewName = "/user/login_failed.html";
        String loginSuccessViewName = "/index.html";

        if (user == null) {
            return createLoginResponse(HttpStatus.NOT_FOUND, loginFailedViewName, false);
        }

        if (!user.isCorrectPassword(password)) {
            return createLoginResponse(HttpStatus.UNAUTHORIZED, loginFailedViewName, false);
        }

        return createLoginResponse(HttpStatus.OK, loginSuccessViewName, true);
    };

    private HttpResponse createLoginResponse(HttpStatus status, String viewName, boolean isLoggedIn) {
        return HttpResponse.builder()
                .setStatus(status)
                .setViewName(viewName)
                .addHeader("Set-Cookie", "logined=" + isLoggedIn)
                .build();
    }
}
