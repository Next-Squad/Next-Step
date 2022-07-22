package dev.kukim.webserver.http.request;

import dev.kukim.util.IOUtils;
import dev.kukim.webserver.http.request.domain.HttpMethod;
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

	private RequestBody parseRequestBody(BufferedReader bufferedReader) throws IOException {
		if (requestHeaders.hasContentLength()) {
			String rawRequestBody = IOUtils.readData(bufferedReader, requestHeaders.getContentLength());
			return new RequestBody(requestHeaders.getContentType(), rawRequestBody);
		}
		return null;
	}

	public String getPath() {
		return requestLine.getPath();
	}

	public HttpMethod getMethod() {
		return requestLine.getMethod();
	}

	public String getQueryParameter(String key) {
		return requestLine.getQueryParameter(key);
	}

	public String getRequestHeader(String key) {
		return requestHeaders.get(key);
	}

	public String getBodyQueryParameter(String key) {
		return requestBody.get(key);
	}
}
