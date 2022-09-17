package controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

public class ListUserController extends AbstractController{
    private final Logger log = LoggerFactory.getLogger(ListUserController.class);
    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        if (isLogined(request.getHeader("Cookie"))) {
            Collection<User> users = DataBase.findAll();
            StringBuilder sb = new StringBuilder();
            sb.append("<table>");
            sb.append("<th>#</th> <th>사용자 아이디</th> <th>이름</th> <th>이메일</th><th></th>");
            sb.append("<tbody>");
            for (User u : users) {
                sb.append("<tr>");
                sb.append("<td>#</td>");
                sb.append("<td>" + u.getUserId() + "</td>");
                sb.append("<td>" + u.getName() + "</td>");
                sb.append("<td>" + u.getEmail() + "</td>");
                sb.append("</tr>");
            }
            sb.append("</tbody>");
            sb.append("</table>");

            response.forwardBody(sb.toString().getBytes(StandardCharsets.UTF_8));
        } else {
            response.sendRedirect("/user/login.html");
        }
    }

    public boolean isLogined(String s) {
        Map<String, String> cookies = HttpRequestUtils.parseCookies(s);
        if(cookies.get("logined") != null && cookies.get("logined").equals("true")) {
            return true;
        }
        return false;
    }
}
