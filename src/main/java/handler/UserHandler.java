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

        HttpHeader header = new HttpHeader();
        String loginFailedViewName = "/user/login_failed.html";

        if (user == null) {
            header.add("Set-Cookie", "logined=false");
            HttpResponse httpResponse = new HttpResponse(HttpStatus.NOT_FOUND, header);
            httpResponse.setViewName(loginFailedViewName);
            return httpResponse;
        }

        if (!user.isCorrectPassword(password)) {
            header.add("Set-Cookie", "logined=false");
            HttpResponse httpResponse = new HttpResponse(HttpStatus.UNAUTHORIZED, header);
            httpResponse.setViewName(loginFailedViewName);
            return httpResponse;
        }

        header.add("Set-Cookie", "logined=true");
        HttpResponse httpResponse = new HttpResponse(HttpStatus.OK, header);
        httpResponse.setViewName("/index.html");
        return httpResponse;
    };
}
