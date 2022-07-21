package dev.kukim.webserver.http.request.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("RequestHeaders 클래스")
class RequestHeadersTest {

	@Nested
	@DisplayName("생성자")
	class Describe_constructor {

	    @Nested
	    @DisplayName("만약 HTTP Request Header가 문자열 리스트로 주어진다면")
	    class Context_with_http_request_header_list {

	        @Test
	        @DisplayName("RequestHeaders 객체를 반환한다")
	        void It_returns_a_object() {
				List<String> rawHeaders = new ArrayList<>();
				rawHeaders.add("Host: localhost:8080");
				rawHeaders.add("User-Agent: curl/7.64.1");
				rawHeaders.add("Accept: */*");

				RequestHeaders sut = new RequestHeaders(rawHeaders);

				assertThat(sut.getHost()).isEqualTo("localhost:8080");
				assertThat(sut.getUserAgent()).isEqualTo("curl/7.64.1");
				assertThat(sut.getAccept()).isEqualTo("*/*");
			}
	    }
	}

}
