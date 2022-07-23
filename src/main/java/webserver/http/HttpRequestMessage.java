package webserver.http;

import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

public record HttpRequestMessage(HttpMethod method, URI uri, HttpHeader header, HttpRequestBody body) {

    private static final String CONTENT_LENGTH = "Content-Length";
    private static final String SP = " ";
    private static final int METHOD_INDEX = 0;
    private static final int URI_INDEX = 1;

    public static HttpRequestMessage parse(InputStream in) throws IOException, IllegalArgumentException, IndexOutOfBoundsException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String[] requestLine = br.readLine().split(SP);

        HttpMethod httpMethod = HttpMethod.valueOf(requestLine[METHOD_INDEX]);
        String path = requestLine[URI_INDEX];
        HttpHeader httpHeader = parseHttpHeader(br);

        HttpRequestBody requestBody = new HttpRequestBody();

        if (hasContent(httpHeader)) {
            int contentLength = Integer.parseInt(httpHeader.get(CONTENT_LENGTH));

            String data = IOUtils.readData(br, contentLength);

            requestBody.addAll(HttpRequestUtils.parseQueryString(data));
        }

        return new HttpRequestMessage(httpMethod, URI.create(path), httpHeader, requestBody);
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
}
