package webserver;

import db.DataBase;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;
import login.LoginService;
import login.dto.LoginResult;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.HttpRequest;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private LoginService loginService = LoginService.getInstance();


    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = new HttpRequest(in);

            // create user
            if ("/user/create".equals(httpRequest.getUrl())) {
                User user = createUser(httpRequest.getBody());
                log.debug("user: {}", user);
                DataOutputStream dos = new DataOutputStream(out);
                response302Header(dos, "/index.html");
                DataBase.addUser(user);
                return;
            }

            // login logic
            if ("/user/login".equals(httpRequest.getUrl())) {
                Map<String, String> params = httpRequest.getBody();
                LoginResult result = loginService.login(params.get("userId"),
                    params.get("password"));
                DataOutputStream dataOutputStream = new DataOutputStream(out);
                response302HeaderWithLoginResult(dataOutputStream, result.getUrl(), result.isLogined());
                return;
            }

            // user list logic
            if ("/user/list".equals(httpRequest.getUrl())) {
                if (!httpRequest.getCookie().getIsLogined().isEmpty()) {
                    String loginStatus = httpRequest.getCookie().getIsLogined();
                    if (loginStatus.equals("true")) {
                        DataOutputStream dataOutputStream = new DataOutputStream(out);
                        response302Header(dataOutputStream, "/user/list.html");
                    }
                } else {
                    DataOutputStream dataOutputStream = new DataOutputStream(out);
                    response302Header(dataOutputStream, "login.html");
                }
                return;
            }

            byte[] body = Files.readAllBytes(new File("./webapp" + httpRequest.getUrl()).toPath());
            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, body.length);
            responseBody(dos, body);
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

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String url) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dos.writeBytes("Location: " + url + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302HeaderWithLoginResult(DataOutputStream dos, String url, boolean isLogined) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dos.writeBytes("Location: " + url + "\r\n");
            dos.writeBytes("Set-Cookie: " + "logined=" + isLogined + "Path=/;" + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
