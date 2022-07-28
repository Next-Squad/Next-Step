package http.request;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("HttpRequest 클래스")
class HttpRequestTest {

	@Nested
	@DisplayName("from 메소드는")
	class Describe_from {

		@Nested
		@DisplayName("[HTTP GET REQUEST] InputStream 객체를 인자로 받으면")
		class Context_with_GET_InputStream_instance {

			@Test
			@DisplayName("새로운 GET HttpRequest 객체를 반환한다.")
			void it_returns_new_GET_HttpRequest_instance() throws IOException {
				//given
				String str = "GET /index.html HTTP/1.1\r\n"
					+ "Host: localhost:8080\r\n"
					+ "Connection: keep-alive\r\n"
					+ "Accept: */*\r\n"
					+ "\r\n";

				//when
				ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
				HttpRequest httpRequest = HttpRequest.from(in);
				RequestLine requestLine = httpRequest.getRequestLine();
				RequestHeaders requestHeaders = httpRequest.getRequestHeaders();

				//then
				assertThat(requestLine.getHttpMethod()).isEqualTo(HttpMethod.GET);
				assertThat(requestLine.getRequestUri()).hasToString("/index.html");
				assertThat(requestHeaders.getHeader("Host")).isEqualTo("localhost:8080");
				assertThat(requestHeaders.getHeader("Connection")).isEqualTo("keep-alive");
				assertThat(requestHeaders.getHeader("Accept")).isEqualTo("*/*");
				assertThat(requestHeaders.hasContentLength()).isFalse();
			}
		}

		@Nested
		@DisplayName("[HTTP POST REQUEST] InputStream 객체를 인자로 받으면")
		class Context_with_POST_InputStream_instance {

			@Test
			@DisplayName("새로운 POST HttpRequest 객체를 반환한다.")
			void it_returns_new_POST_HttpRequest_instance() throws IOException {
				//given
				String str = "POST /user/create HTTP/1.1\r\n"
					+ "Host: localhost:8080\r\n"
					+ "Connection: keep-alive\r\n"
					+ "Content-Length: 58\r\n"
					+ "Content-Type: application/x-www-form-urlencoded\r\n"
					+ "Accept: */*\r\n"
					+ "\r\n"
					+"userId=nathan&password=123123&name=나단&email=nathan@dev.com";
				//TODO Content-Length, Content-Type 추가 해야한다.
				//when
				ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
				HttpRequest httpRequest = HttpRequest.from(in);
				RequestLine requestLine = httpRequest.getRequestLine();
				RequestHeaders requestHeaders = httpRequest.getRequestHeaders();
				RequestMessageBody requestMessageBody = httpRequest.getRequestMessageBody();

				//then
				assertThat(requestLine.getHttpMethod()).isEqualTo(HttpMethod.POST);
				assertThat(requestLine.getRequestUri()).hasToString("/user/create");
				assertThat(requestHeaders.getHeader("Host")).isEqualTo("localhost:8080");
				assertThat(requestHeaders.getHeader("Connection")).isEqualTo("keep-alive");
				assertThat(requestHeaders.getHeader("Accept")).isEqualTo("*/*");
				assertThat(requestHeaders.hasContentLength()).isTrue();
				assertThat(requestHeaders.getContentLength()).isEqualTo(58);
				assertThat(requestHeaders.getHeader("Content-Type")).isEqualTo(
					"application/x-www-form-urlencoded");
				assertThat(requestMessageBody.getMessageBody()).isEqualTo(
					"userId=nathan&password=123123&name=나단&email=nathan@dev.com");
			}
		}
	}
}
