package http.request;

public class RequestURI {

	private final String path;
	private String queryString;

	public RequestURI(String path) {
		this.path = path;
	}

	public RequestURI(String path, String queryString) {
		this(path);
		this.queryString = queryString;
	}

	public static RequestURI from(String requestUri) {
		String[] tokens = requestUri.split("\\?");
		if (tokens.length > 1) {
			return new RequestURI(tokens[0], tokens[1]);
		}
		return new RequestURI(tokens[0]);
	}

	public String getPath() {
		return path;
	}

	public String getQueryString() {
		return queryString;
	}

	@Override
	public String toString() {
		if (queryString != null) {
			return path + queryString;
		}
		return path;
	}
}
