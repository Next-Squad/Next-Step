package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import dispatcher.Dispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import webserver.Request.Request;
import webserver.Response.Response;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final Dispatcher dispatcher;

    public RequestHandler(Socket connectionSocket, Dispatcher dispatcher) {
        this.connection = connectionSocket;
        this.dispatcher = dispatcher;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.

            Request request = readRequest(in);
            log.debug("{}", request.getUriPath());
            Response response = dispatcher.dispatch(request, Response.of(request.getRequestLine().getProtocol()));
            log.debug("{}", response.responseHeaderToString());
            response.writeResponse(new DataOutputStream(out));

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private Request readRequest(InputStream inputStream) throws IOException {
        return HttpRequestUtils.toRequest(inputStream);
    }

}
