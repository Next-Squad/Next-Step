package http.request;

public class RequestLine {

	private final HttpMethod httpMethod;
	private final RequestURI requestUri;
	private final HttpVersion httpVersion;

	public RequestLine(HttpMethod httpMethod, RequestURI requestUri, HttpVersion httpVersion) {
		this.httpMethod = httpMethod;
		this.requestUri = requestUri;
		this.httpVersion = httpVersion;
	}

	public static RequestLine from(String[] tokens) {
		HttpMethod httpMethod = HttpMethod.valueOf(tokens[0]);
		RequestURI requestURI = RequestURI.from(tokens[1]);
		HttpVersion httpVersion = new HttpVersion(tokens[2]);
		return new RequestLine(httpMethod, requestURI, httpVersion);
	}

	public HttpMethod getHttpMethod() {
		return httpMethod;
	}

	public RequestURI getRequestUri() {
		return requestUri;
	}

	public HttpVersion getHttpVersion() {
		return httpVersion;
	}

	public String toString() {
		return httpMethod.toString() + " "
			+ requestUri.toString() + " "
			+ httpVersion.getVersion();
	}
}
