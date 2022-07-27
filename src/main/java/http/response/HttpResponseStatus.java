package http.response;

public enum HttpResponseStatus {
	OK(200, "OK"),
	CREATED(201, "Created"),
	FOUND(302, "Found"),
	NOT_FOUND(404, "Not Found");

	final int code;
	final String message;

	HttpResponseStatus(int code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public String toString() {
		return this.code + " " + this.message;
	}
}
