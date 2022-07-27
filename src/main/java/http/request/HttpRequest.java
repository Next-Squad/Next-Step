package http.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;
import util.IOUtils;

public class HttpRequest {

	private final RequestLine requestLine;
	private final RequestHeaders requestHeaders;
	private RequestMessageBody requestMessageBody;

	private HttpRequest(RequestLine requestLine, RequestHeaders requestHeaders) {
		this.requestLine = requestLine;
		this.requestHeaders = requestHeaders;
	}

	private HttpRequest(RequestLine requestLine, RequestHeaders requestHeaders, RequestMessageBody requestMessageBody) {
		this(requestLine, requestHeaders);
		this.requestMessageBody = requestMessageBody;
	}

	public static HttpRequest from(InputStream in) throws IOException {
		InputStreamReader inputStreamReader = new InputStreamReader(in, StandardCharsets.UTF_8); // 왜 안먹힐까?
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		String line = URLDecoder.decode(bufferedReader.readLine(), StandardCharsets.UTF_8); // 왜 한 번 더 해줘야 먹힐까? - 위에서 안먹히는 걸까?

		RequestLine requestLine = HttpRequestUtils.parseRequestLine(line);
		RequestHeaders requestHeaders = new RequestHeaders();
		while (!line.equals((""))) {
			line = URLDecoder.decode(bufferedReader.readLine(), StandardCharsets.UTF_8);
			Pair pair = HttpRequestUtils.parseHeader(line);
			requestHeaders.addHeader(pair);
		}
		if (requestHeaders.containsKey("Content-Length")) {
			int contentLength = Integer.parseInt(requestHeaders.getHeader("Content-Length"));
			String messageBody = URLDecoder.decode(
				IOUtils.readData(bufferedReader, contentLength),
				StandardCharsets.UTF_8
			);
			RequestMessageBody requestMessageBody = new RequestMessageBody(messageBody);
			return new HttpRequest(requestLine, requestHeaders, requestMessageBody);
		}
		return new HttpRequest(requestLine, requestHeaders);
	}

	public RequestLine getRequestLine() {
		return requestLine;
	}

	public RequestHeaders getRequestHeaders() {
		return requestHeaders;
	}

	public RequestMessageBody getRequestMessageBody() {
		return requestMessageBody;
	}
}
