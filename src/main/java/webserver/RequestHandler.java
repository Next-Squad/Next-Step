package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.FileUtils;
import util.HttpRequestUtils;
import webserver.http.ContentType;
import webserver.http.HttpHeader;
import webserver.http.HttpRequest;
import webserver.http.HttpRequestMessage;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.was.Dispatcher;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final String webAppPath;
    private final Dispatcher dispatcher;

    public RequestHandler(Socket connectionSocket, String webAppPath, Dispatcher dispatcher) {
        this.connection = connectionSocket;
        this.webAppPath = webAppPath;
        this.dispatcher = dispatcher;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequestMessage requestMessage = HttpRequestMessage.parse(in);
            String path = requestMessage.uri().getPath();

            // 정적 요청 처리
            if (isStaticResourceRequest(path)) {
                String extension = HttpRequestUtils.parseExtension(path);
                byte[] file = FileUtils.readFile(webAppPath + path);

                HttpHeader header = new HttpHeader();
                header.add("Content-Type", ContentType.findByExtension(extension).getHeader());
                header.add("Content-Length", String.valueOf(file.length));

                flush(out, new HttpResponse(HttpStatus.OK, header, file));
                return;
            }

            // 동적 요청 처리
            if (dispatcher.isMapped(requestMessage.method(), requestMessage.uri())) {
                HttpRequest request = HttpRequest.from(requestMessage);

                HttpResponse httpResponse = this.dispatcher.handlerRequest(request);
                flush(out, httpResponse);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private boolean isStaticResourceRequest(String path) {
        return ContentType.isExistsByExtension(HttpRequestUtils.parseExtension(path));
    }

    private void flush(OutputStream out, HttpResponse response) {
        try (DataOutputStream dos = new DataOutputStream(out)) {
            HttpStatus status = response.getStatus();
            HttpHeader header = response.getHeader();
            byte[] body = response.getBody();

            dos.writeBytes(String.format("HTTP/1.1 %d %s \r\n", status.getCode(), status.getStatus()));
            for (String key : header.keySet()) {
                dos.writeBytes(String.format("%s: %s\r\n", key, header.get(key)));
            }
            dos.writeBytes("\r\n");
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
