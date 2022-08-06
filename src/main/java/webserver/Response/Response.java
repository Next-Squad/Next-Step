package webserver.Response;

import webserver.ContentType;
import webserver.Header;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Response {

    private static final String NEW_LINE = "\r\n";

    private String protocol;
    private HttpStatus httpStatus;
    private Header headers;
    private byte[] body;

    private Response(String protocol, HttpStatus httpStatus, Header headers, byte[] body) {
        this.protocol = protocol;
        this.httpStatus = httpStatus;
        this.headers = headers;
        this.body = body;
    }

    public static Response of(String protocol) {
        return new Response(protocol, HttpStatus.OK, Header.emptyHeaders(), "".getBytes(StandardCharsets.UTF_8));
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public void setResponseBody(byte[] body, ContentType contentType) {
        this.body = body;
        headers.setContentType(contentType);
        headers.setContentLength(String.valueOf(body.length));
    }

    public void redirect(String location) {
        this.httpStatus = HttpStatus.REDIRECT;
        headers.setLocation(location);
    }

    public void writeResponse(DataOutputStream dataOutputStream) {
        try {
            dataOutputStream.writeBytes(responseHeaderToString());
            dataOutputStream.write(this.body, 0, body.length);
            dataOutputStream.flush();
        } catch (IOException e) {
           e.printStackTrace();
        }
    }

    public String responseHeaderToString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(protocol).append(" ").append(httpStatus.toString()).append(NEW_LINE);
        stringBuilder.append(headers.getStringHeaders()).append(NEW_LINE);

        return stringBuilder.toString();
    }

    public void setSession(String sessionId) {
        headers.setHeader("Set-Cookie", "login=" + sessionId);
    }
}
