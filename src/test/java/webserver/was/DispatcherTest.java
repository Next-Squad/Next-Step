package webserver.was;

import handler.Handler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import webserver.http.HttpHeader;
import webserver.http.HttpMethod;
import webserver.http.HttpRequest;
import webserver.http.HttpRequestBody;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;

import java.net.URI;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class DispatcherTest {

    private Dispatcher dispatcher;

    @BeforeEach
    void setUp() {
        RequestMappingInfo mappingInfo = new RequestMappingInfo(HttpMethod.GET, "/test");
        Handler handler = request -> new HttpResponse(HttpStatus.OK, new HttpHeader());

        dispatcher = new Dispatcher(
                new HandlerMapping(Map.of(
                        mappingInfo, handler)
                )
        );
    }

    @Nested
    @DisplayName("요청을 수행할 때")
    class HandleRequestTest {

        @Test
        void 존재하는_매핑_정보면_요청_수행을_성공한다 () {
            // given
            HttpRequest request = new HttpRequest(
                    HttpMethod.GET,
                    new HttpHeader(),
                    new HttpRequestBody(),
                    URI.create("/test")
            );

            // when
            HttpResponse httpResponse = dispatcher.handlerRequest(request);

            // then
            assertThat(httpResponse).isNotNull();
            assertThat(httpResponse.getStatus()).isEqualTo(HttpStatus.OK);
        }

        @Test
        void 존재하지_않는_매핑_정보면_찾을_수_없다는_응답이_반환된다 () {
            // given
            HttpRequest request = new HttpRequest(
                    HttpMethod.POST,
                    new HttpHeader(),
                    new HttpRequestBody(),
                    URI.create("/not/exists")
            );

            // when
            HttpResponse httpResponse = dispatcher.handlerRequest(request);

            // then
            assertThat(httpResponse).isNotNull();
            assertThat(httpResponse.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }
}