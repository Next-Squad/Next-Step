package http.response;

import http.HttpVersion;

public class StatusLine {

	private final HttpVersion httpVersion;
	private final HttpResponseStatus status;

	public StatusLine(HttpVersion httpVersion, HttpResponseStatus status) {
		this.httpVersion = httpVersion;
		this.status = status;
	}

	@Override
	public String toString() {
		return httpVersion.getVersion() + " " + status.toString();
	}
}
