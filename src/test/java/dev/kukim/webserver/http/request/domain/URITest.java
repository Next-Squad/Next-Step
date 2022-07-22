package dev.kukim.webserver.http.request.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("URI 클래스")
class URITest {

	@Nested
	@DisplayName("생성자")
	class Describe_constructor {

	    @Nested
	    @DisplayName("만약 Path와 QueryParameters이 포함된 URI 문자열이 주어진다면")
	    class Context_with_String_path_and_queryparameters {

	        @Test
	        @DisplayName("URI 객체를 반환한다.")
	        void It_returns_a_object() {
				String rawUri = "/user/create?userId=kukim&password=1234";

				URI sut = new URI(rawUri);

				assertThat(sut.getPath()).isEqualTo("/user/create");
				assertThat(sut.findQueryParameterBy("userId")).isEqualTo("kukim");
				assertThat(sut.findQueryParameterBy("password")).isEqualTo("1234");
			}
	    }

		@Nested
		@DisplayName("만약 Path와 resource가 포함된 URI 문자열이 주어진다면 ")
		class Context_with_String_path_and_resource {

			@Test
			@DisplayName("URI 객체를 반환한다.")
			void It_returns_a_object() {
				String rawUri = "/user/create/form.html";

				URI sut = new URI(rawUri);

				assertThat(sut.getPath()).isEqualTo("/user/create/form.html");
			}
		}

	}
}
