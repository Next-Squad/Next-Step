package http.response;

import http.Cookie;
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

	private final DataOutputStream dos;
	private StatusLine statusLine;
	private final ResponseHeaders responseHeaders = new ResponseHeaders();
	private ResponseMessageBody responseMessageBody;

	public HttpResponse(OutputStream out) {
		this.dos = new DataOutputStream(out);
	}

	public StatusLine getStatusLine() {
		return statusLine;
	}

	public ResponseHeaders getResponseHeaders() {
		return responseHeaders;
	}

	public ResponseMessageBody getResponseMessageBody() {
		return responseMessageBody;
	}

	public void ok() throws IOException {
		statusLine = new StatusLine(new HttpVersion("HTTP/1.1"), HttpResponseStatus.OK);
		byte[] messageBody = "Hello World".getBytes(StandardCharsets.UTF_8);
		responseHeaders.setContentType("text/plain;charset=utf-8");
		responseHeaders.setContentLength(messageBody.length);
		responseMessageBody = new ResponseMessageBody(messageBody);
		flush();
	}

	public void ok(String viewName) throws IOException {
		byte[] messageBody = Files.readAllBytes(new File("./webapp" + viewName).toPath());
		statusLine = new StatusLine(new HttpVersion("HTTP/1.1"), HttpResponseStatus.OK);
		String[] urlTokens = viewName.split("\\.");
		String extension = urlTokens[urlTokens.length - 1];

		responseHeaders.setContentType("text/"+extension+";charset=utf-8");
		responseHeaders.setAccept("text/"+extension+", */*; q=0.1");
		responseHeaders.setContentLength(messageBody.length);
		responseMessageBody = new ResponseMessageBody(messageBody);
		flush();
	}

	public void ok(String viewName, List<User> users) throws IOException {
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
		statusLine = new StatusLine(new HttpVersion("HTTP/1.1"), HttpResponseStatus.OK);
		responseHeaders.setContentType("text/html;charset=utf-8");
		responseHeaders.setContentLength(messageBody.length);
		responseMessageBody = new ResponseMessageBody(messageBody);
		flush();
	}

	public void found(String redirectURI) throws IOException {
		statusLine = new StatusLine(new HttpVersion("HTTP/1.1"), HttpResponseStatus.FOUND);
		responseHeaders.setContentType("text/html;charset=utf-8");
		responseHeaders.setLocation(redirectURI);
		responseHeaders.setCookie(new Cookie("logined", "false"));
		flush();
	}

	public void found(String redirectURI, Cookie cookie) throws IOException {
		statusLine = new StatusLine(new HttpVersion("HTTP/1.1"), HttpResponseStatus.FOUND);
		responseHeaders.setContentType("text/html;charset=utf-8");
		responseHeaders.setLocation(redirectURI);
		responseHeaders.setCookie(cookie);
		flush();
	}

	private void flush() throws IOException {
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
