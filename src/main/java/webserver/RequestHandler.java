package webserver;

import db.DataBase;
import http.request.HttpRequest;
import http.request.RequestReader;
import http.response.ResponseWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;
import login.LoginService;
import login.dto.LoginResult;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final LoginService loginService = LoginService.getInstance();


    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            RequestReader requestReader = new RequestReader(in);
            HttpRequest httpRequest = requestReader.create();
            new ResponseWriter(httpRequest);

            // create user
            if ("/user/create".equals(httpRequest.getUrl())) {
                User user = createUser(httpRequest.getBody());
                log.debug("user: {}", user);
                httpResponse.response302Header("/index.html");
                DataBase.addUser(user);
                return;
            }

            // login logic
            if ("/user/login".equals(httpRequest.getUrl())) {
                Map<String, String> params = httpRequest.getBody();
                LoginResult result = loginService.login(params.get("userId"),
                    params.get("password"));
                httpResponse.response302HeaderWithLoginResult(result.getUrl(), result.isLogined());
                return;
            }

            // user list logic
            if ("/user/list".equals(httpRequest.getUrl())) {
                if (!httpRequest.getCookie().getIsLogined().isEmpty()) {
                    String loginStatus = httpRequest.getCookie().getIsLogined();
                    if (loginStatus.equals("true")) {
                        httpResponse.response302Header("/user/list.html");
                    }
                } else {
                    httpResponse.response302Header("login.html");
                }
                return;
            }

            byte[] body = httpResponse.getBody(httpRequest.getUrl());
            httpResponse.response200Header(body.length);
            httpResponse.responseBody(body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }



    private User createUser(Map<String, String> params) {
        return new User(
            params.get("userId"),
            params.get("password"),
            params.get("name"),
            params.get("email"));
    }
}
