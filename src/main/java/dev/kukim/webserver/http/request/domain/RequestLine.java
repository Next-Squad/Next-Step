package dev.kukim.webserver.http.request.domain;

public class RequestLine {


	public static final String SEP_URI_AND_QUERYSTRING = "\\?";
	private final HttpMethod method;
	private final String uri;
	private final QueryParameters queryParameters;
	private final String version;


	/**
	 * @param rawRequestLine e.g. GET /user/create?userId=kukim&password=1234 HTTP/1.1
	 */
	public RequestLine(String rawRequestLine) {
		String[] tokens = rawRequestLine.split(" ");
		method = HttpMethod.valueOf(tokens[0]);

		String[] uriAndQueryString = tokens[1].split(SEP_URI_AND_QUERYSTRING);
		uri = uriAndQueryString[0];
		queryParameters = new QueryParameters(uriAndQueryString[1]);

		version = tokens[2];
	}

	public HttpMethod getMethod() {
		return method;
	}

	public String getUri() {
		return uri;
	}

	public String getQueryParameter(String key) {
		return queryParameters.get(key);
	}

	public String getVersion() {
		return version;
	}
}
