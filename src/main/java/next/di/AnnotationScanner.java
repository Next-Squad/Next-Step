package next.di;

import next.web.HandlerKey;
import next.web.HandlerMapping;
import next.web.HandlerMethod;
import next.web.RequestMapping;
import next.web.controller.Controller;
import org.reflections.Reflections;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationScanner {

    private final Reflections reflections;

    // bean네임과 bean(객체)
    private final Map<String, Object> controllerMap;
    private final HandlerMapping handlerMapping;

    public AnnotationScanner(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
        this.controllerMap = extractController();
        this.handlerMapping = extractHandlerMapping();

    }

    private Map<String, Object> extractController() {
        Set<Class<?>> myControllers = reflections.getTypesAnnotatedWith(Controller.class);
        Map<String, Object> map = new HashMap<>();
        for( Class<?> clazz : myControllers ) {
            String path = clazz.getAnnotation(Controller.class).path();
            try {
                Constructor<?> declaredConstructor = clazz.getDeclaredConstructor();
                System.out.println(declaredConstructor.getName());
                map.put(path, declaredConstructor.newInstance());
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    private HandlerMapping extractHandlerMapping() {
        Set<Method> methods = reflections.getMethodsAnnotatedWith(RequestMapping.class);

        Map<HandlerKey, HandlerMethod> handlerMap = new HashMap<>();

        for(Method method : methods ) {

            RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
            String path = requestMapping.path();
            RequestMethod requestMethod = requestMapping.requestMethod();
            HandlerKey handlerKey = new HandlerKey(path, requestMethod);

            Object bean = this.controllerMap.get(method.getDeclaringClass().getSimpleName());
            handlerMap.put(handlerKey, new HandlerMethod(bean, method));
        }
        return new HandlerMapping(handlerMap);
    }




}
