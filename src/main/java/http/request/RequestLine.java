package http.request;

public class RequestLine {

	private HttpMethod httpMethod;
	private RequestURI requestUri;
	private HttpVersion httpVersion;

	public RequestLine(HttpMethod httpMethod, RequestURI requestUri, HttpVersion httpVersion) {
		this.httpMethod = httpMethod;
		this.requestUri = requestUri;
		this.httpVersion = httpVersion;
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
}
