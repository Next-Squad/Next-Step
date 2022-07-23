package webserver.http;

import java.util.Arrays;

public class HttpResponse {

    public static final HttpResponse NOT_FOUNT_RESPONSE = new HttpResponse(HttpStatus.NOT_FOUND, new HttpHeader());

    private final HttpStatus status;
    private final HttpHeader header;
    private final byte[] body;

    public HttpResponse(HttpStatus status, HttpHeader header) {
        this(status, header, new byte[]{});
    }

    public HttpResponse(HttpStatus status, HttpHeader header, byte[] body) {
        this.status = status;
        this.header = header.clone();
        this.body = body;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public HttpHeader getHeader() {
        return header.clone();
    }

    public byte[] getBody() {
        return Arrays.copyOf(body, body.length);
    }
}
