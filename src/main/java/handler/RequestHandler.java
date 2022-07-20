package handler;

import http.request.RequestLine;
import http.request.RequestURI;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

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

            InputStreamReader inputStreamReader = new InputStreamReader(in, StandardCharsets.UTF_8); // 왜 안먹힐까?
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();

            String line = URLDecoder.decode(bufferedReader.readLine(), StandardCharsets.UTF_8); // 왜 한 번 더 해줘야 먹힐까? - 위에서 안먹히는 걸까?
            log.info("RequestLine = {}", line);
            if (line == null) {
                return;
            }
            RequestLine requestLine = HttpRequestUtils.parseRequestLine(line);
            RequestURI requestUri = requestLine.getRequestUri();
            String url = requestUri.getPath();

            byte[] body = null;
            DataOutputStream dos = new DataOutputStream(out);
            if (url.equals("/index.html")) {
                body = Files.readAllBytes(new File("./webapp" + url).toPath());
            }

            if (url.equals("/user/form.html")) {
                body = Files.readAllBytes(new File("./webapp" + url).toPath());
            }

            if (url.equals("/user/create")) {
                String queryString = requestUri.getQueryString();
                log.debug("Decoded querystring = {}", queryString);
                Map<String, String> parsedQueryString = HttpRequestUtils.parseQueryString(queryString);
                User user = new User(
                        parsedQueryString.get("userId"),
                        parsedQueryString.get("password"),
                        parsedQueryString.get("name"),
                        parsedQueryString.get("email")
                    );
                log.debug("Create User ! = {}", user);
            }

            if (body == null){
                body = "Hello World".getBytes();
            }

            response200Header(dos, body.length);
            responseBody(dos, body);

            while (!line.equals((""))) {
                line = URLDecoder.decode(bufferedReader.readLine(), StandardCharsets.UTF_8);
                sb.append(line);
                sb.append("\r\n");
                log.debug("Header = {}", line);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
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
