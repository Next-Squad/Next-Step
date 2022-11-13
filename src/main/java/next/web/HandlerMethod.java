package next.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerMethod {

    private final Object bean;
    private final Method method;

    public HandlerMethod(Object bean, Method method) {
        this.bean = bean;
        this.method = method;
    }

    public String handle(HttpServletRequest req, HttpServletResponse resp) {
        try {
            return (String) method.invoke(bean, req, resp);
        } catch (IllegalAccessException | InvocationTargetException e) {
           throw new RuntimeException("invoke error");
        }
    }

    @Override
    public String toString() {
        return "HandlerMethod{" +
                "bean=" + bean +
                ", method=" + method +
                '}';
    }
}
