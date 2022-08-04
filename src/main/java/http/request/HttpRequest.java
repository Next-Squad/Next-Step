package http.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

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
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
		String line = URLDecoder.decode(bufferedReader.readLine(), StandardCharsets.UTF_8);
		RequestLine requestLine = RequestLine.from(line);
		RequestHeaders requestHeaders = new RequestHeaders();
		while (!line.equals((""))) {
			line = URLDecoder.decode(bufferedReader.readLine(), StandardCharsets.UTF_8);
			requestHeaders.addHeader(line);
		}
		if (requestHeaders.hasContentLength()) {
			RequestMessageBody requestMessageBody = RequestMessageBody
				.from(bufferedReader, requestHeaders.getContentLength());
			return new HttpRequest(requestLine, requestHeaders, requestMessageBody);
		}
		return new HttpRequest(requestLine, requestHeaders);
	}

	public RequestLine getRequestLine() {
		return requestLine;
	}

	public String getRequestURIPath() {
		return requestLine.getPath();
	}

	public RequestHeaders getRequestHeaders() {
		return requestHeaders;
	}

	public String getMessageBody() {
		return requestMessageBody.getMessageBody();
	}
}
