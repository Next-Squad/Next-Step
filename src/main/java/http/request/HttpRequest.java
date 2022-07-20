package http.request;

public class HttpRequest {

	private final RequestLine requestLine;
	private final RequestHeaders requestHeaders;
	private RequestMessageBody requestMessageBody;

	public HttpRequest(RequestLine requestLine, RequestHeaders requestHeaders) {
		this.requestLine = requestLine;
		this.requestHeaders = requestHeaders;
	}

	public HttpRequest(RequestLine requestLine, RequestHeaders requestHeaders, RequestMessageBody requestMessageBody) {
		this(requestLine, requestHeaders);
		this.requestMessageBody = requestMessageBody;
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
