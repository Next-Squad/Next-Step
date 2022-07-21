package webserver;

import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public record HttpRequestMessage(HttpMethod method, String path, HttpHeader header) {
    private static final String SP = " ";
    private static final int METHOD_INDEX = 0;
    private static final int PATH_INDEX = 1;

    public static HttpRequestMessage parse(InputStream in) throws IOException, IllegalArgumentException, IndexOutOfBoundsException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String requestLine = br.readLine();
        String[] split = requestLine.split(SP);

        HttpMethod httpMethod = HttpMethod.valueOf(split[METHOD_INDEX]);
        String path = split[PATH_INDEX];
        HttpHeader httpHeader = parseHttpHeader(br);

        return new HttpRequestMessage(httpMethod, path, httpHeader);
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
