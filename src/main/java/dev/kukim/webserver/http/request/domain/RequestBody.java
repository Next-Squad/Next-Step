package dev.kukim.webserver.http.request.domain;

public class RequestBody {

	public static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
	private QueryParameters parameters;

	public RequestBody(String contentType, String requestBody) {
		if (APPLICATION_X_WWW_FORM_URLENCODED.equals(contentType)) {
			parameters = new QueryParameters(requestBody);
		}
	}

	public String get(String key) {
		return parameters.get(key);
	}
}
