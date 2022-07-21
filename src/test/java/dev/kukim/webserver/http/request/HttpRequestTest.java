package dev.kukim.webserver.http.request;

import static org.assertj.core.api.Assertions.assertThat;

import dev.kukim.webserver.http.request.domain.HttpMethod;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("HttpRequestTest 클래스")
class HttpRequestTest {

	@Nested
	@DisplayName("생성자")
	class Describe_constructor {

	    @Nested
	    @DisplayName("만약 쿼리 파라미터가 존재하는 Http GET Reuqest 문자열이 InputStream으로 주어진다면")
	    class Context_with_valid_httpRequest_inputStream {

	        @Test
	        @DisplayName("HttpRequest 객체를 반환한다")
	        void It_returns_a_HttpRequest() throws IOException {
				// Arrange
				String httpRequestString = "GET /user/create?userId=kukim&password=1234&name=kunhee HTTP/1.1\n" +
					"Host: localhost:8080\n" +
					"User-Agent: curl/7.64.1\n" +
					"Accept: */*\n" +
					"\n";

				try (InputStream inputStream = new ByteArrayInputStream(httpRequestString.getBytes(
					StandardCharsets.UTF_8))) {

				// Act
					HttpRequest sut = new HttpRequest(inputStream);

				// Assert
					assertThat(sut.getMethod()).isEqualTo(HttpMethod.GET);
					assertThat(sut.getQueryParameter("userId")).isEqualTo("kukim");
					assertThat(sut.getRequestHeader("Host")).isEqualTo("localhost:8080");
				}
	        }
	    }
	}

}
