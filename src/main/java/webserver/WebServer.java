package webserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import handler.UserHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.was.Dispatcher;
import webserver.was.HandlerMapping;
import webserver.was.RequestMappingProcessor;
import webserver.was.ViewResolver;

public class WebServer {
    private static final Logger log = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;
    private static final String WEB_APP_PATH = "./webapp";

    public static void main(String[] args) throws Exception {
        int port;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }

        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            log.info("Web Application Server started {} port.", port);

            // RequestMapping 정보를 초기화 한다.
            RequestMappingProcessor mappingProcessor = new RequestMappingProcessor(List.of(
                    UserHandler.class
            ));
            log.info("Initialize handler mapping");

            // Dispatcher 를 초기화 한다.
            HandlerMapping handlerMapping = new HandlerMapping(mappingProcessor.getHandlerMappingInfo());
            ViewResolver viewResolver = new ViewResolver(WEB_APP_PATH);
            Dispatcher dispatcher = new Dispatcher(handlerMapping, viewResolver);
            log.info("Initialize Dispatcher");

            // 클라이언트가 연결될때까지 대기한다.
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                RequestHandler requestHandler = new RequestHandler(connection, WEB_APP_PATH, dispatcher);
                requestHandler.start();
            }
        }
    }
}
