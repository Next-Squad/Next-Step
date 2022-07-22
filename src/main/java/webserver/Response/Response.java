package webserver.Response;

import webserver.Headers;

import java.nio.charset.StandardCharsets;

public class Response {

    private String protocol;
    private HttpStatus httpStatus;
    private Headers headers;
    private byte[] body;

    private Response(String protocol, HttpStatus httpStatus, Headers headers, byte[] body) {
        this.protocol = protocol;
        this.httpStatus = httpStatus;
        this.headers = headers;
        this.body = body;
    }

    public static Response of(String protocol) {
        return new Response(protocol, HttpStatus.OK, Headers.emptyHeaders(), "".getBytes(StandardCharsets.UTF_8));
    }

    public void setResponseBody(byte[] body) {
        this.body = body;
        headers.setContentLength(String.valueOf(body.length));
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }




}
