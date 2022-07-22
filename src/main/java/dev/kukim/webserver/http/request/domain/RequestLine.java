package dev.kukim.webserver.http.request.domain;


public class RequestLine {

	public static final String SEP_URI_AND_QUERYSTRING = "\\?";
	private final HttpMethod method;
	private final URI uri;
	private final String version;

	/**
	 * @param rawRequestLine e.g. GET /user/create?userId=kukim&password=1234 HTTP/1.1
	 */
	public RequestLine(String rawRequestLine) {
		String[] tokens = rawRequestLine.split(" ");
		method = HttpMethod.valueOf(tokens[0]);
		uri = new URI(tokens[1]);
		version = tokens[2];
	}

	public HttpMethod getMethod() {
		return method;
	}

	public String getPath() {
		return uri.getPath();
	}

	public String getQueryParameter(String key) {
		return uri.findQueryParameterBy(key);
	}

	public String getVersion() {
		return version;
	}
}
