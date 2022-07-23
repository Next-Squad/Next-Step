package webserver.was;

import util.FileUtils;

public class ViewResolver {

    private final String webAppPath;

    public ViewResolver(String webAppPath) {
        this.webAppPath = webAppPath;
    }

    public byte[] resolveView(String viewName) {
        return FileUtils.readFile(webAppPath + viewName);
    }
}
