package handler;

import controller.Controller;
import controller.HomeController;
import controller.LoginController;
import controller.SignUpController;
import controller.UserController;
import http.request.HttpRequest;
import http.response.HttpResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final Map<String, Controller> handlerMap = new HashMap<>();

    static {
        handlerMap.put("/index.html", new HomeController());
        handlerMap.put("/user/list", new UserController());
        handlerMap.put("/user/list.html", new UserController());
        handlerMap.put("/user/create", new SignUpController());
        handlerMap.put("/user/login", new LoginController());
    }

    private final Socket connection;
    private Controller controller = new HomeController();;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = HttpRequest.from(in);
            HttpResponse httpResponse = new HttpResponse(out);
            String url = httpRequest.getRequestURIPath();

            if (handlerMap.containsKey(url)) {
                controller = handlerMap.get(url);
                log.debug("Controller = {}", controller.getClass());
                controller.service(httpRequest, httpResponse);
                return;
            }
            controller.service(httpRequest, httpResponse);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
