package http.request;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("RequestLine 클래스")
class RequestLineTest {

	@Nested
	@DisplayName("from 메소드는")
	class Describe_from{

		@Nested
		@DisplayName("파싱된 Request Line이 주어진다면")
		class Context_with_parsed_RequestLine {

			@Test
			@DisplayName("RequestLine 객체를 반환한다")
			void it_returns_a_RequestLine() {
				String rawHttpRequestLine = "GET /index.html?userId=3333 HTTP/1.1";
				RequestLine requestLine = RequestLine.from(rawHttpRequestLine);

				assertThat(requestLine.getHttpMethod()).isEqualTo(HttpMethod.GET);
				assertThat(requestLine.getRequestUri()).hasToString("/index.html?userId=3333");
				assertThat(requestLine.getHttpVersion().getVersion()).isEqualTo("HTTP/1.1");

			}

		}
	}


}
