package dev.kukim.webserver.response;

import static org.assertj.core.api.Assertions.assertThat;

import dev.kukim.webserver.http.response.HttpResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("HttpResponse 클래스")
class HttpResponseTest {

	@Nested
	@DisplayName("sendRedirect 메서드")
	class Describe_sendRedirect {

		@Nested
		@DisplayName("만약 /index.html으로 리다이렉트 한다면")
		class Context_with_redirect_index {

			@Test
			@DisplayName("HTTP Response 302 Found를 반환한다")
			void It_returns_Response_303() throws IOException {
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				HttpResponse response = new HttpResponse(byteArrayOutputStream);

				response.sendRedirect("/index.html");

				assertThat(byteArrayOutputStream.toString())
					.hasToString("HTTP/1.1 302 Found \r\n"
						+ "Location: /index.html\r\n"
						+ "\r\n");
			}
		}
	}

	@Nested
	@DisplayName("sendOK 메서드")
	class Describe_sendOK {

		@Nested
		@DisplayName("만약 response body가 존재하고 반환한다면")
		class Context_with_200_OK {

			@Test
			@DisplayName("HTTP Response 200 OK와 Body를 반환한다")
			void It_returns_Response_303() throws IOException {
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				HttpResponse response = new HttpResponse(byteArrayOutputStream);
				String rawBody = "<html><body><h1>Test</h1></body></html>";

				response.setBody(rawBody.getBytes(StandardCharsets.UTF_8));
				response.sendOK();

				assertThat(byteArrayOutputStream.toString())
					.hasToString("HTTP/1.1 200 OK \r\n"
						+ "Content-Type: text/html;charset=utf-8\r\n"
						+ "Content-Length: 39\r\n\r\n"
						+ "<html><body><h1>Test</h1></body></html>");
			}
		}
	}
}
