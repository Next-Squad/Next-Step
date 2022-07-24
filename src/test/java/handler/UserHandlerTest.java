package handler;

import db.DataBase;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import webserver.http.HttpStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UserHandlerTest extends HandlerTest {

    private final String USER_CREATE_URI_PATH = "/user/create";
    private final String USER_LOGIN_URI_PATH = "/user/login";

    @Nested
    @DisplayName("회원가입 요청은")
    class CreateUserTest {

        @Test
        @DisplayName("정상적인 회원 정보가 들어오면, 회원가입이 성공한다.")
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
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            User savedUser = DataBase.findUserById(expectedUser.userId());

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
    }

    @Nested
    @DisplayName("로그인 요청은")
    class LoginTest {

        @Test
        void 존재하는_회원이면_성공한다 () throws IOException, InterruptedException {
            // given
            User expectedUser = new User("jay", "1234", "김진완", "jay@mail.com");
            DataBase.addUser(expectedUser);
            BodyPublisher bodyPublisher = BodyPublishers.ofString(getParameterFromMap(
                    Map.of(
                            "userId", expectedUser.userId(),
                            "password", expectedUser.password()
                    )
            ));

            String uri = baseUrl + USER_LOGIN_URI_PATH;

            HttpRequest request = HttpRequest.newBuilder()
                    .POST(bodyPublisher)
                    .uri(URI.create(uri))
                    .build();

            HttpClient httpClient = HttpClient.newHttpClient();

            // when
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.getCode());

            HttpHeaders headers = response.headers();
            assertThat(headers).isNotNull();

            Optional<String> cookies = headers.firstValue("Set-Cookie");
            assertThat(cookies.isPresent()).isTrue();
            assertThat(cookies.orElseThrow()).isEqualTo("logined=true");
        }

        @Test
        void 존재하지만_비밀번호가_다르면_로그인_실패_페이지로_이동한다() throws IOException, InterruptedException {
            // given
            User expectedUser = new User("jay", "1234", "김진완", "jay@mail.com");
            DataBase.addUser(expectedUser);
            BodyPublisher bodyPublisher = BodyPublishers.ofString(getParameterFromMap(
                    Map.of(
                            "userId", expectedUser.userId(),
                            "password", expectedUser.password() + "5678"
                    )
            ));

            String uri = baseUrl + USER_LOGIN_URI_PATH;

            HttpRequest request = HttpRequest.newBuilder()
                    .POST(bodyPublisher)
                    .uri(URI.create(uri))
                    .build();

            HttpClient httpClient = HttpClient.newHttpClient();

            // when
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.getCode());

            HttpHeaders headers = response.headers();
            assertThat(headers).isNotNull();

            Optional<String> cookies = headers.firstValue("Set-Cookie");
            assertThat(cookies.isPresent()).isTrue();
            assertThat(cookies.orElseThrow()).isEqualTo("logined=false");
        }

        @Test
        void 존재하지_않는_회원이면_로그인_실패_페이지로_이동한다 () throws IOException, InterruptedException {
            // given
            BodyPublisher bodyPublisher = BodyPublishers.ofString(getParameterFromMap(
                    Map.of(
                            "userId", "no_user",
                            "password", "1234"
                    )
            ));

            String uri = baseUrl + USER_LOGIN_URI_PATH;

            HttpRequest request = HttpRequest.newBuilder()
                    .POST(bodyPublisher)
                    .uri(URI.create(uri))
                    .build();

            HttpClient httpClient = HttpClient.newHttpClient();

            // when
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());

            HttpHeaders headers = response.headers();
            assertThat(headers).isNotNull();

            Optional<String> cookies = headers.firstValue("Set-Cookie");
            assertThat(cookies.isPresent()).isTrue();
            assertThat(cookies.orElseThrow()).isEqualTo("logined=false");
        }
    }

    private String getUserCreateUri() {
        return baseUrl + USER_CREATE_URI_PATH;
    }

    private String getUserCreateParameters(User user) {
        return "userId=" + user.userId() +
                "&password=" + user.password() +
                "&name=" + user.name() +
                "&email=" + user.email();
    }

    private String getParameterFromMap(Map<String, String> map) {
        List<String> parameters = new ArrayList<>();

        for (String key : map.keySet()) {
            parameters.add(key + "=" + map.get(key));
        }

        return parameters.stream()
                .reduce((obj, nested) -> obj + "&" + nested)
                .orElseThrow();
    }
}