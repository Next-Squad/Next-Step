package webserver;


public enum ContentType {

    HTML("text/html", ".html"),
    CSS("text/css", ".css"),
    JS("application/javascript", ".js"),
    FAVICON("image/x-icon",".ico");

    private final String type;
    private final String extension;

    ContentType(String type, String extension) {
        this.type = type;
        this.extension = extension;
    }

    public ContentType getContentTypeHeader(String extension) {

        if (HTML.extension.equals(extension)) {
            return HTML;
        }

        if (CSS.extension.equals(extension)) {
            return CSS;
        }

        return JS;
    }

    public String getType() {
        return type;
    }

    public String getExtension() {
        return extension;
    }


}
