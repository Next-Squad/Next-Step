package webserver;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.Request;
import webserver.response.Response;
import webserver.response.ResponseMaker;

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
            Request request = readRequest();
            Response response = makeResponseOf(request);

            sendResponse(response);
            br.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public Request readRequest() throws IOException {
        String firstLine = br.readLine();
        Request request = new Request(firstLine);
        request.setParams(parseParams());
        log.debug("method: {}, uri: {}", request.getMethod(), request.getUri());
        return request;
    }

    private Map<String, String> parseParams() throws IOException {
        Map<String, String> params = new HashMap<>();
        String line = br.readLine();
        while (line.length() != 0 && !(line.equals("\r\n"))) {
            log.debug(line);
            line = br.readLine();
        }
        if (line.length() != 0) {
            String[] paramsString = br.readLine().split("&");
            for (String param : paramsString) {
                params.put(param.split("=")[0], param.split("=")[1]);
            }
        }
        return params;
    }

    public Response makeResponseOf(Request request) {
        return responseMaker.getResponse(request);
    }

    public void sendResponse(Response response) {
        try {
            dos.write(response.getHeader());
            dos.write(response.getBody());
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }
}
