package dev.kukim.webserver.http.request;

import dev.kukim.webserver.http.request.domain.RequestBody;
import dev.kukim.webserver.http.request.domain.RequestHeaders;
import dev.kukim.webserver.http.request.domain.RequestLine;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

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

	private RequestLine parseRequestLine(BufferedReader bufferedReader) {
		throw new UnsupportedOperationException("HttpRequest#parseRequestBody 아직 구현하지 않음 :)");
	}

	private RequestHeaders parseRequestHeaders(BufferedReader bufferedReader) {
		throw new UnsupportedOperationException("HttpRequest#parseRequestBody 아직 구현하지 않음 :)");
	}

	private RequestBody parseRequestBody(BufferedReader bufferedReader) {
		throw new UnsupportedOperationException("HttpRequest#parseRequestBody 아직 구현하지 않음 :)");

	}
}
