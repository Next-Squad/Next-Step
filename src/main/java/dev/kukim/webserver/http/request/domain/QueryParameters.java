package dev.kukim.webserver.http.request.domain;

import dev.kukim.util.HttpRequestUtils;
import java.util.HashMap;
import java.util.Map;

public class QueryParameters {

	private final Map<String, String> parameters;

	public QueryParameters(String rawQueryString) {
		if ("".equals(rawQueryString)) {
			parameters = new HashMap<>();
		} else {
			parameters = HttpRequestUtils.parseQueryString(rawQueryString);
		}
	}

	public String get(String key) {
		return parameters.get(key);
	}
}
