package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.FileUtils;
import util.HttpRequestUtils;

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
            String path = requestMessage.uri().getPath();

            // 정적 요청 처리
            if (isStaticResourceRequest(path)) {
                String extension = HttpRequestUtils.parseExtension(path);
                byte[] file = FileUtils.readFile(webAppPath + path);

                HttpHeader header = new HttpHeader();
                header.add("Content-Type", ContentType.findByExtension(extension).getHeader());
                header.add("Content-Length", String.valueOf(file.length));

                flush(out, new HttpResponse(HttpStatus.OK, header, file));
                return;
            }

            // 동적 요청 처리
            if (isUserCreateRequest(requestMessage, HttpMethod.POST, USER_CREATE_URI_PATH)) {
                HttpRequestBody body = requestMessage.body();
                registerNewUser(body);

                HttpHeader header = new HttpHeader();
                header.add("Location", "/index.html");

                flush(out, new HttpResponse(HttpStatus.FOUND, header));
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private boolean isStaticResourceRequest(String path) {
        return ContentType.isExistsByExtension(HttpRequestUtils.parseExtension(path));
    }

    private boolean isUserCreateRequest(HttpRequestMessage requestMessage, HttpMethod method, String path) {
        URI uri = requestMessage.uri();
        return requestMessage.method().equals(method) && uri.getPath().equals(path);
    }

    private void registerNewUser(HttpRequestBody requestBody) {
        User user = new User(
                requestBody.get("userId"),
                requestBody.get("password"),
                requestBody.get("name"),
                requestBody.get("email"));
        DataBase.addUser(user);
    }

    private void flush(OutputStream out, HttpResponse response) {
        try (DataOutputStream dos = new DataOutputStream(out)) {
            HttpStatus status = response.getStatus();
            HttpHeader header = response.getHeader();
            byte[] body = response.getBody();

            dos.writeBytes(String.format("HTTP/1.1 %d %s \r\n", status.getCode(), status.getStatus()));
            for (String key : header.keySet()) {
                dos.writeBytes(String.format("%s: %s\r\n", key, header.get(key)));
            }
            dos.writeBytes("\r\n");
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
