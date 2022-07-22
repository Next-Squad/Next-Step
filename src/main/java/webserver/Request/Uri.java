package webserver.Request;

import webserver.ContentType;

public class Uri {

    private final String path;
    private final String query;

    private Uri(String path, String query) {
        this.path = path;
        this.query = query;
    }

    public static Uri simplePath(String path) {
        return new Uri(path, "");
    }

    public static Uri of(String path, String query) {
        return new Uri(path, query);
    }

    public String getPath() {
        return path;
    }

    public String getQuery() {
        return query;
    }

    public boolean isResourcePath() {
        for (ContentType value : ContentType.values()) {
            if (this.path.contains(value.getExtension())) {
                return true;
            }
        }
        return false;
    }
}
