package webserver.was;

import handler.Handler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import webserver.http.HttpMethod;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RequestMappingProcessorTest {

    @Nested
    @DisplayName("매핑정보 검증은")
    class ValidateTest{

        @Test
        void 매핑_어노테이션이_붙으면_매핑_정보가_생성된다() {
            // given
            List<Class<?>> classes = List.of(ValidationSuccess.class);

            // when
            RequestMappingProcessor requestMappingProcessor = new RequestMappingProcessor(classes);

            // then
            Map<RequestMappingInfo, Handler> handlerMappingInfo = requestMappingProcessor.getHandlerMappingInfo();
            assertThat(handlerMappingInfo).isNotNull();
            assertThat(handlerMappingInfo).isNotEmpty();
        }

        @Test
        void  매핑_어노테이션이_붙어있지_않으면_매핑_정보가_생성되지_않는다() {
            // given
            List<Class<?>> classes = List.of(NoAnnotation.class);

            // when
            RequestMappingProcessor requestMappingProcessor = new RequestMappingProcessor(classes);

            // then
            Map<RequestMappingInfo, Handler> handlerMappingInfo = requestMappingProcessor.getHandlerMappingInfo();
            assertThat(handlerMappingInfo).isNotNull();
            assertThat(handlerMappingInfo).isEmpty();
        }

        @Test
        void 필드_접근_제어자가_public_이_아니면_예외가_발생한다() {
            // given
            List<Class<?>> classes = List.of(NonePublicField.class);

            // then
            assertThatThrownBy(() -> new RequestMappingProcessor(classes))
                    .isInstanceOf(IllegalStateException.class);
        }

        @Test
        void 정해진_타입이_아닌_필드면_예외가_발생한다() {
            // given
            List<Class<?>> classes = List.of(NoneRequiredType.class);

            // then
            assertThatThrownBy(() -> new RequestMappingProcessor(classes))
                    .isInstanceOf(IllegalStateException.class);
        }

        @Test
        void 이미_같은_매핑정보가_있으면_예외가_발생한다() {
            // given
            List<Class<?>> classes = List.of(DuplicatedMapping.class);

            // then
            assertThatThrownBy(() -> new RequestMappingProcessor(classes))
                    .isInstanceOf(IllegalStateException.class);
        }
    }

    public static class ValidationSuccess {

        @RequestMapping(method = HttpMethod.GET, url = "/test")
        public final Handler test = (request) -> null;
    }

    public static class NoAnnotation {
        public final Handler test = (request) -> null;
    }

    public static class NonePublicField {
        @RequestMapping(method = HttpMethod.GET, url = "/test")
        private final Handler test = (request) -> null;
    }

    public static class NoneRequiredType {
        @RequestMapping(method = HttpMethod.GET, url = "/test")
        private final String test = "hello world";
    }

    public static class DuplicatedMapping {
        @RequestMapping(method = HttpMethod.GET, url = "/test")
        private final Handler test_a = (request) -> null;

        @RequestMapping(method = HttpMethod.GET, url = "/test")
        private final Handler test_b = (request) -> null;
    }

    @Nested
    @DisplayName("매핑정보 생성은")
    class CreateMappingInfoTest {

        @Test
        void 접근_가능한_기본_생성자가_없으면_예외가_발생한다() {
            // given
            List<Class<?>> classes = List.of(NonePublicConstructorClass.class);

            // then
            assertThatThrownBy(() -> new RequestMappingProcessor(classes))
                    .isInstanceOf(IllegalStateException.class);
        }

        @Test
        void 접근_가능한_기본_생성자가_있으면_매핑정보가_생성된다() {
            // given
            List<Class<?>> classes = List.of(PublicConstructorClass.class);

            // when
            RequestMappingProcessor requestMappingProcessor = new RequestMappingProcessor(classes);
            Map<RequestMappingInfo, Handler> handlerMappingInfo =
                    requestMappingProcessor.getHandlerMappingInfo();

            // then
            assertThat(handlerMappingInfo).isNotNull();
            assertThat(handlerMappingInfo).isNotEmpty();
        }
    }

    public static class NonePublicConstructorClass {
        private NonePublicConstructorClass() {}

        @RequestMapping(method = HttpMethod.GET, url = "/test")
        public final Handler test = (request) -> null;
    }

    public static class PublicConstructorClass {

        @RequestMapping(method = HttpMethod.GET, url = "/test")
        public final Handler test = (request) -> null;
    }
}