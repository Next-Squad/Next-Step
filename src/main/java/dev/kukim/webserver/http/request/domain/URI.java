package dev.kukim.webserver.http.request.domain;

public class URI {

	public static final String SEP_URI_AND_QUERYSTRING = "\\?";

	private final String path;
	private final QueryParameters queryParameters;

	/**
	 * @param /user/create?userId=kukim&password=1234
	 */
	public URI(String rawUri) {
		String[] tokens = rawUri.split(SEP_URI_AND_QUERYSTRING);
		path = tokens[0];
		if (tokens.length != 1) {
			queryParameters = new QueryParameters(tokens[1]);
		} else {
			queryParameters = new QueryParameters("");
		}
	}

	public String getPath() {
		return path;
	}

	public String findQueryParameterBy(String key) {
		return queryParameters.get(key);
	}
}
