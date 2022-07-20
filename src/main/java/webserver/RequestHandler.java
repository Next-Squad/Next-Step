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
import java.nio.file.Files;
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

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            InputStreamReader inReader = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(inReader);

            String line = br.readLine(); // request line 따로 입력 받음
            String[] tokens = line.split(" "); // request line split
            while (!line.equals("")) {
                line = br.readLine();

                if (line.contains("?")) {
                    log.debug("tokens[1]: {}", tokens[1]);
                    String[] split = tokens[1].split("\\?");
                    log.debug("split[1]: {}", split[1]);
                    Map<String, String> parseQueryString = HttpRequestUtils.parseQueryString(
                        split[1]);

                    User user = getUser(parseQueryString);

                    DataBase.addUser(user);
                }

                log.debug("line: {}", line);
            }
            byte[] body = Files.readAllBytes(new File("./webapp" + tokens[1]).toPath());

            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private User getUser(Map<String, String> parseQueryString) {
        return new User(
            parseQueryString.get("userId"),
            parseQueryString.get("password"),
            parseQueryString.get("name"),
            parseQueryString.get("email"));
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
