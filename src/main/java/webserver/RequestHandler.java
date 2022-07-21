package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.FileUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;

public class RequestHandler extends Thread {

    private static final String USER_CREATE_URI_PATH = "/user/create";
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final String webAppPath;

    public RequestHandler(Socket connectionSocket, String webAppPath) {
        this.connection = connectionSocket;
        this.webAppPath = webAppPath;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequestMessage requestMessage = HttpRequestMessage.parse(in);
            URI uri = requestMessage.uri();

            // 정적 요청 처리
            if (isStaticResourceRequest(uri)) {
                responseStaticResources(out, uri);
                return;
            }

            // 동적 요청 처리
            if (isUserCreateRequest(requestMessage, HttpMethod.POST, USER_CREATE_URI_PATH)) {
                HttpRequestBody body = requestMessage.body();
                registerNewUser(body);

                responseDynamic(out);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private boolean isUserCreateRequest(HttpRequestMessage requestMessage, HttpMethod method, String path) {
        URI uri = requestMessage.uri();
        return requestMessage.method().equals(method) && uri.getPath().equals(path);
    }

    private void responseStaticResources(OutputStream out, URI uri) {
        DataOutputStream dos = new DataOutputStream(out);
        byte[] body = FileUtils.readFile(this.webAppPath + uri.getPath());
        response200Header(dos, body.length);
        responseBody(dos, body);
    }

    private static void registerNewUser(HttpRequestBody requestBody) {
        User user = new User(
                requestBody.get("userId"),
                requestBody.get("password"),
                requestBody.get("name"),
                requestBody.get("email"));
        DataBase.addUser(user);
    }

    private void responseDynamic(OutputStream out) {
        DataOutputStream dos = new DataOutputStream(out);
        byte[] body = new byte[]{};
        response200Header(dos, body.length);
        responseBody(dos, body);
    }

    private boolean isStaticResourceRequest(URI uri) {
        return !uri.getPath().equals(USER_CREATE_URI_PATH);
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

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
