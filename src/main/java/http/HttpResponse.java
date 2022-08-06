package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpResponse {
    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
    private DataOutputStream dos = null;
    private Map<String, String> header = new HashMap<String, String>();

    public HttpResponse(OutputStream out) {
        dos = new DataOutputStream(out);
    }

    public void forward(String url) {
        try {
            byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
            if (url.endsWith(".css")) {
                header.put("Content-Type", "text/css");
            } else {
                header.put("Content-Type", "text/html;charset=uft-8");
            }
            header.put("Content-length", body.length + "");
            response200Header(body.length);
            responseBody(body);
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
    }

    public void forwardBody(byte[] body) {
        header.put("Content-Type", "text/html;charset=uft-8");
        header.put("Content-length", body.length + "");
        response200Header(body.length);
        responseBody(body);
    }

    public void response200Header(int length) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            writeHeaders();
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void writeHeaders() {
        try {
            Set<String> keys = header.keySet();
            for (String key : keys) {
                dos.writeBytes(key + ": " + header.get(key) + "\r\n");
            }
        } catch (IOException e){
            log.error(e.getMessage());
        }
    }

    public void sendRedirect(String url) {
        try {
            dos.writeBytes("HTTP/1.1 302 Redirect \r\n");
            writeHeaders();
            dos.writeBytes("Location: " + url + "\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


    public void addHeader(String key, String value) {
        header.put(key, value);
    }

}
