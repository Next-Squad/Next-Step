package dispatcher;

import webserver.Request.Request;
import webserver.Response.Response;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerMethod {

    private final Object bean;
    private final Method method;

    public HandlerMethod(Object bean, Method method) {
        this.bean = bean;
        this.method = method;
    }

    public Object getBean() {
        return bean;
    }

    public Method getMethod() {
        return method;
    }

    public Response invoke(Request request, Response response) throws InvocationTargetException, IllegalAccessException {
        return (Response) method.invoke(bean, request, response);
    }
}
