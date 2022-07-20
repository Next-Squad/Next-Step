package dev.kukim.webserver.http.request.domain;

import java.util.HashMap;
import java.util.Map;

public class QueryParameters {

	public static final String SEP_QUERYSTRINGS = "&";
	public static final String SEP_QUERYSTRING_KEY_AND_VALUE = "=";
	private final Map<String, String> parameters = new HashMap<>();

	public QueryParameters(String rawQueryString) {

		String[] queryStringArr = rawQueryString.split(SEP_QUERYSTRINGS);

		for (String queryString : queryStringArr) {
			String[] keyAndValue = queryString.split(SEP_QUERYSTRING_KEY_AND_VALUE);
			parameters.put(keyAndValue[0], keyAndValue[1]);
		}
	}

	public String get(String key) {
		return parameters.get(key);
	}
}
