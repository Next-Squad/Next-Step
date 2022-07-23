package webserver;

import java.nio.charset.StandardCharsets;

public class Response {
    private StatusCode statusCode;
    private byte[] body;

    public Response(StatusCode statusCode, byte[] body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public byte[] getHeader() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("HTTP/1.1 %d %s \r\n", statusCode.getCodeNumber(), statusCode));
        sb.append("Content-Type: text/html;charset=utf-8\r\n");
        sb.append("Content-Length: " + body.length + "\r\n");
        sb.append("\r\n");
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    public byte[] getBody() {
        return body.clone();
    }
}
