package dev.kukim.webserver.http.request.domain;

import dev.kukim.util.HttpRequestUtils;
import dev.kukim.util.HttpRequestUtils.Pair;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RequestHeaders {

	private final Map<String, String> headers;

	public RequestHeaders(List<String> headers) {
		this.headers = headers.stream()
			.map(HttpRequestUtils::parseHeader)
			.collect(Collectors.toMap(
				Pair::getKey,
				Pair::getValue
			));
	}


	public String get(String key) {
		return headers.get(key);
	}
}
