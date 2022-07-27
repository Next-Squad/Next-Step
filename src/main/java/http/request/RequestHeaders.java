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
		if (line == null) {
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

	public boolean containsKey(String key) {
		return headers.containsKey(key);
	}

	public String getContentLength() {
		return headers.get(CONTENT_LENGTH);
	}

	public String getCookie() {
		if (headers.containsKey(COOKIE)) {
			return headers.get(COOKIE);
		}
		return null;
	}
}
