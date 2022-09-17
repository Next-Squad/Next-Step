package controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginController extends AbstractController{
    private final Logger log = LoggerFactory.getLogger(LoginController.class);
    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        User user = DataBase.findUserById(request.getParameter("userId"));
        log.debug("로그인유저: {}", user );

        if (user == null) {
            response.forward("/user/login_failed.html");
            return;
        }
        if (user.getPassword().equals(request.getParameter("password"))){
            response.addHeader("Set-Cookie", "logined=true");
            response.sendRedirect("/index.html");
        } else {
            response.sendRedirect("/user/login_failed.html");
        }
    }
}
