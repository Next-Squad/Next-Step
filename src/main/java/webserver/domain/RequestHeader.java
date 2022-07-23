package webserver.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestHeader {

	private RequestLine requestLine;
	private List<String> requestHeaders;
	private String body;

	private RequestHeader(){
	}

	public RequestHeader (String startLine, List<String> requestData) {
		this();
		this.requestLine = makeRequestLine(startLine);
	}

	private RequestLine makeRequestLine(String startLine) {
		String[] requestLine = startLine.split(" ");
		return new RequestLine(requestLine[0], requestLine[1], requestLine[2]);
	}
}
