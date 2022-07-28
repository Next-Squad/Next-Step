package http.request;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("RequestMessageBody 클래스")
class RequestMessageBodyTest {

	@Nested
	@DisplayName("from 메소드는")
	class Describe_from {

		@Nested
		@DisplayName("BufferedReader 객체와 String(ContentLength)를 인자로 받으면")
		class Context_with_BufferedRader_and_String {

			@Test
			@DisplayName("새로운 RequestMessageBody 객체를 반환한다")
			void it_returns_new_RequestMessageBody_instance() throws IOException {
				//given
				String rawMessageBody = "userId=nathan&password=123123&name=나단&email=phs5731@nav.com";
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(rawMessageBody.getBytes());
				BufferedReader br = new BufferedReader(new InputStreamReader(byteArrayInputStream));

				//when
				RequestMessageBody requestMessageBody = RequestMessageBody.from(br, rawMessageBody.length());

				//then
				Assertions.assertThat(requestMessageBody.getMessageBody())
					.isEqualTo("userId=nathan&password=123123&name=나단&email=phs5731@nav.com");
			}
		}
	}

}
