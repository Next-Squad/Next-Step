package handler;

import db.DataBase;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UserHandlerTest extends HandlerTest {

    private final String USER_CREATE_URI_PATH = "/user/create";

    @Test
    @DisplayName("정상적인 회원가입 요청이 들어오면, 회원정보가 저장된다.")
    void createUserTest() throws IOException, InterruptedException {
        // given
        User expectedUser = new User("jay", "1234", "김진완", "jay@mail.com");
        HttpRequest.BodyPublisher userCreateRequestBody =
                HttpRequest.BodyPublishers.ofString(getUserCreateParameters(expectedUser));

        String uri = getUserCreateUri();

        HttpRequest request = HttpRequest.newBuilder()
                .POST(userCreateRequestBody)
                .uri(URI.create(uri))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();

        // when
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
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
}