package dev.kukim.webserver.http.request.domain;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("RequestLineTest 클래스")
class RequestLineTest {
	
	@Nested
	@DisplayName("생성자")
	class Describe_Constructor {
	
	    @Nested
	    @DisplayName("만약 정상적인 Http Request Line 문자열이 주어진다면")
	    class Context_with_valid_reqeust_line {
	        
	        @Test
	        @DisplayName("RequestLine 객체를 반환한다")
	        void It_returns_a_Object() {
				String rawRequestLine = "GET /user/create?userId=kukim&password=1234&name=kunhee HTTP/1.1";

				RequestLine sut = new RequestLine(rawRequestLine);

				assertThat(sut.getMethod()).isEqualTo(HttpMethod.GET);
				assertThat(sut.getPath()).isEqualTo("/user/create");
				assertThat(sut.getQueryParameter("userId")).isEqualTo("kukim");
				assertThat(sut.getQueryParameter("password")).isEqualTo("1234");
				assertThat(sut.getQueryParameter("name")).isEqualTo("kunhee");
				assertThat(sut.getVersion()).isEqualTo("HTTP/1.1");
			}
	    }
	}
}
