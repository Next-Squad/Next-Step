package http.request;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("RequestHeaders 클래스")
class RequestHeadersTest {

	@Nested
	@DisplayName("addHeader 메소드는")
	class Describe_addHeader {

		@Nested
		@DisplayName("String(line)을 인자로 받으면")
		class Context_with_String {

			@Test
			@DisplayName("headers에 값을 key, value로 넣는다.")
			void it_puts_in_headers() {

				RequestHeaders requestHeaders = new RequestHeaders();
				requestHeaders.addHeader("Host: localhost:8080");
				requestHeaders.addHeader("Connection: keep-alive");
				requestHeaders.addHeader("Accept: */*");

				assertThat(requestHeaders.getHeader("Host")).isEqualTo("localhost:8080");
				assertThat(requestHeaders.getHeader("Connection")).isEqualTo("keep-alive");
				assertThat(requestHeaders.getHeader("Accept")).isEqualTo("*/*");
			}
		}
	}
}
