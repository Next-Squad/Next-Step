package webserver.http;

import java.util.Arrays;

public enum ContentType {
    TEXT_PLAIN("text/plain", "txt"),
    TEXT_HTML("text/html", "html"),
    TEXT_CSS("text/css", "css"),
    TEXT_JAVASCRIPT("text/javascript", "js"),
    APPLICATION_X_FONT_TTF("application/x-font-ttf", "ttf"),
    APPLICATION_X_FONT_WOFF("application/x-font-woff", "woff"),
    IMAGE_X_ICON("text/x-icon", "ico");

    private static final ContentType DEFAULT = TEXT_PLAIN;

    private final String header;
    private final String extension;

    ContentType(String header, String extension) {
        this.header = header;
        this.extension = extension;
    }

    public String getHeader() {
        return header;
    }

    public String getExtension() {
        return extension;
    }

    public static boolean isExistsByExtension(String extension) {
        return Arrays.stream(values())
                .anyMatch(type -> type.extension.equals(extension));
    }

    public static ContentType findByExtension(String extension) {
        return Arrays.stream(values())
                .filter(type -> type.extension.equals(extension))
                .findAny()
                .orElse(DEFAULT);
    }
}
