package webserver;

import db.DataBase;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.Map;
import login.LoginService;
import login.dto.LoginResult;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private static final String CREATE_USER_PATH = "/user/create";
    private LoginService loginService = LoginService.getInstance();


    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            // Request Line
            String line = br.readLine();
            log.debug("request line: {}", line);
            String[] tokens = line.split(" ");

            int contentLength = 0;
            while (!line.equals("")) {
                log.debug("header: {}", line);
                line = br.readLine();
                if (line.contains("Content-Length")) {
                    String[] headerTokens = line.split(":");
                    contentLength = Integer.parseInt(headerTokens[1].trim());
                }
            }

            String url = tokens[1];
            if (CREATE_USER_PATH.equals(url)) {
                Map<String, String> params = getParams(br, contentLength);

                User user = new User(
                    params.get("userId"),
                    params.get("password"),
                    params.get("name"),
                    params.get("email"));
                log.debug("user: {}", user);
                DataOutputStream dos = new DataOutputStream(out);
                response302Header(dos, "/index.html");
                DataBase.addUser(user);
            }

            // login logic
            if ("/user/login".equals(url)) {
                Map<String, String> params = getParams(br, contentLength);
                LoginResult result = loginService.login(params.get("userId"),
                    params.get("password"));
                DataOutputStream dataOutputStream = new DataOutputStream(out);
                response302HeaderWithLoginResult(dataOutputStream, result.getUrl(), result.isLogined());
            }

            log.debug("line: {}", line);
            byte[] body = Files.readAllBytes(new File("./webapp" + tokens[1]).toPath());
            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private Map<String, String> getParams(BufferedReader br, int contentLength) throws IOException {
        String body = IOUtils.readData(br, contentLength);
        String decode = URLDecoder.decode(body);
        Map<String, String> params = HttpRequestUtils.parseQueryString(decode);
        return params;
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
            dos.writeBytes("Cookie: " + "logined=" + isLogined + "\r\n");
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
