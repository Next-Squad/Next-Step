package dev.kukim.webserver.http.request;

import dev.kukim.webserver.http.request.domain.RequestHeaders;
import dev.kukim.webserver.http.request.domain.RequestLine;
import dev.kukim.webserver.http.request.domain.RequestBody;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HttpRequest {

	private final RequestLine requestLine;
	private final RequestHeaders requestHeaders;
	private final RequestBody requestBody;

	public HttpRequest(InputStream inputStream) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
			new InputStreamReader(inputStream, StandardCharsets.UTF_8));

		requestLine = parseRequestLine(bufferedReader);
		requestHeaders = parseRequestHeaders(bufferedReader);
		requestBody = parseRequestBody(bufferedReader);
	}

	public RequestLine getRequestLine() {
		return requestLine;
	}

	public RequestHeaders getRequestHeaders() {
		return requestHeaders;
	}

	public RequestBody getRequestBody() {
		return requestBody;
	}

	private RequestLine parseRequestLine(BufferedReader bufferedReader) throws IOException {
		return new RequestLine(bufferedReader.readLine());
	}

	private RequestHeaders parseRequestHeaders(BufferedReader bufferedReader) throws IOException {
		List<String> headers = new ArrayList<>();

		String header = bufferedReader.readLine();
		while (!"".equals(header)) {
			headers.add(header);
			header = bufferedReader.readLine();
		}

		return new RequestHeaders(headers);
	}

	private RequestBody parseRequestBody(BufferedReader bufferedReader) {
		return new RequestBody();
	}
}
