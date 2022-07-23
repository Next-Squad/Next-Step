package webserver.was;

import handler.Handler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import webserver.http.HttpMethod;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class HandlerMappingTest {

    // 매핑 정보를 찾을 때
        // 존재하는 매핑 정보면, 반환한다.
        // 존재하지 않는 매핑 정보면 빈 결과를 반환한다.

    private HandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new HandlerMapping(Map.of(
                new RequestMappingInfo(HttpMethod.GET, "/test"), (request) -> null
        ));
    }

    @Nested
    @DisplayName("매핑 정보를 찾을 때")
    class FindHandlerTest {

        @Test
        void 존재하는_매핑_정보면_반환한다() {
            // given
            HttpMethod get = HttpMethod.GET;
            String url = "/test";

            // when
            Optional<Handler> findResult = handlerMapping.findHandler(get, url);

            // then
            assertThat(findResult.isPresent()).isTrue();
        }

        @Test
        void 존재하지_않는_매핑_정보면_빈_결과를_반환한다() {
            // given
            HttpMethod post = HttpMethod.POST;
            String url = "/not/exists";

            // when
            Optional<Handler> findResult = handlerMapping.findHandler(post, url);

            // then
            assertThat(findResult.isEmpty()).isTrue();
        }
    }
}