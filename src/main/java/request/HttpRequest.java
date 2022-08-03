package request;

import static java.nio.charset.StandardCharsets.UTF_8;
import static util.IOUtils.readData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;

public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private final BufferedReader bufferedReader;
    private String method;
    private String url;
    private String protocol;
    private Map<String, String> header;
    private Map<String, String> body;
    private Cookie cookie;

    public HttpRequest(InputStream in) throws IOException {
        this.bufferedReader = new BufferedReader(new InputStreamReader(in));
        String[] line = bufferedReader.readLine().split(" ");
        this.method = line[0];
        this.url = line[1];
        this.protocol = line[2];
        this.header = parseHeader();
        this.body = parseBody();
        this.cookie = parseCookie();
    }

    private Map<String, String> parseHeader() throws IOException {
        String line;
        Map<String, String> header = new HashMap<>();
        while (!(line = bufferedReader.readLine()).equals("")) {
            log.debug("header: {}", line);
            Pair pair = HttpRequestUtils.parseHeader(line);
            header.put(pair.getKey(), pair.getValue());
        }
        return header;
    }

    private Map<String, String> parseBody() throws IOException {
        int contentLength = getContentLength();
        Map<String, String> body = getBodyData(contentLength);
        return body;
    }

    private Cookie parseCookie() {
        Map<String, String> cookie = HttpRequestUtils.parseCookies(header.get("Cookie"));
        return new Cookie(cookie.get("isLogined"), cookie.get("path"));
    }

    private Map<String, String> getBodyData(int contentLength) throws IOException {
        String data = readData(bufferedReader, contentLength);
        String decode = URLDecoder.decode(data, UTF_8);
        return HttpRequestUtils.parseQueryString(decode);
    }

    private int getContentLength() {
        if (header.get("Content-Length").contains("Content-Length")) {
            return Integer.parseInt(header.get("Content-Length"));
        }
        return 0;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getProtocol() {
        return protocol;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public Map<String, String> getBody() {
        return body;
    }

    public Cookie getCookie() {
        return cookie;
    }
}
