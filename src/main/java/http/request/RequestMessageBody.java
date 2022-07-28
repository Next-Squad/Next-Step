package http.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import util.IOUtils;

public class RequestMessageBody {

	private final String messageBody;

	public RequestMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

	public static RequestMessageBody from(BufferedReader bufferedReader, int contentLength)
		throws IOException {
		String messageBody = URLDecoder.decode(IOUtils.readData(bufferedReader, contentLength), StandardCharsets.UTF_8);
		return new RequestMessageBody(messageBody);
	}

	public String getMessageBody() {
		return messageBody;
	}
}
