package handler;

import db.URLDataBase;
import db.UserDataBase;
import http.MimeType;
import http.request.HttpMethod;
import http.request.HttpRequest;
import http.request.RequestHeaders;
import http.request.RequestLine;
import http.request.RequestMessageBody;
import http.request.RequestURI;
import http.response.HttpResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = HttpRequest.from(in);
            HttpResponse httpResponse = HttpResponse.ok();
            RequestLine requestLine = httpRequest.getRequestLine();
            log.info("RequestLine= {}", requestLine);
            RequestURI requestUri = requestLine.getRequestUri();
            log.info("RequestURI= {}", requestUri);
            String url = requestUri.getPath();

            if (requestLine.getHttpMethod().equals(HttpMethod.GET)) {
                if (URLDataBase.contains(url) || MimeType.isSupportedExtension(url)) {
                    httpResponse = HttpResponse.ok(url);
                }
                if (url.equals("/user/list")) {
                    RequestHeaders requestHeaders = httpRequest.getRequestHeaders();
                    String cookies = requestHeaders.getCookie();
                    log.debug("Cookie= {}", cookies);
                    if (!requestHeaders.hasCookie()) {
                        httpResponse = HttpResponse.ok("/user/login.html");
                    }

                    if (requestHeaders.hasCookie()) {
                        Map<String, String> parseCookies = HttpRequestUtils.parseCookies(cookies);
                        if (parseCookies.get("logined").equals("false")) {
                            httpResponse = HttpResponse.ok("/user/login.html");
                            httpResponse.flush(out);
                        }
                        httpResponse = HttpResponse.ok("/user/list.html", UserDataBase.findAll());
                    }
                }
                httpResponse.flush(out);
            }

            if (requestLine.getHttpMethod().equals(HttpMethod.POST)) {
                RequestMessageBody requestMessageBody = httpRequest.getRequestMessageBody();
                String messageBody = requestMessageBody.getMessageBody();
                log.debug("HTTP Message Body = {}", messageBody );

                Map<String, String> parsedMessageBody = HttpRequestUtils.parseQueryString(messageBody);
                String userId = parsedMessageBody.get("userId");
                String password = parsedMessageBody.get("password");
                if (url.equals("/user/create")) {
                    User user = new User(
                        userId, password,
                        parsedMessageBody.get("name"),
                        parsedMessageBody.get("email")
                    );
                    UserDataBase.addUser(user);
                    log.debug("Create User ! = {}", user);
                }

                if (url.equals("/user/login")) {
                    User savedUser = UserDataBase.findUserById(userId);
                    log.debug("saved User = {}", savedUser);
                    if (!UserDataBase.login(savedUser, userId, password)) {
                        httpResponse = HttpResponse.found("/user/login_failed.html");
                        httpResponse.flush(out);
                        log.debug("Login Fail! userId = {}", userId);
                        return;
                    }
                    log.debug("Login Complete! userId = {}", userId);
                    httpResponse = HttpResponse.found("/index.html", true);
                    log.info("Response Headers = {}", httpResponse.getResponseHeaders());
                    httpResponse.flush(out);
                }

                httpResponse = HttpResponse.found("/index.html");
            }
            httpResponse.flush(out);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
