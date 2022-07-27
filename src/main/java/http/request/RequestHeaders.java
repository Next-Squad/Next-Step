package http.request;

import java.util.HashMap;
import java.util.Map;
import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;

public class RequestHeaders {

	private static final String CONTENT_LENGTH = "Content-Length";
	private static final String COOKIE = "Cookie";

	private final Map<String, String> headers = new HashMap<>();

	public void addHeader(String line) {
		if (line.isEmpty() || line.isBlank()) {
			return;
		}
		Pair pair = HttpRequestUtils.parseHeader(line);
		headers.put(pair.getKey(), pair.getValue());
	}

	public String getHeader(String key) {
		return headers.get(key);
	}

	public boolean hasContentLength() {
		return headers.containsKey(CONTENT_LENGTH);
	}

	public int getContentLength() {
		return Integer.parseInt(headers.get(CONTENT_LENGTH));
	}

	public boolean hasCookie() {
		return headers.containsKey(COOKIE);
	}

	public String getCookie() {
		return headers.get(COOKIE);
	}
}
