package handler;

import http.request.HttpMethod;
import http.request.RequestHeaders;
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
import util.HttpRequestUtils.Pair;
import util.IOUtils;

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
            if (requestLine.getHttpMethod().equals(HttpMethod.GET)) {
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
            }


            // HTTP Headers
            RequestHeaders requestHeaders = new RequestHeaders();
            while (!line.equals((""))) {
                line = URLDecoder.decode(bufferedReader.readLine(), StandardCharsets.UTF_8);
                log.debug("Header = {}", line);

                Pair pair = HttpRequestUtils.parseHeader(line);
                requestHeaders.addHeader(pair);
            }

            // HTTP Message Body (Content-Length 만큼 받아야 함)
            if (requestLine.getHttpMethod().equals(HttpMethod.POST)) {
                int contentLength = Integer.parseInt(requestHeaders.getHeader("Content-Length"));
                String messageBody = URLDecoder.decode(
                    IOUtils.readData(bufferedReader, contentLength),
                    StandardCharsets.UTF_8
                );
                log.debug("HTTP Message Body = {}", messageBody);

                Map<String, String> parsedMessageBody = HttpRequestUtils.parseQueryString(messageBody);
                if (url.equals("/user/create")) {
                    User user = new User(
                        parsedMessageBody.get("userId"),
                        parsedMessageBody.get("password"),
                        parsedMessageBody.get("name"),
                        parsedMessageBody.get("email")
                    );
                    log.debug("Create User ! = {}", user);
                }
                response302Header(dos, "/index.html");
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

    private void response302Header(DataOutputStream dos, String redirectURI) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Location: " + redirectURI + "\r\n");
            dos.writeBytes("\r\n");
            dos.flush();
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
