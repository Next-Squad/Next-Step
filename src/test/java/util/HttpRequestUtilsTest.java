package util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.HttpRequestUtils.Pair;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpRequestUtilsTest {
    @Test
    @DisplayName("유효한 쿼리스트링이 입력되면, key value 파싱에 성공한다.")
    public void parseQueryString() {
        String queryString = "userId=javajigi";
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
        assertThat(parameters.get("userId")).isEqualTo("javajigi");
        assertThat(parameters.get("password")).isNull();

        queryString = "userId=javajigi&password=password2";
        parameters = HttpRequestUtils.parseQueryString(queryString);
        assertThat(parameters.get("userId")).isEqualTo("javajigi");
        assertThat(parameters.get("password")).isEqualTo("password2");
    }

    @Test
    @DisplayName("쿼리스트링에 null, 빈 문자열, 공백 문자열 이 입력되면, 빈 파싱결과가 나온다.")
    public void parseQueryString_null() {
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(null);
        assertThat(parameters.isEmpty()).isTrue();

        parameters = HttpRequestUtils.parseQueryString("");
        assertThat(parameters.isEmpty()).isTrue();

        parameters = HttpRequestUtils.parseQueryString(" ");
        assertThat(parameters.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("유효하지 않은 쿼리스트링이 입력되면, 빈 파싱결과가 나온다.")
    public void parseQueryString_invalid() {
        String queryString = "userId=javajigi&password";
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
        assertThat(parameters.get("userId")).isEqualTo("javajigi");
        assertThat(parameters.get("password")).isNull();
    }

    @Test
    @DisplayName("유효한 쿠키 문자열이 입력되면, 파싱에 성공한다.")
    public void parseCookies() {
        String cookies = "logined=true; JSessionId=1234";
        Map<String, String> parameters = HttpRequestUtils.parseCookies(cookies);
        assertThat(parameters.get("logined")).isEqualTo("true");
        assertThat(parameters.get("JSessionId")).isEqualTo("1234");
        assertThat(parameters.get("session")).isNull();
    }

    @Test
    @DisplayName("유효한 keyValue 문자열이 입력되면, 파싱에 성공한다.")
    public void getKeyValue() {
        Pair pair = HttpRequestUtils.getKeyValue("userId=javajigi", "=");
        assertThat(pair).isEqualTo(new Pair("userId", "javajigi"));
    }

    @Test
    @DisplayName("유효하지 않은 keyValue 문자열이 입력되면, 파싱에 실패한다.")
    public void getKeyValue_invalid() {
        Pair pair = HttpRequestUtils.getKeyValue("userId", "=");
        assertThat(pair).isNull();
    }

    @Test
    @DisplayName("유효한 header 문자열이 입력되면, 파싱에 성공한다.")
    public void parseHeader() {
        String header = "Content-Length: 59";
        Pair pair = HttpRequestUtils.parseHeader(header);
        assertThat(pair).isEqualTo(new Pair("Content-Length", "59"));
    }

    @Test
    @DisplayName("확장자가 포함된 url 문자열이 입력되면, 파싱에 성공한다.")
    public void parseExtension() {
        String expectedExtension = "html";
        String url = "/index.html";
        String extension = HttpRequestUtils.parseExtension(url);

        assertThat(extension).isEqualTo(expectedExtension);
    }
}
