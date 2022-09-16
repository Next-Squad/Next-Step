package dev.kukim.webserver.http.response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class HttpResponse {

	private final ResponseHeaders headers = new ResponseHeaders();

	private byte[] body = null;

	private final DataOutputStream dos;
	public HttpResponse(OutputStream outputStream) {
		dos = new DataOutputStream(outputStream);
	}

	public void setBody(byte[] body) {
		this.body = body;
		headers.add("Content-Length", String.valueOf(body.length));
	}

	public void sendOK() throws IOException {
		dos.writeBytes("HTTP/1.1 200 OK \r\n");
		writeHeaders();
		writeBody();
		dos.flush();
	}

	public void sendRedirect(String location) throws IOException {
		dos.writeBytes("HTTP/1.1 302 Found \r\n");
		dos.writeBytes("Location: " + location + "\r\n");
		writeHeaders();
		dos.flush();
	}

	private void writeHeaders() throws IOException {
		dos.writeBytes(headers.toString());
	}

	private void writeBody() throws IOException {
		dos.write(body, 0, body.length);
	}

	public void setCookie(String cookie) {
		headers.add("Set-Cookie", cookie);
	}

	public void setContentType(String contentType) {
		headers.add("Content-Type", contentType);
	}

	public void responseMethodNotAllowed() throws IOException {
		dos.writeBytes("HTTP/1.1 405 Method Not Allowed \r\n");
		writeHeaders();
		dos.flush();

	}
}
