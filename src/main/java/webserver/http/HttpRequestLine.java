package webserver.http;

import java.net.URI;
import java.util.Objects;

public class HttpRequestLine {

	private static final String SP = " ";
	private static final int METHOD_INDEX = 0;
	private static final int URI_INDEX = 1;

	private final HttpMethod method;
	private final URI uri;

	public HttpRequestLine(String requestLine) {
		Objects.requireNonNull(requestLine);

		String[] splittedRequestLine = requestLine.split(SP);

		this.method = HttpMethod.valueOf(splittedRequestLine[METHOD_INDEX]);
		this.uri = URI.create(splittedRequestLine[URI_INDEX]);
	}

	public HttpMethod getMethod() {
		return method;
	}

	public URI getUri() {
		return uri;
	}
}
