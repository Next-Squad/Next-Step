package http.response;

import http.HttpVersion;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class HttpResponse {

	private final StatusLine statusLine;
	private final ResponseHeaders responseHeaders;
	private ResponseMessageBody responseMessageBody;

	public HttpResponse(StatusLine statusLine, ResponseHeaders responseHeaders) {
		this.statusLine = statusLine;
		this.responseHeaders = responseHeaders;
	}

	public HttpResponse(StatusLine statusLine, ResponseHeaders responseHeaders, ResponseMessageBody responseMessageBody) {
		this(statusLine, responseHeaders);
		this.responseMessageBody = responseMessageBody;
	}

	public static HttpResponse ok() {
		StatusLine statusLine = new StatusLine(new HttpVersion("HTTP/1.1"), HttpResponseStatus.OK);
		byte[] messageBody = "Hello World".getBytes(StandardCharsets.UTF_8);
		ResponseHeaders responseHeaders = new ResponseHeaders();
		responseHeaders.setContentType("text/plain;charset=utf-8");
		responseHeaders.setContentLength(messageBody.length);
		ResponseMessageBody responseMessageBody = new ResponseMessageBody(messageBody);
		return new HttpResponse(statusLine, responseHeaders, responseMessageBody);
	}

	public static HttpResponse ok(String viewName) throws IOException {
		byte[] messageBody = Files.readAllBytes(new File("./webapp" + viewName).toPath());
		StatusLine statusLine = new StatusLine(new HttpVersion("HTTP/1.1"), HttpResponseStatus.OK);
		ResponseHeaders responseHeaders = new ResponseHeaders();
		responseHeaders.setContentType("text/html;charset=utf-8");
		responseHeaders.setContentLength(messageBody.length);
		ResponseMessageBody responseMessageBody = new ResponseMessageBody(messageBody);
		return new HttpResponse(statusLine, responseHeaders, responseMessageBody);
	}

	public static HttpResponse found(String redirectURI) {
		StatusLine statusLine = new StatusLine(new HttpVersion("HTTP/1.1"), HttpResponseStatus.FOUND);
		ResponseHeaders responseHeaders = new ResponseHeaders();
		responseHeaders.setContentType("text/html;charset=utf-8");
		responseHeaders.setLocation(redirectURI);
		return new HttpResponse(statusLine, responseHeaders);
	}

	public void flush(OutputStream out) throws IOException {
		DataOutputStream dos = new DataOutputStream(out);
		dos.writeBytes(statusLine.toString() + " \r\n");
		dos.writeBytes(responseHeaders.toString());
		dos.writeBytes("\r\n");
		if (responseMessageBody != null) {
			byte[] messageBody = responseMessageBody.getMessageBody();
			dos.write(messageBody, 0, messageBody.length);
		}
		dos.flush();
	}
}
