package webserver.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RequestLine {

	private HttpMethod httpMethod;
	private String url;
	private String httpVersion;

	public RequestLine(String httpMethod, String url, String httpVersion) {
		this.httpMethod = HttpMethod.valueOf(httpMethod);
		this.url = url;
		this.httpVersion = httpVersion;
	}

}
