package webserver.was;

import util.FileUtils;
import webserver.http.HttpResponseModel;

import java.lang.reflect.Field;
import java.util.Collection;

public class ViewResolver {

    /*
        이 클래스는 ViewResolver 와 Template Engine 의 역할을 겸합니다.
        아래 LOOP_TARGET_TAG_START, LOOP_TARGET_TAG_END, VALUE_TAG 는 현재 지원하는 간단한 문법 입니다.

        예)
        class User {
            String userId;
            public User(String userId) { this.userId = userId }
        }
        List<User> users = List.of(new User("jay"), new User("java-was"));
        -------------------------
        (변경 전) test.html
        <table>
            {{^users}}
            <tr>
                <td>{{userId}}</td>
            </tr>
            {{/users}}
        </table>
        -------------------------
        (변경 후) test.html
        <table>
            <tr>
                <td>jay</td>
                <td>java-was</td>
            </tr>
        </table>
        -------------------------
     */
    private static final String LOOP_TARGET_TAG_START = "{{^%s}}";
    private static final String LOOP_TARGET_TAG_END = "{{/%s}}";
    private static final String VALUE_TAG = "{{%s}}";

    private final String webAppPath;

    public ViewResolver(String webAppPath) {
        this.webAppPath = webAppPath;
    }

    public byte[] resolveView(String viewName) {
        return FileUtils.readFile(webAppPath + viewName);
    }

    public byte[] resolveView(String viewName, HttpResponseModel model) {
        if (model.keySet().size() > 0) {
            String file = new String(FileUtils.readFile(webAppPath + viewName));

            for (String name : model.keySet()) {
                Object value = model.get(name);
                if (value instanceof Collection<?>) {
                    file = resolveCollection(file, name, (Collection<?>)value);
                }
            }

            return file.getBytes();
        }

        return resolveView(viewName);
    }

    private String resolveCollection(String file, String name, Collection<?> collection) {
        String loopTarget = parseLoopTarget(file, name);
        StringBuilder loopResolvedResult = new StringBuilder();

        for (Object value : collection) {
            Class<?> objClass = value.getClass();
            loopResolvedResult.append(resolveObjectFields(loopTarget, objClass.getDeclaredFields(), value));
        }

        file = removeListViewTags(file, name);

        return file.replace(loopTarget, loopResolvedResult.toString());
    }

    private String parseLoopTarget(String file, String name) {
        String startTag = String.format(LOOP_TARGET_TAG_START, name);
        int startIndex = file.indexOf(startTag);
        int endIndex = file.indexOf(String.format(LOOP_TARGET_TAG_END, name));

        return file.substring(startIndex + startTag.length(), endIndex);
    }

    private String resolveObjectFields(String loopTarget, Field[] declaredFields, Object object) {
        try {
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                Object value = declaredField.get(object);

                String replaceTarget = String.format(VALUE_TAG, declaredField.getName());
                if (loopTarget.contains(replaceTarget) && value != null) {
                    loopTarget = loopTarget.replace(replaceTarget, value.toString());
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return loopTarget;
    }

    private String removeListViewTags(String file, String name) {
        file = file.replace(String.format(LOOP_TARGET_TAG_START, name), "");
        return file.replace(String.format(LOOP_TARGET_TAG_END, name), "");
    }
}
