package webserver.domain;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestHeader {

	private RequestLine requestLine;
	private List<String> requestHeaders;
	private List<String> body;

	public void makeRequestLine(String startLine) {
		String[] requestLine = startLine.split(" ");
		String httpMethod = requestLine[0];
		String url = requestLine[1];
		String httpVersion = requestLine[2];

		this.requestLine = new RequestLine(httpMethod, url, httpVersion);
	}
}
