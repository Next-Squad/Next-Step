package webserver;

import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

public record HttpRequestMessage(HttpMethod method, URI uri, HttpHeader header) {
    private static final String SP = " ";
    private static final int METHOD_INDEX = 0;
    private static final int URI_INDEX = 1;

    public static HttpRequestMessage parse(InputStream in) throws IOException, IllegalArgumentException, IndexOutOfBoundsException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String requestLine = br.readLine();
        String[] split = requestLine.split(SP);

        HttpMethod httpMethod = HttpMethod.valueOf(split[METHOD_INDEX]);
        String path = split[URI_INDEX];
        HttpHeader httpHeader = parseHttpHeader(br);

        return new HttpRequestMessage(httpMethod, URI.create(path), httpHeader);
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
}
