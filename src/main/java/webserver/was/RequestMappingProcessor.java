package webserver.was;

import handler.Handler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestMappingProcessor {
    
    private final Map<Class<?>, Object> handlerInstances = new HashMap<>();

    private final Map<RequestMappingInfo, Handler> handlerMappingInfo = new HashMap<>();

    public RequestMappingProcessor(List<Class<?>> handlers) {
        process(handlers);
    }

    private void process(List<Class<?>> handlerClasses) {
        for (Class<?> handlerClass : handlerClasses) {
            processHandlerClass(handlerClass);
        }
    }

    private void processHandlerClass(Class<?> handlerClass) {
        for (Field field : handlerClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(RequestMapping.class)) {
                // public 인지 확인
                validateModifierPublic(field);

                // Handler 타입의 필드인지 확인
                validateType(field, Handler.class);

                // 같은 RequestMapping 이 있는지 확인
                RequestMapping mapping = field.getAnnotation(RequestMapping.class);
                RequestMappingInfo requestMappingInfo = new RequestMappingInfo(mapping.method(), mapping.url());
                validateRequestMappingInfo(requestMappingInfo);

                // 핸들러가 정의된 클래스의 인스턴스가 생성된 것이 있는지 확인
                if (!isHandlerInstanceExists(handlerClass)) {
                    Object instance = createNewInstanceOfField(field);

                    handlerInstances.put(handlerClass, instance);
                }

                // 핸들러 인스턴스로부터 핸들러 추출
                Object handlerInstance = handlerInstances.get(handlerClass);
                addHandlerMappingInfo(requestMappingInfo, field, handlerInstance);
            }
        }
    }

    private void addHandlerMappingInfo(RequestMappingInfo requestMappingInfo, Field field, Object handlerInstance) {
        try {
            Handler handler = (Handler) field.get(handlerInstance);
            handlerMappingInfo.put(requestMappingInfo, handler);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("접근 가능한 핸들러가 없습니다", e);
        }
    }

    private boolean isHandlerInstanceExists(Class<?> handlerClass) {
        return handlerInstances.keySet().contains(handlerClass);
    }

    private Object createNewInstanceOfField(Field field) {
        Class<?> clazz = field.getDeclaringClass();

        try {
            Constructor<?> noArgusConstructor = clazz.getConstructor(null);

            return noArgusConstructor.newInstance(null);
        } catch (NoSuchMethodException |
                 InstantiationException |
                 IllegalAccessException |
                 InvocationTargetException e) {
            String fullPackageName = getFullPackageName(field.getDeclaringClass());
            String errorMessage = fullPackageName + "에 접근 가능한 기본 생성자가 없습니다.";

            throw new IllegalStateException(errorMessage, e);
        }
    }

    private void validateModifierPublic(Field field) {
        if (!Modifier.isPublic(field.getModifiers())) {
            throw new IllegalStateException(getFullPackageName(field) + "는 public 이어야 합니다.");
        }
    }

    private void validateType(Field field, Class<?> requiredClass) {
        if (!field.getType()
                .equals(requiredClass)) {
            String errorMessage = String.format("%s 는 %s 타입이어야 합니다.", getFullPackageName(field), requiredClass.getName());
            throw new IllegalStateException(errorMessage);
        }
    }

    private String getFullPackageName(Class<?> clazz) {
        String className = clazz.getName();

        return String.format("%s", className);
    }

    private String getFullPackageName(Field field) {
        String fullPackageName = getFullPackageName(field.getDeclaringClass());
        String fieldName = field.getName();

        return String.format("%s.%s", fullPackageName, fieldName);
    }

    private void validateRequestMappingInfo(RequestMappingInfo requestMappingInfo) {
        if (this.handlerMappingInfo.keySet().contains(requestMappingInfo)) {
            String errorMessage = String.format("중복된 %s 을 선언할 수 없습니다.(%s %s)",
                    RequestMapping.class,
                    requestMappingInfo.method(),
                    requestMappingInfo.url());

            throw new IllegalStateException(errorMessage);
        }
    }

    public Map<RequestMappingInfo, Handler> getHandlerMappingInfo() {
        return Map.copyOf(handlerMappingInfo);
    }
}
