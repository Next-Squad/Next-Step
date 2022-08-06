package http.response;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {
    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);

    private DataOutputStream dataOutputStream;
    private HttpStatus httpStatus;

    public HttpResponse(OutputStream out) {
        this.dataOutputStream = new DataOutputStream(out);
    }

    public void sendResponse() {
//        responseBody();
    }

    public void response200Header(int lengthOfBodyContent) {
        try {
            dataOutputStream.writeBytes("HTTP/1.1 200 OK \r\n");
            dataOutputStream.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dataOutputStream.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dataOutputStream.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void response302Header(String url) {
        try {
            dataOutputStream.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dataOutputStream.writeBytes("Location: " + url + "\r\n");
            dataOutputStream.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void response302HeaderWithLoginResult(String url, boolean isLogined) {
        try {
            dataOutputStream.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dataOutputStream.writeBytes("Location: " + url + "\r\n");
            dataOutputStream.writeBytes("Set-Cookie: " + "logined=" + isLogined + "; Path=/;" + "\r\n");
            dataOutputStream.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void responseBody(byte[] body) {
        try {
            dataOutputStream.write(body, 0, body.length);
            dataOutputStream.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public byte[] getBody(String url) throws IOException {
        return Files.readAllBytes(new File("./webapp" + url).toPath());
    }

}
