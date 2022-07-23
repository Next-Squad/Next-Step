package db;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import model.User;

public class DataBase {

    private static final Map<String, User> users = new HashMap<>();

    public static void addUser(User user) {
        if (checkExistingUserId(user.getUserId())) {
            throw new IllegalArgumentException("중복된 userId 입니다.");
        }
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }

    private static boolean checkExistingUserId(String userId) {
        return users.containsKey(userId);
    }
}
