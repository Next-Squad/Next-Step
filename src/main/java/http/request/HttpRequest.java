package http.request;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private Map<String, String> header;
    private Map<String, String> body;
    private String method;
    private String url;
    private String protocol;
    private Cookie cookie;

    public HttpRequest(Map<String, String> header,
        Map<String, String> body, String method, String url, String protocol) {
        this.header = header;
        this.body = body;
        this.method = method;
        this.url = url;
        this.protocol = protocol;
        this.cookie = parseCookie(header);
    }

    private Cookie parseCookie(Map<String, String> header) {
        Map<String, String> cookie = HttpRequestUtils.parseCookies(header.get("Cookie"));
        return new Cookie(cookie.get("isLogined"), cookie.get("path"));
    }
}
