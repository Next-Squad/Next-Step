package webserver;

import db.DataBase;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

class RequestHandlerTest {

    private static final Logger log = LoggerFactory.getLogger(RequestHandlerTest.class);

    private static final String USER_CREATE_URI_PATH = "/user/create";
    private String baseUrl;

    private Thread webServerThread = null;

    @BeforeEach
    void setUp() {
        int port = randomPortNumber();

        baseUrl =  "http://localhost:" + port;
        startWebServer(port);
    }

    @Test
    @DisplayName("정상적인 회원가입 요청이 들어오면, 회원정보가 저장된다.")
    void createUserTest() throws IOException, InterruptedException {
        // given
        User expectedUser = new User("jay", "1234", "김진완", "jay@mail.com");
        BodyPublisher userCreateRequestBody =
                BodyPublishers.ofString(getUserCreateParameters(expectedUser));

        String uri = getUserCreateUri();

        HttpRequest request = HttpRequest.newBuilder()
                .POST(userCreateRequestBody)
                .uri(URI.create(uri))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();

        // when
        HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
        User savedUser = DataBase.findUserById(expectedUser.getUserId());

        // then
        assertThat(response.statusCode()).isEqualTo(302);

        HttpHeaders headers = response.headers();
        assertThat(headers).isNotNull();

        Optional<String> locationHeader = headers.firstValue("Location");
        assertThat(locationHeader.isPresent()).isTrue();
        assertThat(locationHeader.orElseThrow()).isEqualTo("/index.html");

        assertThat(savedUser).isNotNull();
        assertThat(savedUser).isEqualTo(expectedUser);
    }

    private String getUserCreateUri() {
        return baseUrl + USER_CREATE_URI_PATH;
    }

    private String getUserCreateParameters(User user) {
        return "userId=" + user.getUserId() +
                "&password=" + user.getPassword() +
                "&name=" + user.getName() +
                "&email=" + user.getEmail();
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