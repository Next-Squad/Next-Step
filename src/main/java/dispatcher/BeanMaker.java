package dispatcher;

import com.google.common.reflect.ClassPath;
import handler.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanMaker {
    private static final Logger log = LoggerFactory.getLogger(BeanMaker.class);
    private final HandlerMappingV2 handlerMappingV2;

    public BeanMaker() {
        this.handlerMappingV2 = new HandlerMappingV2();
    }

    public HandlerMappingV2 getHandlerMappingV2() {
        return handlerMappingV2;
    }

    public void readComponents() throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Set<Class<?>> classes = ClassPath.from(ClassLoader.getSystemClassLoader())
                .getAllClasses()
                .stream()
                .filter(clazz -> clazz.getPackageName().equalsIgnoreCase("handler"))
                .map(clazz -> clazz.load())
                .filter(clazz -> clazz.isAnnotationPresent(Component.class))
                .collect(Collectors.toSet());

        for (Class<?> clazz : classes) {
            Constructor<?> declaredConstructor = clazz.getDeclaredConstructor();
            Object bean = declaredConstructor.newInstance();
            Method[] declaredMethods = clazz.getDeclaredMethods();

            for (Method declaredMethod : declaredMethods) {
                if (declaredMethod.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping requestMapping = declaredMethod.getDeclaredAnnotation(RequestMapping.class);
                    log.debug("class={}, decalredMethod={}, httpMethod={}, path={}", clazz.getName(), declaredMethod.getName(), requestMapping.httpMethod(), requestMapping.path());
                    RequestMappingInfo requestMappingInfo = new RequestMappingInfo(requestMapping.httpMethod(), requestMapping.path());
                    HandlerMethod handlerMethod = new HandlerMethod(bean, declaredMethod);
                    handlerMappingV2.addMappingInfo(requestMappingInfo, handlerMethod);
                }
            }
        }

    }
}
