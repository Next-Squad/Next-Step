package webserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import dispatcher.*;
import handler.Handler;
import handler.UserHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import webserver.Request.HttpMethod;

public class WebServer {
    private static final Logger log = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;

    public static void main(String args[]) throws Exception {
        int port = 0;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }
        Map<MappingRegistry, Handler> handlerMap = Map.of(
                new MappingRegistry(HttpMethod.GET, "/user/create"), new UserHandler(new UserService())
        );

        HandlerMapping handlerMapping = new HandlerMapping(handlerMap);

        FrontController frontController = new FrontController(handlerMapping);
        Dispatcher dispatcher = new Dispatcher(frontController, new ResourceHandler());

        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.

        try (ServerSocket listenSocket = new ServerSocket(port)) {
            log.info("Web Application Server started {} port.", port);

            // 클라이언트가 연결될때까지 대기한다.
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                RequestHandler requestHandler = new RequestHandler(connection, dispatcher);
                requestHandler.start();
            }
        }
    }
}
