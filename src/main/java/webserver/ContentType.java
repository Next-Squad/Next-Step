package webserver;


public enum ContentType {

    HTML("text/html", ".html"),
    CSS("text/css", ".css"),
    JS("application/javascript", ".js"),
    FAVICON("image/x-icon",".ico"),
    WOFF("application/font-woff", ".woff"),
    TTF("application/x-font-truetype", ".ttf"),
    PNG("image/png", ".png");

    private final String type;
    private final String extension;

    ContentType(String type, String extension) {
        this.type = type;
        this.extension = extension;
    }

    public static ContentType getContentType(String contentName) {
        if (contentName.endsWith(HTML.getExtension())) {
            return HTML;
        }

        if (contentName.endsWith(JS.getExtension())) {
            return JS;
        }

        if (contentName.endsWith(CSS.getExtension())) {
            return CSS;
        }

        if (contentName.endsWith(WOFF.getExtension())) {
            return WOFF;
        }

        if (contentName.endsWith(TTF.getExtension())) {
            return TTF;
        }

        if (contentName.endsWith(PNG.getExtension())) {
            return PNG;
        }
        return FAVICON;
    }

    public String getType() {
        return type;
    }

    public String getExtension() {
        return extension;
    }


}
