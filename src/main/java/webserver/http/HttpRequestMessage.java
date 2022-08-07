package webserver.http;

import static webserver.http.HttpHeader.CONTENT_LENGTH;

import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

public record HttpRequestMessage(HttpRequestLine requestLine, HttpHeader header, HttpRequestBody body) {

    public static HttpRequestMessage parse(InputStream in) throws IOException, IllegalArgumentException, IndexOutOfBoundsException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        // Http Request Line
        HttpRequestLine httpRequestLine = new HttpRequestLine(br.readLine());

        // Http Header
        HttpHeader httpHeader = parseHttpHeader(br);

        // Http Request Body
        HttpRequestBody requestBody = new HttpRequestBody();
        if (hasContent(httpHeader)) {
            int contentLength = Integer.parseInt(httpHeader.get(CONTENT_LENGTH));

            String data = IOUtils.readData(br, contentLength);

            requestBody.addAll(HttpRequestUtils.parseQueryString(data));
        }

        return new HttpRequestMessage(httpRequestLine, httpHeader, requestBody);
    }

    private static HttpHeader parseHttpHeader(BufferedReader br) throws IOException {
        HttpHeader header = new HttpHeader();
        String line;
        while((line = br.readLine()) != null && !line.isEmpty()) {
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
            header.add(pair.getKey(), pair.getValue());
        }

        return header;
    }

    private static boolean hasContent(HttpHeader httpHeader) {
        return httpHeader.keySet().contains(CONTENT_LENGTH);
    }


    public HttpMethod method() {
        return this.requestLine.getMethod();
    }

    public URI uri() {
        return this.requestLine.getUri();
    }
}
