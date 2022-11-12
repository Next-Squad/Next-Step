package next.di;

import next.web.controller.Controller;
import next.web.controller.HomeController;
import next.web.controller.MyController;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotationScanner {

    private final Reflections reflections;

    public AnnotationScanner(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<String, Controller> extractController() {
        Set<Class<?>> myControllers = reflections.getTypesAnnotatedWith(MyController.class);
        Map<String, Controller> map = new HashMap<>();
        for( Class<?> clazz : myControllers ) {
            System.out.println(clazz.getSimpleName());
            String path = clazz.getAnnotation(MyController.class).path();
            System.out.println(path);
            try {

                Constructor<?> declaredConstructor = clazz.getDeclaredConstructor();
                System.out.println(declaredConstructor.getName());
                map.put(path, (Controller) declaredConstructor.newInstance());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

        return map;
    }


}
