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
		this.requestLine = new RequestLine(requestLine[0], requestLine[1], requestLine[2]);
	}
}
