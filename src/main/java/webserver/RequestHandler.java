package webserver;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = new HttpRequest(in);
            HttpResponse httpResponse = new HttpResponse(out);
            String path = httpRequest.getPath();


            if (("/user/create").equals(path)){
                User user = new User(httpRequest.getParameter("userId"), httpRequest.getParameter("password"),
                        httpRequest.getParameter("name"), httpRequest.getParameter("email"));

                DataBase.addUser(user);
                log.debug("user: {}", user);

                httpResponse.sendRedirect("/index.html");

            } else if (("/user/login").equals(path)){
                User user = DataBase.findUserById(httpRequest.getParameter("userId"));
                log.debug("로그인유저: {}", user );

                if (user == null) {
                    httpResponse.forward("/user/login_failed.html");
                    return;
                }
                if (user.getPassword().equals(httpRequest.getParameter("password"))){
                    httpResponse.addHeader("Set-Cookie", "logined=true");
                    httpResponse.sendRedirect("/index.html");
                } else {
                    httpResponse.sendRedirect("/user/login_failed.html");
                }

            } else if (("/user/list").equals(path)){
                if (isLogined(httpRequest.getHeader("Cookie"))) {
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

                    httpResponse.forwardBody(sb.toString().getBytes(StandardCharsets.UTF_8));
                } else {
                    httpResponse.sendRedirect("/user/login.html");
                }

            } else {
                httpResponse.forward(path);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    //TODO: HttpRequest에 있어야 하는게 맞겠지..? 생각해보기.
    private boolean isLogined(String cookie) {
        Map<String, String> cookies = HttpRequestUtils.parseCookies(cookie);
        if(cookies.get("logined") != null && cookies.get("logined").equals("true")) {
            return true;
        }
        return false;
    }
}
