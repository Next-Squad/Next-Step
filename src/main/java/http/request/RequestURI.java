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

	public String getPath() {
		return path;
	}

	public String getQueryString() {
		return queryString;
	}
}
