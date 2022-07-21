package dev.kukim.webserver.http.response;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ResponseHeaders {

	private final Map<String, String> headers = new HashMap<>();

	public void add(String key, String value) {
		headers.put(key, value);
	}

	@Override
	public String toString() {
		if (headers.isEmpty()) return "\r\n";

		StringBuilder sb = new StringBuilder();
		for (Entry<String, String> header : headers.entrySet()) {
			sb.append(header.getKey())
				.append(": ")
				.append(header.getValue())
				.append("\r\n");
		}
		sb.append("\r\n");
		return sb.toString();
	}
}
