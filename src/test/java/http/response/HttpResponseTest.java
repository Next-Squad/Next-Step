package http.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("HttpResponse 클래스")
class HttpResponseTest {

	@Nested
	@DisplayName("[HTTP RESPONSE 200 OK] ok 메소드는")
	class Describe_ok {

		@Nested
		@DisplayName("아무것도 인자를 받지 않으면")
		class Context_with_null {

			@Test
			@DisplayName("default 200 OK HttpResponse 객체를 반환한다.")
			void dit_returns_default_200_OK_HttpResponse_instance() {
				//when
				HttpResponse response = HttpResponse.ok();
				StatusLine statusLine = response.getStatusLine();
				ResponseHeaders responseHeaders = response.getResponseHeaders();

				//then
				assertThat(statusLine).hasToString("HTTP/1.1 200 OK");
				assertThat(responseHeaders.getHeader("Content-Type")).isEqualTo(
					"text/plain;charset=utf-8");
				assertThat(responseHeaders.getHeader("Content-Length")).isEqualTo("11");
			}

		}

		@Nested
		@DisplayName("String(viewName)을 하나만 받을 때")
		class Context_with_a_String {

			@Test
			@DisplayName("해당 viewName의 파일이 있다면, 그 파일을 담은 200 OK HttpResponse 객체를 반환한다.")
			void it_returns_viewName_OK_HttpResponse_instance() throws IOException {
				//given
				String viewName = "/index.html";
				byte[] messageBody = Files.readAllBytes(new File("./webapp" + viewName).toPath());

				//when
				HttpResponse response = HttpResponse.ok(viewName);
				StatusLine statusLine = response.getStatusLine();
				ResponseHeaders responseHeaders = response.getResponseHeaders();

				//then
				assertThat(statusLine).hasToString("HTTP/1.1 200 OK");
				assertThat(responseHeaders.getHeader("Content-Type")).isEqualTo(
					"text/html;charset=utf-8");
				assertThat(responseHeaders.getHeader("Accept")).isEqualTo("text/html, */*; q=0.1");
				assertThat(responseHeaders.getHeader("Content-Length")).isEqualTo(String.valueOf(messageBody.length));
			}

			@Test
			@DisplayName("해당 viewName의 파일이 없다면, NoSuchFileException을 던진다.")
			void it_throws_NoSuchFileException() throws IOException {
				//given
				String viewName = "/indexing.html";

				//then
				assertThatThrownBy(() -> HttpResponse.ok(viewName)).isInstanceOf(
					NoSuchFileException.class);
			}

		}
	}

	@Nested
	@DisplayName("[HTTP RESPONSE 302 Found] found 메소드는")
	class Discribe_found{

		@Nested
		@DisplayName("String(redirectURI)를 하나만 받으면")
		class Context_with_a_String {

			@Test
			@DisplayName("해당 인자를 Location으로 갖는 302 Found HttpResponse 객체를 반환한다.")
			void it_returns_302_Found_HttpResponse_instance() {
				//given
				String redirectURI = "/index.html";

				//when
				HttpResponse response = HttpResponse.found(redirectURI);
				StatusLine statusLine = response.getStatusLine();
				ResponseHeaders responseHeaders = response.getResponseHeaders();


				//then
				assertThat(statusLine).hasToString("HTTP/1.1 302 Found");
				assertThat(responseHeaders.getHeader("Location")).isEqualTo("/index.html");
			}
		}

		@Nested
		@DisplayName("String(redirectURI)와 boolean(Cookie)를 받으면")
		class Context_with_a_String_and_a_Cookie {

			@Test
			@DisplayName("해당 인자를 Location으로 갖고, Cookie가 세팅된 302 Found HttpResponse 객체를 반환한다.")
			void it_returns_302_Found_HttpResponse_instance_with_Cookie() {
				//given
				String redirectURI = "/index.html";
				boolean login_cookie = true;

				//when
				HttpResponse response = HttpResponse.found(redirectURI, login_cookie);
				StatusLine statusLine = response.getStatusLine();
				ResponseHeaders responseHeaders = response.getResponseHeaders();


				//then
				assertThat(statusLine).hasToString("HTTP/1.1 302 Found");
				assertThat(responseHeaders.getHeader("Location")).isEqualTo("/index.html");
				assertThat(responseHeaders.getHeader("Set-Cookie")).isEqualTo("logined=true; path=/;");
			}
		}
	}
}
