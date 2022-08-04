package webserver.http;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.net.URI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class HttpRequestLineTest {

	@Nested
	@DisplayName("HttpRequestLine 를 생성할 때")
	class Create {

		@Test
		void 정상적인_RequestLine_이_들어오면_객체가_생성된다() {
		    // given
			String requestLine = "GET /index.html HTTP/1.1";
			HttpMethod expectedMethod = HttpMethod.GET;
			URI expectedURI = URI.create("/index.html");

		    // when
			HttpRequestLine httpRequestLine = new HttpRequestLine(requestLine);

			// then
			assertThat(httpRequestLine).isNotNull();
			assertThat(httpRequestLine.getMethod()).isEqualTo(expectedMethod);
			assertThat(httpRequestLine.getUri()).isEqualTo(expectedURI);
		}

		@Test
		void RequestLine_이_null_이면_예외가_발생한다() {
		    // given
			String requestLine = null;

		    // then
			assertThatThrownBy(() -> new HttpRequestLine(requestLine))
				.isInstanceOf(NullPointerException.class);
		}
	}

}