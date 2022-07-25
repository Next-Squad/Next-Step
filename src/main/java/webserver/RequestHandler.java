package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
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
            // 3.4.3.1 1단계: 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();
            log.debug("request line: {}", line);
            String url = parseLine(line);

            int contentLength = 0;
            Map<String, String> cookies = new HashMap<>();
            //2단계 + 3단계
            if (line == null) {
                return;
            }
            while (!line.equals("")){
                line = br.readLine();
                if (line.contains("Content-Length")){
                    contentLength = getContentLength(line);
                }
                if (line.contains("Cookie")){
                    cookies = HttpRequestUtils.parseCookies(line);
                    log.debug("쿠키: {}", cookies);
                }
                if(line.contains("Accept: text/css")){
                    responseCss(out, url);
                }
                log.debug("header: {}", line);
            }

            if (url.startsWith("/user/create")){

                String body = URLDecoder.decode(IOUtils.readData(br, contentLength), StandardCharsets.UTF_8);
                log.debug("바디: {}", body);
                Map<String, String> params = HttpRequestUtils.parseQueryString(body);
                User user = new User(params.get("userId"), params.get("password"), params.get("name"),
                        params.get("email"));
                DataBase.addUser(user);
                log.debug("user: {}", user);

                DataOutputStream dos = new DataOutputStream(out);
                response302Header(dos, "/index.html");

            } else if (url.equals("/user/login")){
                String body = IOUtils.readData(br, contentLength);
                Map<String, String> params = HttpRequestUtils.parseQueryString(body);
                User user = DataBase.findUserById(params.get("userId"));
                log.debug("로그인유저: {}", user );

                if (user == null) {
                    response(out, "/user/login_failed.html");
                    return;
                }
                if (user.getPassword().equals(params.get("password"))){
                    DataOutputStream dos = new DataOutputStream(out);
                    String cookie = "logined=true";
                    String redirect ="/index.html";
                    responseLogin302Header(dos, cookie, redirect);
                } else {
                    DataOutputStream dos = new DataOutputStream(out);
                    String cookie = "logined=false";
                    String redirect = "/user/login_failed.html";
                    responseLogin302Header(dos, cookie, redirect);
                }

            } else if (url.equals("/user/list")){
                if (cookies.get("logined").equals("true")) {
                    log.debug("쿠키확인: {}" , cookies.get("logined"));
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
                    byte[] body = sb.toString().getBytes(StandardCharsets.UTF_8);
                    DataOutputStream dos = new DataOutputStream(out);
                    response200Header(dos, body.length);
                    responseBody(dos, body);
                } else {
                    response(out, "/user/login.html");
                }

            } else {
                response(out, url);
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
    }private void responseCss200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/css;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseLogin302Header(DataOutputStream dos, String cookie, String url) {
        try {
            dos.writeBytes("HTTP/1.1 302 Redirect \r\n");
            dos.writeBytes("Set-Cookie: " + cookie + "\r\n");
            dos.writeBytes("Location: " + url + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String url) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + url + "\r\n");
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

    private void response(OutputStream out, String url) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
        response200Header(dos, body.length);
        responseBody(dos, body);
    }

    private void responseCss(OutputStream out, String url) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
        responseCss200Header(dos, body.length);
        responseBody(dos, body);
    }

    private int getContentLength(String line) {
        String[] split = line.split(":");
        return Integer.parseInt(split[1].trim());
    }

    private String parseLine(String line){
        String[] parsedLines = line.split(" ");
        return parsedLines[1];
    }
}
