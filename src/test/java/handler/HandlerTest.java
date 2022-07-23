package handler;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.WebServer;

import java.net.ServerSocket;
import java.util.Random;

public class HandlerTest {

    private static final Logger log = LoggerFactory.getLogger(HandlerTest.class);
    protected String baseUrl;

    private Thread webServerThread = null;

    @BeforeEach
    void setUp() {
        int port = randomPortNumber();

        baseUrl =  "http://localhost:" + port;
        startWebServer(port);
    }

    private int randomPortNumber() {
        Random random = new Random();

        final int minPort = 49152;
        final int maxPort = 65535;

        int randomPort;
        do {
            randomPort = random.nextInt(minPort, maxPort + 1);
        } while(!isFreePort(randomPort));

        return randomPort;
    }

    private boolean isFreePort(int port) {
        try (ServerSocket server = new ServerSocket(port)) {
            return server.isBound() && !server.isClosed();
        } catch (Exception e) {
            return false;
        }
    }

    private void startWebServer(int port) {
        webServerThread = new Thread(() -> {
            try {
                WebServer.main(new String[]{ String.valueOf(port) });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        webServerThread.start();
    }

    private void interruptWebServer() {
        webServerThread.interrupt();
        log.info("Web Application Server stopped");
    }

    @AfterEach
    void tearDown() {
        interruptWebServer();
    }

}
