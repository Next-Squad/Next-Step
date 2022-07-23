package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.crypto.Data;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final ResponseMaker responseMaker = new ResponseMaker();

    private Socket connection;
    private DataOutputStream dos;
    private BufferedReader br;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());


        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            dos = new DataOutputStream(out);
            br = new BufferedReader(new InputStreamReader(in));

            Request request = readRequest(br);
            sendResponse(makeResponseOf(request));
            br.close();

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public Request readRequest(BufferedReader br) throws IOException {
        Request request = new Request(br.readLine());
        log.debug("method: {}, uri: {}", request.getMethod(), request.getUri());
        return request;
    }

    public Response makeResponseOf(Request request) {
        try {
            byte[] body = Files.readAllBytes(new File("./webapp" + request.getUri()).toPath());
            return new Response(StatusCode.OK, body);
        } catch (IOException e) {
            return new Response(StatusCode.OK, "헬로우 월드!".getBytes(StandardCharsets.UTF_8));
        }
    }

    public void sendResponse(Response response) {
        try {
            dos.write(response.getHeader());
            dos.write(response.getBody());
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }

//    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
//        try {
//            dos.writeBytes("HTTP/1.1 200 OK \r\n");
//            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
//            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
//            dos.writeBytes("\r\n");
//        } catch (IOException e) {
//            log.error(e.getMessage());
//        }
//    }
//
//
//    private void responseBody(DataOutputStream dos, byte[] body) {
//        try {
//            dos.write(body, 0, body.length);
//            dos.flush();
//        } catch (IOException e) {
//            log.error(e.getMessage());
//        }
//    }
}
