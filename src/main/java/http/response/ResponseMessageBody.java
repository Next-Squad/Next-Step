package http.response;

public class ResponseMessageBody {

	private final byte[] messageBody;

	public ResponseMessageBody(byte[] messageBody) {
		this.messageBody = messageBody;
	}

	public byte[] getMessageBody() {
		return messageBody;
	}
}
