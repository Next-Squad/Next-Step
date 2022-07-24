package reflection;

import org.junit.jupiter.api.Test;
import service.UserService;
import webserver.Request.HttpMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class ReflectionPracticeTest {

    static class Calculator{
        @MyAnnotation(httpMethod = HttpMethod.GET, path = "/test")
        int plus(int a, int b) {
            return a + b;
        }

        int multiply(int a, int b) {
            return a * b;
        }
    }



    @Test
    void userSave_invoke_no_return_test() throws InvocationTargetException, IllegalAccessException {
        UserService userService = new UserService();
        Class<? extends UserService> userServiceClass = userService.getClass();
        Method[] declaredMethods = userServiceClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.getName().equals("saveUser")) {
                declaredMethod.invoke(userService, "userId", "password", "name", "email@email.com");
            }
        }
    }

    @Test
    void calculator_plus_invoke() throws InvocationTargetException, IllegalAccessException {
        Calculator cacuclator = new Calculator();
        Class<? extends Calculator> calculatorClass = cacuclator.getClass();

        Method method = Arrays.stream(calculatorClass.getDeclaredMethods())
                .filter(m -> m.getName().equals("plus"))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
        Class<?> returnType = method.getReturnType();
        Object result = method.invoke(cacuclator, 1, 3);

        assertThat(returnType.isPrimitive()).isTrue();
        assertThat(returnType.equals(int.class)).isTrue();
        assertThat(result).isEqualTo(4);

    }

    @Test
    void AnnotationTest() throws InvocationTargetException, IllegalAccessException {
        Calculator calculator = new Calculator();
        Class<? extends Calculator> calculatorClass = calculator.getClass();

        Method method = Arrays.stream(calculatorClass.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(MyAnnotation.class))
                .filter(m -> {
                    MyAnnotation declaredAnnotation = m.getDeclaredAnnotation(MyAnnotation.class);
                    return declaredAnnotation.httpMethod().equals(HttpMethod.GET)
                            && declaredAnnotation.path().equals("/test");
                })
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        assertThat(method.getName()).isEqualTo("plus");
    }
}
