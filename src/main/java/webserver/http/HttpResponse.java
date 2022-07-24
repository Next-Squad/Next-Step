package webserver.http;

import util.HttpRequestUtils;

import java.util.Arrays;

public class HttpResponse {

    private final HttpStatus status;
    private final HttpHeader header;
    private final HttpResponseModel model;
    private final byte[] body;

    private String viewName;

    public HttpResponse(HttpStatus status, HttpHeader header) {
        this(status, header, new HttpResponseModel(), new byte[]{});
    }

    public HttpResponse(HttpStatus status, HttpHeader header, byte[] body) {
        this(status, header, new HttpResponseModel(), body);
    }

    public HttpResponse(HttpStatus status, HttpHeader header, HttpResponseModel model, byte[] body) {
        this.status = status;
        this.header = header.clone();
        this.model = model.clone();
        this.body = body;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public HttpHeader getHeader() {
        return header.clone();
    }

    public HttpResponseModel getModel() {
        return model.clone();
    }

    public byte[] getBody() {
        return Arrays.copyOf(body, body.length);
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;

        String extension = HttpRequestUtils.parseExtension(viewName);

        if (extension != null) {
            ContentType contentType = ContentType.findByExtension(extension);
            header.add("Content-type", contentType.getHeader());
        }
    }

    public boolean hasViewName() {
        return this.viewName != null && !this.viewName.isEmpty() && !this.viewName.isBlank();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private HttpStatus status;
        private final HttpHeader header = new HttpHeader();
        private final HttpResponseModel model = new HttpResponseModel();
        private byte[] body = new byte[] {};
        private String viewName;

        public Builder setStatus(HttpStatus status) {
            this.status = status;
            return this;
        }

        public Builder addHeader(String key, String value) {
            this.header.add(key, value);
            return this;
        }

        public Builder putModelAttribute(String key, Object value) {
            this.model.put(key, value);
            return this;
        }

        public Builder setBody(byte[] body) {
            this.body = body;
            return this;
        }

        public Builder setViewName(String viewName) {
            this.viewName = viewName;
            return this;
        }

        public HttpResponse build() {
            HttpResponse response = new HttpResponse(status, header, body);

            if (model.keySet().size() > 0) {
                response = new HttpResponse(status, header, model, body);
            }

            response.setViewName(viewName);
            return response;
        }
    }
}
