package webserver.http;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class HttpResponseMessageTest {

	@Nested
	@DisplayName("HttpResponseMessage 를 생성할 때")
	class Create{

		@Test
		void 정적_파일_응답에는_응답출력을_성공한다() {
		    // given
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			HttpStatus status = HttpStatus.OK;
			String html = "<html><h1>안녕하세요</h1></html>";
			String expectedResponse = "HTTP/1.1 200 OK\r\n\r\n<html><h1>안녕하세요</h1></html>";
			HttpResponse httpResponse = new HttpResponse(status, new HttpHeader(), html.getBytes());

			// when
			HttpResponseMessage httpResponseMessage = new HttpResponseMessage(out, httpResponse);
			httpResponseMessage.flush();

			// then
			String flushedResponseMessage = out.toString();
			assertThat(flushedResponseMessage).isEqualTo(expectedResponse);
		}
	}
}