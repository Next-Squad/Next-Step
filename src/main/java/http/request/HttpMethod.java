package http.request;

public enum HttpMethod {
	GET, POST, PATCH, DELETE;

	public boolean isGET() {
		return this == GET;
	}

	public boolean isPOST() {
		return this == POST;
	}
}
