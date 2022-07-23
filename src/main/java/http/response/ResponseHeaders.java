package http.response;

import java.util.HashMap;
import java.util.Map;

public class ResponseHeaders {

	private final Map<String, String> headers = new HashMap<>();

	public void addHeader(String key, String value) {
		headers.put(key, value);
	}

	public String getHeader(String key) {
		return headers.get(key);
	}

	public void setContentType(String contentType) {
		headers.put("Content-Type", contentType);
	}

	public void setContentLength(int lengthOfBodyContent) {
		headers.put("Content-Length", String.valueOf(lengthOfBodyContent));
	}

	public void setLocation(String redirectURI) {
		headers.put("Location", redirectURI);
	}

	public void setCookie(boolean cookie) {
		headers.put("Set-Cookie", "logined=" + cookie + "; path=/;");
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		headers.forEach((k, v)-> sb.append(k).append(": ").append(v).append("\r\n"));
		return sb.toString();
	}

}
