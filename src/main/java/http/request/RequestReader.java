package http.request;

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

public class RequestReader {

    private static final Logger log = LoggerFactory.getLogger(RequestReader.class);

    private BufferedReader bufferedReader;

    public RequestReader(InputStream in) throws IOException {
        this.bufferedReader = new BufferedReader(new InputStreamReader(in));
    }

    public HttpRequest create() throws IOException {
        String[] line = bufferedReader.readLine().split(" ");
        String method = line[0];
        String url = line[1];
        String protocol = line[2];
        Map<String, String> header = parseHeader();
        Map<String, String> body = parseBody(header);
        return new HttpRequest(header, body, method, url, protocol);
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

    private Map<String, String> parseBody(Map<String, String> header) throws IOException {
        int contentLength = getContentLength(header);
        return getBodyData(contentLength);
    }

    private Map<String, String> getBodyData(int contentLength) throws IOException {
        String data = readData(bufferedReader, contentLength);
        String decode = URLDecoder.decode(data, UTF_8);
        return HttpRequestUtils.parseQueryString(decode);
    }

    private int getContentLength(Map<String, String> header) {
        if (header.containsKey("Content-Length")) {
            return Integer.parseInt(header.get("Content-Length"));
        }
        return 0;
    }
}
