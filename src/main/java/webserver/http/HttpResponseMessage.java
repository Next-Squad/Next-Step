package webserver.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class HttpResponseMessage {

	private final OutputStream out;
	private final HttpResponse httpResponse;

	public HttpResponseMessage(OutputStream out, HttpResponse httpResponse) {
		this.out = out;
		this.httpResponse = httpResponse;
	}

	public void flush() {
		try (DataOutputStream dos = new DataOutputStream(out)) {
			writeResponseLine(dos, httpResponse.getStatus());
			writeResponseHeader(dos, httpResponse.getHeader());
			dos.writeBytes("\r\n");
			writeResponseBody(dos, httpResponse.getBody());

			dos.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void writeResponseLine(DataOutputStream dos, HttpStatus status) throws IOException {
		dos.writeBytes(String.format("HTTP/1.1 %d %s\r\n", status.getCode(), status.getStatus()));
	}

	private void writeResponseHeader(DataOutputStream dos, HttpHeader header) throws IOException {
		for (String key : header.keySet()) {
			dos.writeBytes(String.format("%s: %s\r\n", key, header.get(key)));
		}
	}

	private static void writeResponseBody(DataOutputStream dos, byte[] body) throws IOException {
		dos.write(body, 0, body.length);
	}
}
