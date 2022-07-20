package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestMessageTest {

    @Test
    @DisplayName("정상적인 Http Request Message 가 입력되면, 파싱에 성공한다")
    void parseTest() throws IOException {
        // given
        String messageString = """
                GET /index.html HTTP/1.1
                Host: localhost:8080
                Connection: keep-alive
                """;
        ByteArrayInputStream in = new ByteArrayInputStream(messageString.getBytes());

        // when
        HttpRequestMessage requestMessage = HttpRequestMessage.parse(in);

        // then
        assertThat(requestMessage.method()).isEqualTo(HttpMethod.GET);
        assertThat(requestMessage.uri()).isEqualTo(URI.create("/index.html"));

        HttpHeader header = requestMessage.header();
        assertThat(header.get("Host")).isEqualTo("localhost:8080");
        assertThat(header.get("Connection")).isEqualTo("keep-alive");
    }

}