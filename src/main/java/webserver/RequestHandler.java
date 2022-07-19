package webserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import java.util.Arrays;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String s;
            StringBuilder sb = new StringBuilder(); // StringBuiffer 를 쓰지않는 이유가 뭘까?

            String requestLine = bufferedReader.readLine();
            String[] parsedRequestLine = requestLine.split(" ");
            System.out.println(Arrays.toString(parsedRequestLine));
            String httpMethod = parsedRequestLine[0];
            String urlPath = parsedRequestLine[1];
            String httpProtocol = parsedRequestLine[2];
            String[] parsedUrlPath = urlPath.split("\\?");
            String parsedPath = parsedUrlPath[0];
            String queryParams = parsedUrlPath[1];
            byte[] body;
            DataOutputStream dos = new DataOutputStream(out);
            if (parsedPath.equals("/index.html")) {
                FileInputStream fileInputStream = new FileInputStream("./webapp/index.html");
                body = fileInputStream.readAllBytes();
            } else {
                body = "Hello World".getBytes();
            }
            response200Header(dos, body.length);
            responseBody(dos, body);


//            while ((s = bufferedReader.readLine()) != null) {
//                sb.append(s);
//                sb.append("\r\n");
//            }

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
