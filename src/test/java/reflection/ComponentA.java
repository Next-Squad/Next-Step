package reflection;

import handler.Component;
import webserver.Request.HttpMethod;

@Component
public class ComponentA {

    @MyAnnotation(httpMethod = HttpMethod.GET, path = "/a")
    public String methodA() {
        return "method A invoked";
    }
}
