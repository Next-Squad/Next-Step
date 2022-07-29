package http.response;

import java.nio.charset.StandardCharsets;

public class ResponseMessageBody {

	private final byte[] messageBody;

	public ResponseMessageBody(byte[] messageBody) {
		this.messageBody = messageBody;
	}

	public byte[] getMessageBody() {
		return messageBody;
	}

	@Override
	public String toString() {
		return new String(messageBody, StandardCharsets.UTF_8);
	}
}
