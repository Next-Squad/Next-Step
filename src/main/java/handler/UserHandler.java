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
    protected final Handler CREATE_USER = (request) -> {
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
}
