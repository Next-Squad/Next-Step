package http.response;

import http.HttpVersion;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import model.User;

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

	public StatusLine getStatusLine() {
		return statusLine;
	}

	public ResponseHeaders getResponseHeaders() {
		return responseHeaders;
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
		String[] urlTokens = viewName.split("\\.");
		String extension = urlTokens[urlTokens.length - 1];

		ResponseHeaders responseHeaders = new ResponseHeaders();
		responseHeaders.setContentType("text/"+extension+";charset=utf-8");
		responseHeaders.setAccept("text/"+extension+", */*; q=0.1");
		responseHeaders.setContentLength(messageBody.length);
		ResponseMessageBody responseMessageBody = new ResponseMessageBody(messageBody);
		return new HttpResponse(statusLine, responseHeaders, responseMessageBody);
	}

	public static HttpResponse ok(String viewName, List<User> users) throws IOException {
		StringBuilder sb = new StringBuilder();
		List<String> fileLines = Files.readAllLines(new File("./webapp" + viewName).toPath());
		for (String fileLine : fileLines) {
			if (fileLine.equals("<!-- user list -->")) {
				int userCount = 0;
				sb.append("              <tbody>\r\n");
				for (User user : users) {
					userCount++;
					sb.append("                <tr>\r\n");
					sb.append("                    <th scope=\"row\">").append(userCount).append("</th>");
					sb.append(" <td>").append(user.getUserId()).append("</td>");
					sb.append(" <td>").append(user.getName()).append("</td>");
					sb.append(" <td>").append(user.getEmail()).append("</td>");
					sb.append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\r\n");
					sb.append("                </tr>\r\n");
				}
				sb.append("              </tbody>\r\n");
			}
			sb.append(fileLine).append("\r\n");
		}
		byte[] messageBody = sb.toString().getBytes(StandardCharsets.UTF_8);
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
		responseHeaders.setCookie(false);
		return new HttpResponse(statusLine, responseHeaders);
	}

	public static HttpResponse found(String redirectURI, boolean cookie) {
		StatusLine statusLine = new StatusLine(new HttpVersion("HTTP/1.1"), HttpResponseStatus.FOUND);
		ResponseHeaders responseHeaders = new ResponseHeaders();
		responseHeaders.setContentType("text/html;charset=utf-8");
		responseHeaders.setLocation(redirectURI);
		responseHeaders.setCookie(cookie);
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
