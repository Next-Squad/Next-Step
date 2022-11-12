package reflection;

import dispatcher.HandlerMethod;
import handler.Component;
import webserver.Request.HttpMethod;


@Component
public class ComponentB {

    @MyAnnotation(httpMethod = HttpMethod.GET, path = "/b")
    public String methodB() {
        return "method b invoked";
    }
}
