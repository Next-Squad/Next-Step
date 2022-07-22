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
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private static final String CREATE_USER_PATH = "/user/create";
    private static final String QUERY_SIGN = "?";

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            InputStreamReader inReader = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(inReader);

            String line = br.readLine(); // request line 따로 입력 받음
            log.debug("request line: {}", line);
            String[] tokens = line.split(" "); // request line split
            int contentLength = 0;

            while (!line.equals("")) {
                log.debug("header: {}", line);
                line = br.readLine();
                if (line.contains("Content-Length")) {
                    String[] headerTokens = line.split(":");
                    contentLength = Integer.parseInt(headerTokens[1].trim());
                }
            }
            log.debug("int 형식의 contentLength: {}", contentLength);

            String url = tokens[1];
            if (CREATE_USER_PATH.equals(url)) {
                String body = IOUtils.readData(br, contentLength);
                String decode = URLDecoder.decode(body);
                log.debug("String으로 변환된 contentLength: {}", decode);
                Map<String, String> params = HttpRequestUtils.parseQueryString(decode);

                User user = new User(
                    params.get("userId"),
                    params.get("password"),
                    params.get("name"),
                    params.get("email"));
                log.debug("user: {}", user);
                DataBase.addUser(user);
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
